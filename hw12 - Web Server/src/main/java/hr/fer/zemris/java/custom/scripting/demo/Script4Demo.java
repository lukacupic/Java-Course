package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.hw12.Utility;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a demonstration program which executes Script 4 from HW.
 *
 * @author Luka Čupić
 */
public class Script4Demo {

    /**
     * The main method.
     *
     * @param args command lines arguments; not used in this program
     */
    public static void main(String[] args) {
        String documentBody = null;
        try {
            documentBody = Utility.readFromFile("src/main/resources/fibonacci.smscr");
        } catch (IOException e) {
            System.out.println("Error reading from file!");
        }

        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();

        new SmartScriptEngine(
            new SmartScriptParser(documentBody).getDocumentNode(),
            new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();

    }
}

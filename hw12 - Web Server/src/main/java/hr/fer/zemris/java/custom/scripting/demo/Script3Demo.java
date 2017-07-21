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
 * This is a demonstration program which executes Script 3 from HW.
 *
 * @author Luka Čupić
 */
public class Script3Demo {

    /**
     * The main method.
     *
     * @param args command lines arguments; not used in this program
     */
    public static void main(String[] args) {
        String documentBody = null;
        try {
            documentBody = Utility.readFromFile("src/main/resources/brojPoziva.smscr");
        } catch (IOException e) {
            System.out.println("Error reading from file!");
        }

        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        persistentParameters.put("brojPoziva", "3");
        RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);

        new SmartScriptEngine(
            new SmartScriptParser(documentBody).getDocumentNode(), rc
        ).execute();
        System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
    }
}

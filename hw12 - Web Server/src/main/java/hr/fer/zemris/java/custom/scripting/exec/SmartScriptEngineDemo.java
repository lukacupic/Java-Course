package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.hw12.Utility;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a demonstration program for the
 * {@link SmartScriptEngine}.
 *
 * @author Luka Čupić
 */
public class SmartScriptEngineDemo {

    /**
     * The main method.
     *
     * @param args command lines arguments; not used in this program
     */
    public static void main(String[] args) {
        String text = null;
        try {
            text = Utility.readFromFile("src/main/resources/smartScript.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> parameters = new HashMap<>();
        Map<String, String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();

        // put some parameter into parameters map
        parameters.put("broj", "4");

        // create engine and execute it
        new SmartScriptEngine(
            new SmartScriptParser(text).getDocumentNode(),
            new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();
    }
}

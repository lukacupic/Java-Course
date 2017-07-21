package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A simple program for testing the SmartScriptParser class.
 *
 * @author Luka Čupić
 */
public class SmartScriptTester {

    /**
     * The main method of the class.
     *
     * @param args command line arguments; accepts only one
     *             argument: path to the document to parse.
     */
    public static void main(String[] args) {

        if (args == null || args.length != 1) {
            System.exit(1);
        }

        String docBody = null;
        try {
            docBody = new String(
                Files.readAllBytes(Paths.get(args[0])),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.err.println("Error reading file.");
            System.exit(1);
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }

        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = createOriginalDocumentBody(document);

        //System.out.println(docBody);
        System.out.printf("%n==============================================%n%n");
        //System.out.println(originalDocumentBody);

        parser = new SmartScriptParser(originalDocumentBody);

        String odb2 = createOriginalDocumentBody(parser.getDocumentNode());

        System.out.println(odb2.equals(originalDocumentBody));
        System.out.println(odb2);

    }

    /**
     * Recreates the original document from the existing document model.
     *
     * @param document the parent node of the document model.
     * @return a recreated document from the existing document model.
     */
    public static String createOriginalDocumentBody(Node document) {
        if (document == null) return "";

        String string = "";

        if (document instanceof TextNode) {
            string += String.format("%s", ((TextNode) document).getText());
        } else if (document instanceof EchoNode) {
            string += "{$ = ";

            for (Element el : ((EchoNode) document).getElements()) {
                if (el instanceof ElementFunction) {
                    string += "@";
                }

                if (el instanceof ElementString) {
                    string += String.format("%s ", parseString(el.asText()));
                } else {
                    string += String.format("%s ", el.asText());
                }
            }
            string += "$}";
        } else if (document instanceof ForLoopNode) {
            string += "{$ for ";

            ForLoopNode node = (ForLoopNode) document;

            string += String.format("%s ", node.getVariable().asText());

            string += parseElement(node.getStartExpression());

            string += parseElement(node.getEndExpression());

            if (node.getStepExpression() != null) {
                string += parseElement(node.getStepExpression());
            }

            string += "$}";
        }

        for (int i = 0, size = document.numberOfChildren(); i < size; i++) {
            string += createOriginalDocumentBody(document.getChild(i));
        }

        if (document instanceof ForLoopNode) {
            string += "{$ end $}";
        }

        return string;
    }

    /**
     * Parses and returns the original string as it was written by the
     * user in the original document.
     *
     * @param string the string stored by the parser.
     * @return the original string.
     */
    private static String parseString(String string) {

        string = string.replace("\\", "\\\\");
        string = string.replace("\"", "\\\"");

        return "\"" + string + "\"";
    }

    /**
     * Parses an element and, if it is of class ElementString, adds the quotation
     * marks around it, to replicate the user's input document.
     *
     * @param element the element to parse.
     * @return a string representation of the element.
     */
    private static String parseElement(Element element) {
        String string = "";

        if (element instanceof ElementString) {
            string += String.format("%s ", parseString(element.asText()));
        } else {
            string += String.format("%s ", element.asText());
        }

        return string;
    }
}

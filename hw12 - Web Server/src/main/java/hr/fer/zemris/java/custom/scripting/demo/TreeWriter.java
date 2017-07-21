package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.visitors.TreeVisitor;
import hr.fer.zemris.java.hw12.Utility;

import java.io.IOException;

/**
 * This class represents a program which parses the specified
 * smart script and writes the tree-structure of the parsed
 * document onto the standard output.
 *
 * @author Luka Čupić
 */
public class TreeWriter {

    /**
     * The main method.
     *
     * @param args command line arguments; accepts a single
     *             argument, which specifies the path to the
     *             smart script file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Single argument expected: path to the file!");
            return;
        }

        String text;
        try {
            text = Utility.readFromFile(args[0]);
        } catch (IOException ex) {
            System.out.println("Could not read the file!");
            return;
        }

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();

        TreeVisitor visitor = new TreeVisitor();
        node.accept(visitor);
    }
}

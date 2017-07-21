package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartScriptParserTest {

    /**
     * A helper method for reading a file.
     *
     * @param filename the name of the file to read.
     * @return a string representation of the file.
     */
    public String readFile(String filename) {
        String docBody = null;
        try {
            docBody = new String(
                Files.readAllBytes(Paths.get(filename)),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.err.println("Error reading file.");
            System.exit(1);
        }

        return docBody;
    }

    @Test
    public void testParsingText() {
        String text = "my awesome lexically correct text";

        SmartScriptParser parser = new SmartScriptParser(text);
        Node node = parser.getDocumentNode();

        assertEquals(text, ((TextNode) node.getChild(0)).getText());
    }


    @Test
    public void testParsingForConstruct() {
        String text = "{$FOR i 0 \"10\" -2 $}";

        SmartScriptParser parser = new SmartScriptParser(text);

        ForLoopNode node = (ForLoopNode) parser.getDocumentNode().getChild(0);

        assertTrue(node.getVariable() != null);
        assertTrue(node.getStartExpression() instanceof ElementConstantInteger);
        assertTrue(node.getEndExpression() instanceof ElementString);
        assertTrue(node.getStepExpression() instanceof ElementConstantInteger);

        assertEquals("i", node.getVariable().asText());
        assertEquals("0", node.getStartExpression().asText());
        assertEquals("10", node.getEndExpression().asText());
        assertEquals("-2", node.getStepExpression().asText());
    }

    @Test(expected = SmartScriptParserException.class)
    public void testIllegalForConstruct() {
        String text = "{$FOR i \"lalala\" $}";

        new SmartScriptParser(text);
    }

    @Test
    public void testTextEscapeSequences() {
        String text = "Example \\{$=1$}.";

        SmartScriptParser parser = new SmartScriptParser(text);

        TextNode node = (TextNode) parser.getDocumentNode().getChild(0);

        assertEquals("Example {$=1$}.", node.getText());
    }

    @Test
    public void testTextEscapeSequencesAndEchoTag() {
        String text = "Example \\{$=1$}. Now actually write one {$=1$}";

        SmartScriptParser parser = new SmartScriptParser(text);

        TextNode textNode = (TextNode) parser.getDocumentNode().getChild(0);
        EchoNode echoNode = (EchoNode) parser.getDocumentNode().getChild(1);

        assertEquals("Example {$=1$}. Now actually write one ", textNode.getText());
        assertEquals("1", echoNode.getElements()[0].asText());
    }

    @Test
    public void testStringEscapeSequences() {
        String text = "{$= \"seq1: \\\\, seq2: \\\"\" $}";

        SmartScriptParser parser = new SmartScriptParser(text);

        EchoNode echoNode = (EchoNode) parser.getDocumentNode().getChild(0);

        assertEquals("seq1: \\, seq2: \"", echoNode.getElements()[0].asText());
        System.out.println();
    }

    @Test
    public void testParsingDocument1() {
        String filename = "src/test/resources/document1.txt";
        SmartScriptParser parser = new SmartScriptParser(readFile(filename));

        ForLoopNode fourthNode
            = (ForLoopNode) parser.getDocumentNode()
            .getChild(0)
            .getChild(1)
            .getChild(1)
            .getChild(1);

        assertTrue(fourthNode.getVariable() != null);
        assertTrue(fourthNode.getStartExpression() instanceof ElementConstantInteger);
        assertTrue(fourthNode.getEndExpression() instanceof ElementString);
        assertTrue(fourthNode.getStepExpression() == null);

        assertEquals("cetrdeset_dva", fourthNode.getVariable().asText());
        assertEquals("1", fourthNode.getStartExpression().asText());
        assertEquals("-10", fourthNode.getEndExpression().asText());
    }

    @Test
    public void testParsingDocument2() {
        String filename = "src/test/resources/document2.txt";
        SmartScriptParser parser = new SmartScriptParser(readFile(filename));

        EchoNode node = (EchoNode) parser.getDocumentNode().getChild(0);

        Element[] elements = node.getElements();

        assertTrue(elements[0] instanceof ElementVariable);
        assertTrue(elements[1] instanceof ElementVariable);
        assertTrue(elements[2] instanceof ElementOperator);
        assertTrue(elements[3] instanceof ElementFunction);
        assertTrue(elements[4] instanceof ElementString);
        assertTrue(elements[5] instanceof ElementFunction);

        assertEquals("a", elements[0].asText());
        assertEquals("b", elements[1].asText());
        assertEquals("^", elements[2].asText());
        assertEquals("cos", elements[3].asText());
        assertEquals("3.141", elements[4].asText());
        assertEquals("my_awesome_function", elements[5].asText());
    }
}

package hr.fer.zemris.java.custom.scripting.visitors;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * This class represents a node visitor which recursively
 * visits a {@link SmartScriptParser}'s document model and
 * writes the tree (i.e. the nodes) on the standard output.
 *
 * @author Luka Čupić
 */
public class TreeVisitor implements INodeVisitor {

    /**
     * Creates a new instance of this class.
     */
    public TreeVisitor() {
    }

    @Override
    public void visitTextNode(TextNode node) {
        System.out.print(String.format("%s", node.getText()));

        VisitorUtil.visitChildren(node, this);
    }

    @Override
    public void visitForLoopNode(ForLoopNode node) {
        StringBuilder sb = new StringBuilder();

        sb.append("{$ for ");
        sb.append(String.format("%s ", node.getVariable().asText()));
        sb.append(parseElement(node.getStartExpression()));
        sb.append(parseElement(node.getEndExpression()));

        if (node.getStepExpression() != null) {
            sb.append(parseElement(node.getStepExpression()));
        }
        sb.append("$}");
        System.out.print(sb.toString());

        VisitorUtil.visitChildren(node, this);
        System.out.println("{$ end $}");
    }

    @Override
    public void visitEchoNode(EchoNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("{$ = ");

        for (Element e : node.getElements()) {
            if (e instanceof ElementFunction) {
                sb.append("@");
            }
            if (e instanceof ElementString) {
                sb.append(String.format("%s ", restoreEscapeSeqs(e.asText())));
            } else {
                sb.append(String.format("%s ", e.asText()));
            }
        }
        sb.append("$}");
        System.out.print(sb.toString());

        VisitorUtil.visitChildren(node, this);
    }

    @Override
    public void visitDocumentNode(DocumentNode node) {
        VisitorUtil.visitChildren(node, this);
    }

    /**
     * Parses an element and, if it is of class ElementString, adds the quotation
     * marks around it, to replicate the user's input document.
     *
     * @param element the element to parse
     * @return a string representation of the element
     */
    private static String parseElement(Element element) {
        String string = "";

        if (element instanceof ElementString) {
            string += String.format("%s ", restoreEscapeSeqs(element.asText()));
        } else {
            string += String.format("%s ", element.asText());
        }

        return string;
    }

    /**
     * Restores the escape sequences to the given string.
     *
     * @param string the string stored by the parser
     * @return the given string as it was written by the
     * user in the original document
     */
    private static String restoreEscapeSeqs(String string) {
        string = string.replace("\\", "\\\\");
        string = string.replace("\"", "\\\"");
        return "\"" + string + "\"";
    }
}

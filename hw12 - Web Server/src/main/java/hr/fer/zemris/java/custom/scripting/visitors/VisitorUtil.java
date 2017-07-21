package hr.fer.zemris.java.custom.scripting.visitors;

import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;

/**
 * This class is a utility class for the {@link INodeVisitor}s.
 * It offers static methods used for performing some miscellaneous
 * tasks.
 *
 * @author Luka Čupić
 */
public class VisitorUtil {

    /**
     * A helper method which recursively visits all the children of the
     * given document node.
     *
     * @param node the node whose children is to be visited
     */
    public static void visitChildren(Node node, INodeVisitor visitor) {
        for (int i = 0, size = node.numberOfChildren(); i < size; i++) {
            node.getChild(i).accept(visitor);
        }
    }
}
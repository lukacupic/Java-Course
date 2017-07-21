package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.visitors.ScriptEvaluator;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class represents a smart-script engine.
 * <p>
 * An instance of this class receives a document node
 * (representing the root of the document model tree)
 * and the request context object, and evaluates the
 * script. The result of the evaluation is then sent
 * to the request context object, which writes the
 * result to it's output stream.
 *
 * @author Luka Čupić
 */
public class SmartScriptEngine {

    /**
     * Represents the root of the document to evaluate.
     */
    private DocumentNode documentNode;

    /**
     * Represents the request context object.
     */
    private RequestContext requestContext;

    /**
     * Represents the multi-stack object.
     */
    private ObjectMultistack multistack;

    /**
     * Represents the visitor which will evaluate
     * the expression.
     */
    private ScriptEvaluator visitor;

    /**
     * Creates a new instance of the {@link SmartScriptEngine}.
     *
     * @param documentNode   the root node representing the document
     *                       to execute
     * @param requestContext the request context object
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;
        this.multistack = new ObjectMultistack();
        this.visitor = new ScriptEvaluator(requestContext, multistack);
    }

    /**
     * Evaluates the expression represented by the document node
     * given through the class constructor.
     */
    public void execute() {
        documentNode.accept(visitor);
    }
}
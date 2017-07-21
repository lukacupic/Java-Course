package hr.fer.zemris.java.custom.scripting.visitors;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Stack;

/**
 * This class represents a visitor which evaluates the
 * expression, represented by the root node on which the
 * visitor has been called upon.
 *
 * @author Luka Čupić
 */
public class ScriptEvaluator implements INodeVisitor {

    /**
     * Represents the request context object, inherited
     * from the {@link SmartScriptEngine}.
     */
    private RequestContext requestContext;

    /**
     * Represents the multi-stack for storing values, inherited
     * from the {@link SmartScriptEngine}.
     */
    private ObjectMultistack multistack;

    /**
     * Creates a new instance of this class.
     *
     * @param requestContext the request context object
     * @param multistack     the multi-stack for storing values
     */
    public ScriptEvaluator(RequestContext requestContext, ObjectMultistack multistack) {
        this.requestContext = requestContext;
        this.multistack = multistack;
    }

    @Override
    public void visitDocumentNode(DocumentNode node) {
        int noOfChildren = node.numberOfChildren();
        for (int i = 0; i < noOfChildren; i++) {
            node.getChild(i).accept(this);
        }
    }

    @Override
    public void visitTextNode(TextNode node) {
        try {
            requestContext.write(node.getText());
        } catch (IOException ignorable) {
        }
    }

    /**
     * @throws IllegalArgumentException if the given for-loop construct contains
     *                                  illegal values
     */
    @Override
    public void visitForLoopNode(ForLoopNode node) throws IllegalArgumentException {
        try {
            int start = Integer.parseInt(node.getStartExpression().asText());
            int end = Integer.parseInt(node.getEndExpression().asText());
            int step = Integer.parseInt(node.getStepExpression().asText());

            if (start > end) {
                throw new IllegalArgumentException();
            }

            String varName = node.getVariable().getName();
            multistack.push(varName, new ValueWrapper(start));

            for (int i = start; i <= end; i += step) {
                VisitorUtil.visitChildren(node, this);
                ValueWrapper vw = multistack.pop(varName);
                vw.add(step);
                multistack.push(varName, vw);
            }
            multistack.pop(varName);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Illegal for-loop value!");
        }
    }

    @Override
    public void visitEchoNode(EchoNode node) {
        Stack<Object> stack = new Stack<>();

        for (Element e : node.getElements()) {
            if (e instanceof ElementConstantInteger) {
                stack.push(((ElementConstantInteger) e).getValue());
                continue;
            }
            if (e instanceof ElementConstantDouble) {
                stack.push(((ElementConstantDouble) e).getValue());
                continue;
            }
            if (e instanceof ElementString) {
                stack.push(((ElementString) e).getValue());
            }
            if (e instanceof ElementVariable) {
                stack.push(multistack.peek(((ElementVariable) e).getName()).getValue());
            }
            if (e instanceof ElementOperator) {
                ElementOperator op = (ElementOperator) e;
                ValueWrapper v1 = new ValueWrapper(stack.pop());
                ValueWrapper v2 = new ValueWrapper(stack.pop());

                performOperation(op, v1, v2);
                stack.push(v1.getValue());
            }
            if (e instanceof ElementFunction) {
                ElementFunction fun = (ElementFunction) e;
                applyFunction(fun, stack);
            }
        }

        if (!stack.isEmpty()) {
            for (Object o : stack.subList(0, stack.size())) {
                try {
                    requestContext.write((String.valueOf(o)));
                } catch (IOException ignorable) {
                }
            }
        }
    }

    /**
     * Applies the given function upon an arbitrary number of arguments
     * taken from the stack.
     *
     * @param fun   the function to apply
     * @param stack a stack containing the attributes
     */
    private void applyFunction(ElementFunction fun, Stack<Object> stack) {
        String s = fun.asText();
        if (s.equals("sin")) {
            Object obj = stack.pop();
            Double value = obj instanceof Double ? (Double) obj : ((Integer) obj).doubleValue();
            stack.push(Math.sin(value * Math.PI / 180));

        } else if (s.equals("decfmt")) {
            DecimalFormat f = new DecimalFormat((String) stack.pop());
            stack.push(f.format(stack.pop()));

        } else if (s.equals("dup")) {
            stack.push(stack.peek());

        } else if (s.equals("swap")) {
            Object a = stack.pop();
            Object b = stack.pop();
            stack.push(a);
            stack.push(b);

        } else if (s.equals("setMimeType")) {
            requestContext.setMimeType(String.valueOf(stack.pop()));

        } else if (s.equals("paramGet")) {
            getParameter(requestContext.getParameters(), stack);

        } else if (s.equals("pparamGet")) {
            getParameter(requestContext.getPersistentParameters(), stack);

        } else if (s.equals("pparamSet")) {
            requestContext.setPersistentParameter((String.valueOf(stack.pop())),
                String.valueOf(stack.pop())
            );

        } else if (s.equals("pparamDel")) {
            requestContext.getPersistentParameters().remove(String.valueOf(stack.pop()));

        } else if (s.equals("tparamGet")) {
            getParameter(requestContext.getTemporaryParameters(), stack);

        } else if (s.equals("tparamSet")) {
            requestContext.setTemporaryParameter((String.valueOf(stack.pop())),
                String.valueOf(stack.pop())
            );

        } else if (s.equals("tparamDel")) {
            requestContext.getTemporaryParameters().remove(String.valueOf(stack.pop()));
        }
    }

    /**
     * A helper method for performing either the {@code paramGet},
     * {@code pparamGet} or the {@code tparamGet} function.
     *
     * @param parameters the map of the parameters
     * @param stack      the stack object
     */
    private void getParameter(Map<String, String> parameters, Stack<Object> stack) {
        Object defValue = stack.pop();
        Object name = stack.pop();

        String value = parameters.get(String.valueOf(name));
        stack.push(value != null ? value : defValue);
    }

    /**
     * Performs a mathematical operation, specified by the value of the given
     * element operator object upon the two given operands.
     *
     * @param op the operator
     * @param v1 the first operand
     * @param v2 the second operand
     */
    private void performOperation(ElementOperator op, ValueWrapper v1, ValueWrapper v2) {
        switch (op.getSymbol()) {
            case "+":
                v1.add(v2.getValue());
                break;
            case "-":
                v1.subtract(v2.getValue());
                break;
            case "*":
                v1.multiply(v2.getValue());
                break;
            case "/":
                v1.divide(v2.getValue());
                break;
        }
    }
}

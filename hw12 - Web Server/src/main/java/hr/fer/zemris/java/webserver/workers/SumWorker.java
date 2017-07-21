package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class represents a server worker which is used
 * for calculating the sum of two integers and displaying
 * the result.
 *
 * @author Luka Čupić
 */
public class SumWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/html");

        int a;
        int b;
        try {
            a = Integer.parseInt(context.getParameter("a"));
        } catch (NumberFormatException ex) {
            a = 1;
        }
        try {
            b = Integer.parseInt(context.getParameter("b"));
        } catch (NumberFormatException ex) {
            b = 1;
        }
        context.setTemporaryParameter("a", String.valueOf(a));
        context.setTemporaryParameter("b", String.valueOf(b));
        context.setTemporaryParameter("zbroj", String.valueOf(a + b));
        context.getDispatcher().dispatchRequest("private/calc.smscr");
    }
}

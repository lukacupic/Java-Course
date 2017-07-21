package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Map;

/**
 * This class represents a web worker which simply writes
 * all the parameters which where given by the user, in a
 * table-like structure. The table consists of two columns
 * (name and value) and the number of rows, equivalent to
 * the number of parameters provided by the user.
 *
 * @author Luka Čupić
 */
public class EchoParams implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/html");

        context.write("<html><body>");
        context.write("<table>");

        context.write("<tr>");
        context.write("<th>Name</th>");
        context.write("<th>Value</th>");
        context.write("</tr>");

        for (Map.Entry<String, String> entry : context.getParameters().entrySet()) {
            context.write("<tr>");
            context.write("<td>" + entry.getKey() + "</td>");
            context.write("<td>" + entry.getValue() + "</td>");
            context.write("</tr>");
        }

        context.write("</tr>");
        context.write("</table>");
        context.write("</body></html>");
    }
}
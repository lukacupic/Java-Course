package hr.fer.zemris.java.webserver;

/**
 * This interface represents a web worker which is used to perform
 * a certain task. The {@link #processRequest} method must provide
 * an implementation which performs a certain task whose result will
 * be visible by the client.
 *
 * @author Luka Čupić
 */
public interface IWebWorker {

    /**
     * Processes the request by performing the task specified by
     * the implementation of this method.
     *
     * @param context the context which should be used for the
     *                communication with the client.
     * @throws Exception if an error occurs while processing the request
     */
    void processRequest(RequestContext context) throws Exception;
}

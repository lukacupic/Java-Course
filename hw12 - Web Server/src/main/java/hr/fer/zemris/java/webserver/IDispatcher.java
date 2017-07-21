package hr.fer.zemris.java.webserver;

/**
 * This interface represents a dispatcher of the user requests.
 *
 * @author Luka Čupić
 */
public interface IDispatcher {

    /**
     * Dispatches the request specified by the given path (i.e. it
     * analyzes the path and determines how it should be processed.
     *
     * @param urlPath the path
     * @throws Exception if an error occurs while dispatching the request
     */
    void dispatchRequest(String urlPath) throws Exception;
}
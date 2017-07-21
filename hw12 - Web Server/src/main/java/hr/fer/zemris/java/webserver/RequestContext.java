package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The {@link RequestContext} class represents the data of a web context.
 * This class is used as a bridge between a {@link SmartHttpServer.ClientWorker}
 * and the end user, in a sense that most contexts sent by a client worker are
 * performed through an instance of this class (this excludes any error
 * messages that might occur due to a bad request received from the user;
 * in such case, a direct context is sent by the worker, which is then not
 * relying on the implementation of this class.
 *
 * @author Luka Čupić
 */
public class RequestContext {

    /**
     * Represents the encoding for this context with the default value.
     */
    private String encoding = "UTF-8";

    /**
     * Represents the status code with the default value.
     */
    private int statusCode = 200;

    /**
     * Represents the status message with the default value.
     */
    private String statusText = "OK";

    /**
     * Represents the mime type with the default value.
     */
    private String mimeType = "text/html";

    /**
     * The output stream for writing data.
     */
    private OutputStream outputStream;

    /**
     * A {@link Charset} object, representing the current
     * charset of the context.
     */
    private Charset charset = Charset.forName(encoding);

    /**
     * A map holding the parameters.
     */
    private Map<String, String> parameters;

    /**
     * A map holding the persistent parameters.
     */
    private Map<String, String> persistentParameters;

    /**
     * A map holding the temporary parameters.
     */
    private Map<String, String> temporaryParameters;

    /**
     * Represents the cookies for the context.
     */
    private List<RCCookie> outputCookies;

    /**
     * A flag which tells whether or not the header has been generated.
     */
    private boolean headerGenerated = false;

    /**
     * Represents the request dispatcher.
     */
    private IDispatcher dispatcher;

    /**
     * Creates a new {@link RequestContext} object.
     *
     * @param outputStream         the stream to write to
     * @param parameters           the map of parameters
     * @param persistentParameters the map of persistent parameters
     * @param outputCookies        the list of cookies
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters,
                          Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output Stream must not be null!");
        } else {
            this.outputStream = outputStream;
        }

        this.parameters = parameters != null ?
            parameters : new HashMap<>();

        this.persistentParameters = persistentParameters != null ?
            persistentParameters : new HashMap<>();

        this.temporaryParameters = temporaryParameters != null ?
            temporaryParameters : new HashMap<>();

        this.outputCookies = outputCookies != null ?
            outputCookies : new ArrayList<>();
    }

    /**
     * Creates a new {@link RequestContext} object.
     *
     * @param outputStream         the stream to write to
     * @param parameters           the map of parameters
     * @param persistentParameters the map of persistent parameters
     * @param outputCookies        the list of cookies
     * @param temporaryParameters  the map of temporary parameters
     * @param dispatcher           the dispatcher
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters,
                          Map<String, String> persistentParameters, List<RCCookie> outputCookies,
                          Map<String, String> temporaryParameters, IDispatcher dispatcher) {
        this(outputStream, parameters, persistentParameters, outputCookies);

        this.temporaryParameters = temporaryParameters;
        this.dispatcher = dispatcher;
    }

    /**
     * Writes the given text to the output stream of this context.
     *
     * @param text the text to write
     * @throws IOException if an error occurs while writing to the
     *                     output stream
     */
    public RequestContext write(String text) throws IOException {
        write(text.getBytes(charset));
        return this;
    }

    /**
     * Writes the given bytes to the output stream of this context.
     *
     * @param data the bytes to write
     * @throws IOException if an error occurs while writing to the
     *                     output stream
     */
    public RequestContext write(byte[] data) throws IOException {
        if (!headerGenerated) {
            generateHeader();
            headerGenerated = true;
        }
        outputStream.write(data);
        outputStream.flush();
        return this;
    }

    /**
     * Generates the header for this context. This method will be called
     * only once per a context - before any user-data has been written to
     * the output stream.
     *
     * @throws IOException if an error occurs while writing to the
     *                     output stream
     */
    private void generateHeader() throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");

        if (mimeType.startsWith("text/")) {
            mimeType = mimeType + "; charset=" + encoding;
        }
        sb.append("Content-Type: " + mimeType + "\r\n");

        if (outputCookies != null) {
            for (RCCookie cookie : outputCookies) {
                sb.append("Set-Cookie: " + cookie.getName() + "=\"" + cookie.getValue() + "\"; ");

                if (cookie.getDomain() != null) {
                    sb.append("Domain=" + cookie.getDomain());
                }
                if (cookie.getPath() != null) {
                    sb.append("; Path=" + cookie.getPath());
                }
                if (cookie.getMaxAge() != null) {
                    sb.append("; Max-Age=" + cookie.getMaxAge());
                }
                if (cookie.isHttpOnly()) {
                    sb.append("; HttpOnly");
                }
                sb.append("\r\n");
            }
            sb.append("\r\n");
        }

        outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
        outputStream.flush();
    }

    // getters and setters

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Returns the {@link #parameters} map.
     *
     * @return a map which represents the parameters
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Returns the {@link #persistentParameters} map.
     *
     * @return a map which represents persistent parameters
     */
    public Map<String, String> getPersistentParameters() {
        return persistentParameters;
    }

    /**
     * Returns the {@link #temporaryParameters} map.
     *
     * @return a map which represents temporary parameters
     */
    public Map<String, String> getTemporaryParameters() {
        return temporaryParameters;
    }

    /**
     * Sets the {@link #persistentParameters} to a new value.
     *
     * @param persistentParameters a new map for the persistent parameters
     */
    public void setPersistentParameters(Map<String, String> persistentParameters) {
        this.persistentParameters = persistentParameters;
    }

    /**
     * Sets the {@link #temporaryParameters} to a new value.
     *
     * @param temporaryParameters a new map for the temporary parameters
     */
    public void setTemporaryParameters(Map<String, String> temporaryParameters) {
        this.temporaryParameters = temporaryParameters;
    }

    /**
     * Retrieves a value of the parameter from the {@link #parameters}
     * map, specified by the given name of the parameter.
     *
     * @param name the name of the parameter to get, or null if the
     *             given name does not map to any of the values
     * @return a single parameter whose value is mapped to by the
     * given name
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Retrieves a persistent parameter from the {@link #persistentParameters}
     * map, specified by the given name of the persistent parameter.
     *
     * @param name the name of the persistent parameter to get, or null
     *             if the given name does not map to any of the values
     * @return a single persistent parameter whose value is mapped to by
     * the given name
     */
    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    /**
     * Retrieves a temporary parameter from the {@link #temporaryParameters}
     * map, specified by the given name of the temporary parameter.
     *
     * @param name the name of the temporary parameter to get, or null
     *             if the given name does not map to any of the values
     * @return a single temporary parameter whose value is mapped to by
     * the given name
     */
    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    /**
     * Adds a new parameter to the {@link #persistentParameters} map, or replaces
     * the old value if it exists.
     *
     * @param name  the name of the new parameter
     * @param value the value of the new parameter
     */
    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    /**
     * Adds a new parameter to the {@link #temporaryParameters} map, or replaces
     * the old value if it exists.
     *
     * @param name  the name of the new parameter
     * @param value the value of the new parameter
     */
    public void setTemporaryParameter(String name, String value) {
        temporaryParameters.put(name, value);
    }

    /**
     * Removes a parameter, specified by the given name, from the
     * {@link #persistentParameters} map.
     *
     * @param name the name of the parameter to remove
     */
    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    /**
     * Removes a parameter, specified by the given name, from the
     * {@link #temporaryParameters} map.
     *
     * @param name the name of the parameter to remove
     */
    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    /**
     * Returns an unmodifiable set of names from the {@link #parameters} map.
     *
     * @return a read-only set of names from the {@link #parameters} map.
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Returns an unmodifiable set of names from the {@link #persistentParameters}
     * map.
     *
     * @return a read-only set of names from the {@link #persistentParameters}
     * map.
     */
    public Set<String> getPersistenstParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * Returns an unmodifiable set of names from the {@link #temporaryParameters}
     * map.
     *
     * @return a read-only set of names from the {@link #temporaryParameters}
     * map.
     */
    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * Sets the encoding for this context.
     *
     * @param encoding a string representation of the encoding
     *                 to set
     */
    public void setEncoding(String encoding) {
        checkHeader();
        this.encoding = encoding;
        this.charset = Charset.forName(encoding);
    }

    /**
     * Sets the status code for this context.
     *
     * @param statusCode the status code to set
     * @throws IllegalStateException if the header has been changed
     *                               prior to calling this method
     */
    public void setStatusCode(int statusCode) throws IllegalStateException {
        checkHeader();
        this.statusCode = statusCode;
    }

    /**
     * Sets the status text for this context.
     *
     * @param statusText the status text to set
     * @throws IllegalStateException if the header has been changed
     *                               prior to calling this method
     */
    public void setStatusText(String statusText) throws IllegalStateException {
        checkHeader();
        this.statusText = statusText;
    }

    /**
     * Sets the mime type for this context.
     *
     * @param mimeType the mime type to set
     * @throws IllegalStateException if the header has been changed
     *                               prior to calling this method
     */
    public void setMimeType(String mimeType) throws IllegalStateException {
        checkHeader();
        this.mimeType = mimeType;
    }

    /**
     * Adds a cookie to this context.
     *
     * @param cookie the cookie to add
     * @throws IllegalStateException if the header has been changed
     *                               prior to calling this method
     */
    public void addRCCookie(RCCookie cookie) throws IllegalStateException {
        checkHeader();
        outputCookies.add(cookie);
    }

    /**
     * A helper method which throws an exception if the header has been created.
     */
    private void checkHeader() {
        if (headerGenerated) {
            throw new IllegalStateException("Header attributes cannot be modified" +
                "after the header has been created!");
        }
    }

// end of getters and setters

    /**
     * This class represents a request-context cookie.
     *
     * @author Luka Čupić
     */
    public static class RCCookie {

        /**
         * The name of the cookie.
         */
        private String name;

        /**
         * The value of the cookie.
         */
        private String value;

        /**
         * The maximum lifespan of the cookie, in seconds.
         */
        private Integer maxAge;

        /**
         * The domain for which the cookie is valid.
         */
        private String domain;

        /**
         * The path of the cookie.
         */
        private String path;

        /**
         * A flag which tells whether this cookie is
         * http-only.
         */
        private boolean isHttpOnly;

        /**
         * Creates a new cookie.
         *
         * @param name   the name of the cookie
         * @param value  the value of the cookie
         * @param maxAge the maximum age of the cookie
         * @param domain the domain of the cookie
         * @param path   the path of the cookie
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean isHttpOnly) {
            this.name = name;
            this.value = value;
            this.maxAge = maxAge;
            this.domain = domain;
            this.path = path;
            this.isHttpOnly = isHttpOnly;
        }

        /**
         * Gets the name of this cookie.
         *
         * @return the cookie's name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the value of this cookie.
         *
         * @return the cookie's value
         */
        public String getValue() {
            return value;
        }

        /**
         * Gets the maximum age of this cookie.
         *
         * @return the cookie's maximum age
         */
        public Integer getMaxAge() {
            return maxAge;
        }

        /**
         * Gets the domain of this cookie.
         *
         * @return the cookie's domain
         */
        public String getDomain() {
            return domain;
        }

        /**
         * Gets the path of this cookie.
         *
         * @return the cookie's path
         */
        public String getPath() {
            return path;
        }

        /**
         * Checks if this cookie is denoted as "http only".
         *
         * @return true if this cookie is "http only"; false
         * otherwise
         */
        public boolean isHttpOnly() {
            return isHttpOnly;
        }
    }
}
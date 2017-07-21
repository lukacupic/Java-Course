package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Smart HTTP server is a multipurpose server which allows
 * clients to perform different actions when accessing the server.
 * <p>
 * One of the actions a client can perform is accessing certain
 * files in the main client directory of the server, which serves
 * as a place with shared data, which all clients accessing the
 * server can see. The list of allowed file extensions are defined
 * in the "mime.properties" file.
 * <p>
 * Another possible action is executing smart scripts directly
 * from the server: all scripts are located in the "/scripts"
 * subdirectory.
 * <p>
 * Several "server procedures" called "workers" can also be called,
 * which in turn do a certain job for the client. For example, calling
 * "/ext/HelloWorker" greets the client with a proper message.
 * <p>
 * The server properties can be modified in the "server.properties"
 * configuration file.
 *
 * @author Luka Čupić
 */
public class SmartHttpServer {

	/**
	 * The address of the server.
	 */
	private String address;

	/**
	 * The port on which this server operates.
	 */
	private int port;

	/**
	 * Holds the number of worker threads on this server.
	 */
	private int workerThreads;

	/**
	 * Represents the absolute path to the root directory from
	 * which the files are server.
	 */
	private Path documentRoot;

	/**
	 * A map which holds the mime types, as specified in the
	 * "mime.properties" configuration file.
	 */
	private Map<String, String> mimeTypes = new HashMap<>();

	/**
	 * Holds the maximum duration of client sessions, in seconds.
	 */
	private volatile int sessionTimeout;

	/**
	 * Represents the thread pool which contains the client
	 * worker threads.
	 */
	private ExecutorService threadPool;

	/**
	 * Represents the main server thread.
	 */
	private ServerThread serverThread;

	/**
	 * A flag which tells whether the server is currently running.
	 */
	private volatile boolean isRunning = false;

	/**
	 * A map which maps a path of a worker (the one specified
	 * in the workers.properties file) to an actual worker of
	 * that type.
	 */
	private Map<String, IWebWorker> workersMap;

	/**
	 * Represents a map of the existing client sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();

	/**
	 * Represents the random number generator used for generating
	 * unique client session ID's.
	 */
	private static Random sessionRandom = new Random();

	/**
	 * Creates a new SmartHTTP WebServer.
	 *
	 * @param configFile the path to the configuration files
	 * @throws IOException if an error occurs while reading the
	 *                     configuration file
	 */
	public SmartHttpServer(String configFile) throws IOException {
		Map<String, String> properties = getPropertyTypes(configFile);

		this.address = properties.get("server.address");
		this.port = Integer.parseInt(properties.get("server.port"));
		this.workerThreads = Integer.parseInt(properties.get("server.workerThreads"));
		this.documentRoot = Paths.get(properties.get("server.documentRoot"));
		this.mimeTypes = getPropertyTypes(properties.get("server.mimeConfig"));
		this.sessionTimeout = Integer.parseInt(properties.get("session.timeout"));
		this.workersMap = getWorkersMap(properties.get("server.workers"));
	}

	/**
	 * Reads the properties file, specified by the given path, and
	 * returns a map which maps a property's name to it's value.
	 *
	 * @param filepath the path to the properties file
	 * @return a map which maps of properties
	 * @throws IOException if the properties file cannot be read
	 */
	private Map<String, String> getPropertyTypes(String filepath) throws IOException {
		InputStream is = new FileInputStream(filepath);
		Properties p = new Properties();
		p.load(is);
		is.close();

		Map<String, String> types = new HashMap<>();
		for (Map.Entry entry : p.entrySet()) {
			types.put((String) entry.getKey(), (String) entry.getValue());
		}
		return types;
	}

	/**
	 * Reads the workers' properties file, specified by the given path
	 * and returns a map which maps a URL path for specifying a worker
	 * to a reference to an actual worker object.
	 *
	 * @param filepath the path to the workers' properties file
	 * @return a map which maps a URL path for a worker to this worker
	 * object
	 * @throws IOException if the workers properties file cannot be read
	 */
	private Map<String, IWebWorker> getWorkersMap(String filepath) throws IOException {
		Map<String, IWebWorker> workers = new HashMap<>();
		for (Map.Entry<String, String> entry : getPropertyTypes(filepath).entrySet()) {
			String path = entry.getKey();
			String fqcn = entry.getValue();

			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				workers.put(path, iww);
			} catch (Exception ex) {
				throw new IOException(ex);
			}
		}
		return workers;
	}

	/**
	 * Starts the main server thread. Invocation has no effect if
	 * the server has already started.
	 */
	protected synchronized void start() {
		if (isRunning) return;
		isRunning = true;

		threadPool = Executors.newFixedThreadPool(workerThreads);

		serverThread = new ServerThread();
		serverThread.start();
	}

	/**
	 * Stops the main server thread. Invocation has no effect if
	 * the server is already stopped.
	 */
	protected synchronized void stop() {
		if (!isRunning) return;

		threadPool.shutdown();
		serverThread.interrupt();
		isRunning = false;
	}

	/**
	 * This class represents the main thread of the HTTP server.
	 * Once started, the server thread will listen to any requests
	 * to make a connection to the server; all these requests will
	 * be accepted and delegated to a new {@link ClientWorker} which
	 * will then serve the connected client.
	 *
	 * @author Luka Čupić
	 */
	protected class ServerThread extends Thread {

		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress((InetAddress) null, port));

				GarbageCollector gc = new GarbageCollector(300);
				gc.start();

				while (isRunning) {
					System.out.println("Waiting for a request...");
					Socket client = serverSocket.accept();

					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException ignorable) {
			}
		}
	}

	/**
	 * This class represents a garbage collector thread for
	 * the {@link SmartHttpServer}. It periodically goes through
	 * the {@link #sessions} map and removes all expired records.
	 */
	protected class GarbageCollector extends Thread {

		/**
		 * Represents the length of the cycle in between two
		 * consecutive cleaning operations (in seconds).
		 */
		private long cycle;

		/**
		 * Creates a new instance of this class.
		 *
		 * @param cycle the time duration before another pass
		 *              of removing sessions is performed
		 */
		public GarbageCollector(long cycle) {
			this.setDaemon(true);
			this.cycle = cycle;
		}

		@Override
		public void run() {
			while (isRunning) {
				for (Map.Entry<String, SessionMapEntry> e : sessions.entrySet()) {
					String sid = e.getKey();
					SessionMapEntry session = e.getValue();

					if (isExpired(session)) {
						sessions.remove(sid);
					}
				}

				try {
					Thread.sleep(cycle * 1000);
				} catch (InterruptedException ignorable) {
				}
			}
		}
	}

	/**
	 * Checks if the specified client session has expired.
	 *
	 * @param session the session to check
	 * @return true if the session has expired; false otherwise
	 */
	private boolean isExpired(SessionMapEntry session) {
		return System.currentTimeMillis() / 1000 >= session.validUntil;
	}

	/**
	 * This class represents a session for a single client.
	 * Each time a new client is encountered, a new instance
	 * of this class is created and stored in the {@link #sessions}
	 * map, where the server holds all active sessions.
	 *
	 * @author Luka Čupić
	 */
	private static class SessionMapEntry {

		/**
		 * The session identifier.
		 */
		String SID;

		/**
		 * Holds the time (in seconds) until this session will be valid.
		 */
		long validUntil;

		/**
		 * A map for storing the client's data.
		 */
		Map<String, String> map;

		/**
		 * Creates a new session entry.
		 *
		 * @param sessionTimeout the maximum duration of client sessions,
		 *                       in seconds.
		 */
		public SessionMapEntry(int sessionTimeout) {
			SID = generateRandomSID();
			validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
			map = new ConcurrentHashMap<>();
		}

		/**
		 * Generates a unique session identifier consisting of 20
		 * uppercase letters of the english alphabet.
		 *
		 * @return a unique session identifier consisting of 20
		 * uppercase letters of the english alphabet
		 */
		private String generateRandomSID() {
			String sid = "";
			for (int i = 0; i < 20; i++) {
				sid += (char) (Math.abs(sessionRandom.nextInt()) % 26 + 65);
			}
			return sid;
		}
	}

	/**
	 * The main method.
	 *
	 * @param args a single argument: path to the "server.properties" file
	 */
	public static void main(String[] args) {

		try {
			SmartHttpServer server = new SmartHttpServer("./config/server.properties");
			server.start();
		} catch (Exception e) {
			System.out.println("Fatal Server Error: Shutting down!");
		}
	}

	/**
	 * This class represents a worker which servers the clients
	 * connected to the server. Each worker is added to a server's
	 * thread pool which assigns new jobs (i.e. new clients) to each
	 * of the workers.
	 *
	 * @author Luka Čupić
	 */
	public class ClientWorker implements Runnable, IDispatcher {

		/**
		 * Represents the default mime-type for the client response.
		 * If the mime-type is not specified, a response to the client
		 * will simply be a stream of bytes.
		 */
		private static final String DEFAULT_MIMETYPE = "application/octet-stream";

		/**
		 * Represents a package for the server workers.
		 */
		private static final String WORKERS_FQCN = "hr.fer.zemris.java.webserver.workers.";

		/**
		 * The client socket, used for communication with the client.
		 */
		private Socket csocket;

		/**
		 * The input stream used to receiving the data from the client.
		 */
		private PushbackInputStream istream;

		/**
		 * The output stream used to sending the data to the client.
		 */
		private OutputStream ostream;

		/**
		 * The HTML version used by this client worker.
		 */
		private String version;

		/**
		 * The method used by this client worker.
		 */
		private String method;

		/**
		 * A map holding the parameters.
		 */
		private Map<String, String> params = new HashMap<>();

		/**
		 * A map holding the permanent parameters.
		 */
		private Map<String, String> permParams = new HashMap<>();

		/**
		 * A map holding the temporary parameters.
		 */
		private Map<String, String> tempParams = new HashMap<>();

		/**
		 * Represents the cookies of this client worker's context.
		 */
		private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();

		/**
		 * Represents the context used for client response.
		 */
		private RequestContext context = null;

		/**
		 * Represents the ID of the current client session.
		 */
		private String SID;

		/**
		 * Creates a new client worker.
		 *
		 * @param csocket the socket used for communication with the client.
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}

		@Override
		public void run() {
			System.out.println("Processing request...");

			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();

				performRequest();
			} catch (Exception ex) {
				throw new RuntimeException("Error while performing the request!");
			}

			try {
				istream.close();
				ostream.flush();
				ostream.close();
				csocket.close();
			} catch (Exception ignorable) {
			}

			System.out.println("Completed!");
		}

		/**
		 * Performs the request that was requested by the client.
		 *
		 * @throws Exception if an error occurs while performing
		 *                   the request
		 */
		private void performRequest() throws Exception {
			List<String> request = readRequest();

			checkSession(request);

			String requestedPath = parseHeader(request);

			String[] pathParts = requestedPath.split("[?]");
			Path path = Paths.get(pathParts[0]);

			if (pathParts.length == 2) {
				String paramString = pathParts[1];
				parseParameters(paramString);
			}

			internalDispatchRequest(path.toString(), true);
		}

		/**
		 * Checks the validity of the current session. The session is
		 * considered invalid if the received SID is not recognizable
		 * as an existing SID or if the session has expired. In these
		 * cases, a new session will be created for the client.
		 *
		 * @param request the header lines received from the client
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = getSIDCandidate(request);

			SessionMapEntry session = null;

			boolean createNewSession = true;
			if (sidCandidate != null) {
				session = sessions.get(sidCandidate);

				if (session != null) {
					if (isExpired(session)) {
						sessions.remove(sidCandidate);
					} else {
						createNewSession = false;
					}
				}
			}

			if (createNewSession) {
				session = createNewSession(request);
			}

			session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
			permParams = session.map;
		}

		/**
		 * Creates a new session for the current client.
		 *
		 * @param request the header lines received from the client
		 * @return a new {@link SessionMapEntry} object, representing
		 * the currently active session
		 */
		private SessionMapEntry createNewSession(List<String> request) {
			SessionMapEntry session = new SessionMapEntry(sessionTimeout);
			sessions.put(session.SID, session);

			String domain = SmartHttpServer.this.address;
			for (String p : request) {
				if (p.startsWith("Host:")) {
					domain = p.split(":")[1].trim();
				}
			}
			outputCookies.add(new RequestContext.RCCookie(
				"sid", session.SID, null,
				domain, "/", true)
			);
			return session;
		}

		/**
		 * Gets the SID candidate, extracted from the header received
		 * by the client.
		 *
		 * @param request the header lines received from the client
		 * @return the SID candidate, extracted from the header received
		 * by the client, or null if no SID candidate was found
		 */
		private String getSIDCandidate(List<String> request) {
			String sidCandidate = null;
			for (String p : request) {
				if (!p.startsWith("Cookie:")) continue;
				String[] cookies = p.substring(7).trim().split(";");

				for (String cookie : cookies) {
					cookie = cookie.trim();
					String[] parts = cookie.split("=");
					if (!"sid".equals(parts[0])) continue;
					sidCandidate = parts[1].replace("\"", "");
				}
			}
			return sidCandidate;
		}

		/**
		 * Sends the response for the determined request back to
		 * the client.
		 *
		 * @throws IOException if an error occurs while processing
		 *                     the client's request
		 */

		private void sendResponse(Path path) throws IOException {
			byte[] contents = Files.readAllBytes(path);

			String extension = getExtension(path);

			String mimeType = mimeTypes.get(extension);
			if (mimeType == null) mimeType = DEFAULT_MIMETYPE;
			context.setStatusCode(200);

			if ("smscr".equals(extension)) {
				String script = new String(contents, StandardCharsets.UTF_8);
				context.setMimeType("text/plain");
				executeScript(script, context);
			} else {
				context.setMimeType(mimeType);
				context.write(contents);
			}
		}

		/**
		 * Parses the given list of strings, representing the the lines of
		 * the header.
		 *
		 * @param request the header lines received from the client
		 * @return a string, representing the path extracted from the URL
		 * client request
		 * @throws IOException if the received header is invalid
		 */
		private String parseHeader(List<String> request) throws IOException {
			if (request == null || request.size() < 1) {
				sendError(400, "Bad request.");
				throw new IOException();
			}

			String[] firstLine = request.isEmpty() ? null : request.get(0).split(" ");
			if (firstLine == null || firstLine.length != 3) {
				sendError(400, "Bad request.");
				throw new IOException();
			}

			String method = firstLine[0].toUpperCase();
			if (!method.equals("GET")) {
				sendError(400, "Method Not Allowed.");
				throw new IOException();
			}
			this.method = method;

			String version = firstLine[2].toUpperCase();
			if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
				sendError(400, "HTTP Version Not Supported.");
				throw new IOException();
			}
			this.version = version;

			return firstLine[1].substring(1);
		}

		/**
		 * Executes the specified script using the {@link SmartScriptEngine}.
		 *
		 * @param script the script to execute
		 * @param rc     the request context to use for response to the client
		 */
		private void executeScript(String script, RequestContext rc) {
			SmartScriptParser parser = new SmartScriptParser(script);
			DocumentNode node = parser.getDocumentNode();

			SmartScriptEngine engine = new SmartScriptEngine(node, rc);
			engine.execute();
		}

		/**
		 * Extracts the extension from a file, specified by the
		 * given path.
		 *
		 * @param requestedPath the path representing the file
		 * @return the extension of the file specified by the
		 * given path
		 */
		private String getExtension(Path requestedPath) {
			String file = requestedPath.toString();
			return file.substring(file.lastIndexOf(".") + 1);
		}

		/**
		 * Extracts the parameters contained in the given string
		 * and stores them int the {@link #params} map, by mapping
		 * the parameter's name to it's value.
		 *
		 * @param paramString the string to extract the parameters from
		 */
		private void parseParameters(String paramString) {
			String[] pairs = paramString.split("[&]");

			for (String pair : pairs) {
				String[] parts = pair.split("[=]");
				if (parts.length != 2) continue;
				params.put(parts[0], parts[1]);
			}
		}

		/**
		 * Reads the request received from the client and returns
		 * the request as a list of strings.
		 *
		 * @return a list, representing the client's request
		 * @throws IOException if an error occurs while reading
		 *                     the client request
		 */
		private List<String> readRequest() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			int state = 0;
			while (true) {
				int b = istream.read();
				if (b == -1) return null;
				if (b != 13) bos.write(b);

				if (state == 0) {
					if (b == 13) state = 1;
					else if (b == 10) state = 4;

				} else if (state == 1) {
					if (b == 10) state = 2;
					else state = 0;

				} else if (state == 2) {
					if (b == 13) state = 3;
					else state = 0;

				} else if (state == 3) {
					if (b == 10) break;
					else state = 0;

				} else if (state == 4) {
					if (b == 10) break;
					else state = 0;
				}
			}
			bos.flush();

			String charset = StandardCharsets.US_ASCII.name();
			String header = bos.toString(charset);

			bos.close();

			return Arrays.asList(header.split("\\r?\\n"));
		}

		/**
		 * Sends the error back to the client, signalizing that something
		 * went wrong with the client-server communication.
		 *
		 * @param statusCode the status code of the error message
		 * @param statusText the status text of the error message
		 * @throws IOException if an error occurs while sending the message
		 */
		private void sendError(int statusCode, String statusText) throws IOException {
			StringBuilder sb = new StringBuilder();

			sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
			sb.append("Server: simple java server\r\n");
			sb.append("Content-Type: text/plain;charset=UTF-8\r\n");
			sb.append("Connection: close\r\n");

			ostream.write(sb.toString().getBytes(StandardCharsets.US_ASCII));
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * This method is used for dispatching the client's request.
		 * The specified path is analyzed and a response is generated
		 * depending on the structure on the given path. Also, the given
		 * flag (directCall) tells whether the request was dispatched
		 * internally (from inside the server structure) or externally
		 * (by the client). The latter might have some restrictions while
		 * accessing certain directories (for example, a directory named
		 * "private" will not be accessible through an outside call.
		 *
		 * @param urlPath    the path which specifies the requested file
		 *                   or folder
		 * @param directCall a flag which denotes whether or not the request
		 *                   was made by the client, or internally, by a certain
		 *                   server component
		 * @throws Exception if an error occurs while performing the request
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			Path path = Paths.get(urlPath);

            /*
			if (path.startsWith("private/") && directCall) {
                sendError(404, "Forbidden.");
                return;
            }
            */

			if (context == null) {
				context = new RequestContext(ostream, params, permParams,
					outputCookies, tempParams, this
				);
			}

			IWebWorker worker = workersMap.get(path.toString());
			if (worker == null) {
				if (path.startsWith("ext/")) {
					String fqcn = WORKERS_FQCN + path.toString().substring(4);

					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
					Object newObject = referenceToClass.newInstance();
					worker = (IWebWorker) newObject;
				}
			}
			if (worker != null) {
				worker.processRequest(context);
				return;
			}

			path = documentRoot.resolve(path.toString()).toAbsolutePath();
			if (!path.startsWith(documentRoot)) {
				sendError(404, "Forbidden.");
				return;
			}

			if (!Files.exists(path) ||
				!Files.isRegularFile(path) ||
				!Files.isReadable(path)) {
				sendError(404, "Not found.");
				return;
			}
			sendResponse(path);
		}
	}
}
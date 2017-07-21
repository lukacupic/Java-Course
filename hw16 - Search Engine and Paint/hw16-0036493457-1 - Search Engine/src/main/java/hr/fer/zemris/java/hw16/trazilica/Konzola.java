package hr.fer.zemris.java.hw16.trazilica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represent a CMD document search engine. It uses
 * the TF-IDF vector representation of documents, which are then
 * used for matching the user's input and finding the best results.
 * The program expects a single argument: the path to the folder
 * containing the documents which will be searched (and by which the
 * vocabulary will be created).
 *
 * @author Luka Čupić
 */
public class Konzola {

	/**
	 * The path to the file containing the stop words.
	 */
	private static final String STOP_WORDS_PATH = "src/main/resources/hrvatski_stoprijeci.txt";

	/**
	 * The current vocabulary containing all words from all documents.
	 */
	private static Set<String> vocabulary = new TreeSet<>();

	/**
	 * A set of stop words. A "stop word" is defined as a word irrelevant to
	 * the searching algorithm.
	 */
	private static Set<String> stopWords = new TreeSet<>();

	/**
	 * Holds the number of occurrences in the documents for each word from
	 * the vocabulary.
	 */
	private static Map<String, Integer> wordFrequency = new LinkedHashMap<>();

	/**
	 * A map of all the documents, mapped to by it's appropriate file system
	 * paths.
	 */
	private static Map<Path, Document> documents = new LinkedHashMap<>();

	/**
	 * A helper IDF vector which holds the IDF components for each of the
	 * words from the vocabulary.
	 */
	private static Vector idf;

	/**
	 * Holds a list of results which were created by the last "query" command.
	 */
	private static List<Result> results;

	/**
	 * A flag which tells whether the results have previously been generated
	 * or not.
	 */
	private static boolean hasResults = false;

	/**
	 * The main method.
	 *
	 * @param args a single argument - representing the path to the folder of
	 *             with documents
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("A single argument is expected - path to the " +
				"collection of documents");
			return;
		}

		Path path = Paths.get(args[0]);

		try {
			System.out.println("Initializing...");
			init(path);
			System.out.printf("Success!%n%n");
		} catch (IOException e) {
			System.out.println("Initialization error!");
			System.exit(0);
		}

		Scanner sc = new Scanner(System.in);

		System.out.print("Enter command > ");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();

			try {
				parseInput(line);
			} catch (Exception ex) {
				System.out.println("Illegal command!");
			}
			System.out.printf("%nEnter command > ");
		}
	}

	/**
	 * Parses the given input and performs an appropriate
	 * command or throws an exception if an illegal (or
	 * unknown) command is given.
	 *
	 * @param input the user's input.
	 */
	private static void parseInput(String input) throws Exception {
		if (input.startsWith("query")) {
			processQuery(input.substring(5));

		} else if (input.startsWith("type")) {
			processType(input.substring(4));

		} else if (input.startsWith("results")) {
			processResults();

		} else if (input.startsWith("exit")) {
			System.exit(0);

		} else {
			System.out.println("Unknown command!");
		}
	}

	/**
	 * Initializes the program.
	 *
	 * @param path the path to the folder containing the documents
	 * @throws IOException if an error occurs while initialization
	 */
	private static void init(Path path) throws IOException {
		readStopWords();
		createVocabulary(path);
		initDocuments(path);
	}

	/**
	 * Reads the file containing the stop words and creates a set
	 * holding them.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	private static void readStopWords() throws IOException {
		Path path = Paths.get(STOP_WORDS_PATH);

		List<String> words = Files.readAllLines(path);
		for (String word : words) {
			stopWords.add(word.toLowerCase());
		}
	}

	/**
	 * Creates the vocabulary by recursively reading all documents from the
	 * given path. Words specified in the set {@code {@link #stopWords}} are
	 * ignored, as they are not important for the further steps in the algorithm.
	 * All character that are not words will simply be ignored. So for example, a
	 * part of the document "hitch42iker" shall be interpreted as "hitch iker".
	 *
	 * @param path the path to the folder containing the documents
	 * @throws IOException if an error occurs while creating the vocabulary
	 */
	private static void createVocabulary(Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
				for (String word : readDocument(path)) {
					if (word.isEmpty() || stopWords.contains(word)) continue;
					vocabulary.add(word);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Creates the TF vectors for all read documents (which are thenadded to
	 * the {@link #documents } map) and fills {@link #wordFrequency} with
	 * values obtained from the documents.
	 *
	 * @param path the path to the folder containing the documents
	 * @throws IOException if an error occurs while creating the documents
	 */
	private static void initDocuments(Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
				List<String> vocabList = new ArrayList<>(vocabulary);

				List<String> words = readDocument(path);
				words.removeAll(stopWords);

				// create the TF vector component
				double[] values = new double[vocabulary.size()];
				for (String word : words) {
					int wordIndex = vocabList.indexOf(word);
					values[wordIndex]++;
				}

				// update the map, mapping each word to the number of documents
				// containing it
				for (String word : new LinkedHashSet<>(words)) {
					wordFrequency.put(word, wordFrequency.containsKey(word) ? wordFrequency.get(word) + 1 : 1);
				}

				Document doc = new Document(path, new Vector(values), null);
				documents.put(path, doc);

				return FileVisitResult.CONTINUE;
			}
		});
		createIDFVector();
	}

	/**
	 * Creates the "main" IDF vector which represents the number of occurrences in
	 * the documents for each of the words from the vocabulary.
	 */
	private static void createIDFVector() {
		double[] values = new double[vocabulary.size()];

		List<String> vocabList = new ArrayList<>(vocabulary);
		for (String word : vocabList) {
			int wordCount = wordFrequency.get(word);
			values[vocabList.indexOf(word)] = (double) vocabulary.size() / wordCount;
		}
		idf = new Vector(values);

		for (Document d : documents.values()) {
			d.setVector(Vector.multiply(d.getTFVector(), idf));
		}
	}

	/**
	 * Reads a document specified by the given path and returns a list of words contained
	 * in the document. All non-letter characters will be ignored in the end result. For
	 * example, a part of the document "hitch42iker" shall be interpreted as "hitch iker".
	 *
	 * @param path the path to the document
	 * @return a list of words representing the contents of the document
	 * @throws IOException if an error occurs while reading the document
	 */
	private static List<String> readDocument(Path path) throws IOException {
		String document = new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
			.replaceAll("[^A-Za-zČĆĐŠŽčćđšž]", " ").trim()
			.replaceAll(" +", " ").toLowerCase();
		return new ArrayList<>(Arrays.asList(document.split(" ")));
	}

	/**
	 * Processes the user's query input, read from the given scanner object, and
	 * displays the results (onto the standard output).
	 */
	private static void processQuery(String input) {
		List<String> words = Arrays.asList(input.trim().split(" +"));
		words.retainAll(vocabulary);

		List<String> vocabList = new ArrayList<>(vocabulary);

		double[] values = new double[vocabulary.size()];
		for (String word : words) {
			int wordIndex = vocabList.indexOf(word);
			values[wordIndex]++;
		}

		Document inputDoc = new Document(null, null, Vector.multiply(new Vector(values), idf));

		results = getResults(inputDoc);

		System.out.println("Here are the 10 best results:");
		printResults();
		hasResults = true;
	}

	/**
	 * Compares the given document object to all other documents in the
	 * collection, compares them, and returns the list of Result objects,
	 * encapsulating the similarity coefficients representing the similarity
	 * in respect to the provided document.
	 *
	 * @param doc the document
	 * @return a list of top 10 search results (the top 10 result with the
	 * highest similarity coefficients)
	 */
	private static List<Result> getResults(Document doc) {
		List<Result> results = new ArrayList<>();
		for (Document d : documents.values()) {
			results.add(new Result(doc.similarTo(d), d));
		}
		results.sort(Comparator.reverseOrder());
		return results.subList(0, 9);
	}

	private static void processType(String input) throws IOException {
		int index = Integer.parseInt(input.trim());

		Document doc = results.get(index).getDocument();

		System.out.println(doc.getPath());
		System.out.println("--------------------------------------");
		Files.readAllLines(doc.getPath()).forEach(System.out::println);
	}

	/**
	 * Prints the currently stored results onto the standard output.
	 */
	private static void printResults() {
		for (int i = 0; i < results.size(); i++) {
			Result r = results.get(i);
			if (r.getSim() < 10E-8) break;
			System.out.printf("[%d] (%f) %s%n", i, r.getSim(), r.getDocument().getPath());
		}
	}

	/**
	 * Writes out the results which were previously generated by the "query"
	 * command, or an appropriate message if no previous results exist.
	 */
	private static void processResults() {
		if (!hasResults) {
			System.out.println("No results available.");
			return;
		}
		printResults();
	}
}

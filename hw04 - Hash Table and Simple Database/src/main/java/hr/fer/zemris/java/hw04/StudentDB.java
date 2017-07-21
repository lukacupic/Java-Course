package hr.fer.zemris.java.hw04;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw04.db.QueryFilter;
import hr.fer.zemris.java.hw04.db.QueryParser;
import hr.fer.zemris.java.hw04.db.QueryParserException;
import hr.fer.zemris.java.hw04.db.StudentDatabase;
import hr.fer.zemris.java.hw04.db.StudentRecord;

/**
 * A simple program used for performing a query command upon a
 * {@link StudentDatabase} database.
 *
 * @author Luka Čupić
 */
public class StudentDB {

    /**
     * The main method of the class.
     *
     * @param args command line arguments; not used in this program.
     */
    public static void main(String[] args) {
        // read the database file
        List<String> lines = null;
        try {
            lines = Files.readAllLines(
                Paths.get("src/main/java/hr/fer/zemris/java/hw04/database.txt"),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.err.println("Error reading the database file!");
            System.exit(1);
        }

        StudentDatabase db = new StudentDatabase(lines);

        Scanner sc = new Scanner(System.in);

        System.out.printf("> ");
        while (sc.hasNextLine()) {

            String line = sc.nextLine();

            if (line.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (!line.startsWith("query")) {
                System.out.printf("Unknown command!%n%n");
                System.out.printf("> ");
                continue;
            }

            // skip the "query" keyword
            line = line.substring("query".length());

            QueryParser parser;
            try {
                parser = new QueryParser(line);
            } catch (QueryParserException ex) {
                System.out.printf("Illegal query input!%n%n");
                System.out.printf("> ");
                continue;
            }

            List<StudentRecord> recordList = new ArrayList<>();

            if (parser.isDirectQuery()) {
                StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());

                // no records found
                if (r == null) {
                    System.out.printf("Records selected: 0%n%n");
                    System.out.printf("> ");
                    continue;
                }

                System.out.println("Using index for record retrieval.");
                recordList.add(r);
            } else {
                try {
                    recordList = db.filter(new QueryFilter(parser.getQuery()));
                } catch (Exception ex) {
                    System.out.printf("Illegal query input!%n%n");
                    System.out.printf("> ");
                    continue;
                }

                // no records found
                if (recordList.size() == 0) {
                    System.out.printf("Records selected: 0%n%n");
                    System.out.printf("> ");
                    continue;
                }
            }

            System.out.println(formatOutput(recordList));
            System.out.printf("Records selected: %d%n%n", recordList.size());
            System.out.printf("> ");
        }
    }

    /**
     * Formats the passed list of {@link StudentRecord} objects in a
     * compact table-like structure and returns it as a string.
     *
     * @param records a list of strings to represent in the table.
     * @return a string representation of the student records.
     */
    private static String formatOutput(List<StudentRecord> records) {
        int maxJmbagLength = 0;
        int maxLastNameLength = 0;
        int maxFirstNameLength = 0;
        int maxGradeLength = 0;

        // iterate through the list and find the max length for each attribute
        for (StudentRecord r : records) {
            maxJmbagLength = Math.max(r.getJmbag().length(), maxJmbagLength);
            maxLastNameLength = Math.max(r.getLastName().length(), maxLastNameLength);
            maxFirstNameLength = Math.max(r.getFirstName().length(), maxFirstNameLength);
            maxGradeLength = Math.max(String.valueOf(r.getFinalGrade()).length(), maxGradeLength);
        }

        StringBuilder builder = new StringBuilder();

        String border =
            "+" + getChars(maxJmbagLength + 2, '=') +
                "+" + getChars(maxLastNameLength + 2, '=') +
                "+" + getChars(maxFirstNameLength + 2, '=') +
                "+" + getChars(maxGradeLength + 2, '=') +
                "+";

        builder.append(border + "\n");

        for (StudentRecord r : records) {
            // create new strings filled with spaces
            String jmbagSpaces = getChars(maxJmbagLength - r.getJmbag().length(), ' ');
            String lastNameSpaces = getChars(maxLastNameLength - r.getLastName().length(), ' ');
            String firstNameSpaces = getChars(maxFirstNameLength - r.getFirstName().length(), ' ');
            String gradeSpaces = getChars(maxGradeLength - String.valueOf(r.getFinalGrade()).length(), ' ');

            String str = String.format(
                "| %s%s | %s%s | %s%s | %s%s |\n",
                r.getJmbag(),
                jmbagSpaces,
                r.getLastName(),
                lastNameSpaces,
                r.getFirstName(),
                firstNameSpaces,
                r.getFinalGrade(),
                gradeSpaces
            );
            builder.append(str);
        }
        builder.append(border);

        return builder.toString();
    }

    /**
     * Returns a string filled with {@param length} {@param ch} characters.
     *
     * @param length the number of characters to be stored in the string.
     * @param ch     the character to be stored in the string.
     * @return a string with {@param length} spaces.
     */
    private static String getChars(int length, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(ch);
        }

        return builder.toString();
    }
}
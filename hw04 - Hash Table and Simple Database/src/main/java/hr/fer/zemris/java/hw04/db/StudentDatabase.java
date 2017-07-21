package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.collections.SimpleHashTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a simple database of {@link StudentRecord} objects.
 * <p>
 * The class methods can be called upon to retrieve a list of
 * {@link StudentRecord} entries. Individual record can also be
 * retrieved by providing their JMBAG values, which serve as a key.
 *
 * @author Luka Čupić
 */
public class StudentDatabase {

    /**
     * Represents the records of this student database.
     */
    private List<StudentRecord> studentList;

    /**
     * Represents a map of student records, with the {@link StudentRecord#jmbag}
     * attribute as the key, and the {@link StudentRecord} object as the value.
     */
    private SimpleHashTable<String, StudentRecord> studentMap;

    /**
     * Creates a new database from the passed list of strings, which are
     * a textual representation of the student records.
     *
     * @param stringList a list of strings representing the student records.
     */
    public StudentDatabase(List<String> stringList) {
        studentList = new ArrayList<>();
        studentMap = new SimpleHashTable<>();

        for (String string : stringList) {
            String[] parts = string.split("\t");

            if (parts.length != 4) {
                throw new IllegalArgumentException("Passed string is illegal!");
            }

            int grade;
            try {
                grade = Integer.parseInt(parts[3]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }

            StudentRecord record = new StudentRecord(parts[0], parts[1], parts[2], grade);

            studentList.add(record);
            studentMap.put(parts[0], record);
        }
    }

    /**
     * Returns a {@link StudentRecord} object from the database, or null if
     * such an entry doesn't exist.
     *
     * @param jmbag the ID of the student to return.
     * @return a student record from the database.
     */
    public StudentRecord forJMBAG(String jmbag) {
        if (!studentMap.containsKey(jmbag)) return null;

        return studentMap.get(jmbag);
    }

    /**
     * Iterates through all student records in this database and returns
     * a new list of those who satisfy the condition placed by the passed
     * {@link IFilter} object.
     *
     * @param filter the filter object which will be used for applying a
     *               filter to the database objects.
     * @return a list of filtered {@link StudentRecord} objects.
     */
    public List<StudentRecord> filter(IFilter filter) {
        List<StudentRecord> filteredList = new LinkedList<>();

        for (StudentRecord record : studentList) {
            if (filter.accepts(record)) {
                filteredList.add(record);
            }
        }
        return filteredList;
    }

}

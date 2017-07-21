package hr.fer.zemris.java.hw04.db;

/**
 * Represents a record for a student's final grade.
 * <p>
 * The attributes which are stored in this object are:
 * - JMBAG (representing the student's ID)
 * - LAST NAME (representing the last name of the student)
 * - FIRST NAME (representing the first name of the student
 *
 * @author Luka Čupić
 */
public class StudentRecord {

    /**
     * Student's ID.
     */
    private String jmbag;

    /**
     * Student's last name.
     */
    private String lastName;

    /**
     * Student's first name.
     */
    private String firstName;

    /**
     * Student's final grade.
     */
    private int finalGrade;

    /**
     * Creates a new student record with the passed arguments.
     *
     * @param jmbag      student's ID.
     * @param lastName   student's last name.
     * @param firstName  student's first name.w
     * @param finalGrade student's final grade.
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        if (jmbag == null || lastName == null || firstName == null || finalGrade < 0) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentRecord that = (StudentRecord) o;

        return jmbag.equals(that.jmbag);
    }

    @Override
    public int hashCode() {
        return jmbag.hashCode();
    }

    /**
     * Returns the student's ID.
     *
     * @return the student's ID.
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Returns the student's last name.
     *
     * @return the student's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the student's first name.
     *
     * @return the student's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the student's final grade.
     *
     * @return the student's final grade.
     */
    public int getFinalGrade() {
        return finalGrade;
    }
}

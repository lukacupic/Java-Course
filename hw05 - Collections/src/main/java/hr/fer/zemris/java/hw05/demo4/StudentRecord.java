package hr.fer.zemris.java.hw05.demo4;

/**
 * Represents a record for a student.
 * <p>
 * The attributes stored in an instance of this class are:
 * - {@link StudentRecord#jmbag} - representing the student's ID
 * - {@link StudentRecord#lastName} - representing the student's last name
 * - {@link StudentRecord#firstName} - representing the student's first name
 * - {@link StudentRecord#midtermPoints} - representing the student's midterm exam points
 * - {@link StudentRecord#finalPoints} - representing the student's final exam points
 * - {@link StudentRecord#labPoints} - representing the student's laboratory points
 * - {@link StudentRecord#labPoints} - representing the student's final grade
 *
 * @author Luka Čupić
 */
public class StudentRecord {

	/**
	 * ID of the student.
	 */
	private String jmbag;

	/**
	 * First name of the student.
	 */
	private String lastName;

	/**
	 * Last name of the student.
	 */
	private String firstName;

	/**
	 * Points the student earned on the midterm exam.
	 */
	private double midtermPoints;

	/**
	 * Points the student earned on the final exam.
	 */
	private double finalPoints;

	/**
	 * Points the student earned on the laboratory exercises.
	 */
	private double labPoints;

	/**
	 * The final grade of the student.
	 */
	private int grade;


	/**
	 * Creates a new {@link StudentRecord} object.
	 *
	 * @param jmbag         ID of the student.
	 * @param lastName      first name of the student.
	 * @param firstName     last name of the student.
	 * @param midtermPoints points the student earned on the midterm exam.
	 * @param finalPoints   points the student earned on the final exam.
	 * @param labPoints     points the student earned on the laboratory exercises.
	 * @param grade         the final grade of the student.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, double midtermPoints,
						 double finalPoints, double labPoints, int grade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midtermPoints = midtermPoints;
		this.finalPoints = finalPoints;
		this.labPoints = labPoints;
		this.grade = grade;
	}

	/**
	 * Gets the ID of the student.
	 *
	 * @return the ID of the student.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Gets the last name of the student.
	 *
	 * @return the last name of the student.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the ID first name the student.
	 *
	 * @return the first name of the student.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the midterm exam points of the student.
	 *
	 * @return the midterm exam points of the student.
	 */
	public double getMidtermPoints() {
		return midtermPoints;
	}

	/**
	 * Gets the final exam points of the student.
	 *
	 * @return the final exam points of the student.
	 */
	public double getFinalPoints() {
		return finalPoints;
	}

	/**
	 * Gets the laboratory exercises' points of the student.
	 *
	 * @return the laboratory exercises' points of the student.
	 */
	public double getLabPoints() {
		return labPoints;
	}

	/**
	 * Gets the final grade of the student.
	 *
	 * @return the final grade of the student.
	 */
	public int getGrade() {
		return grade;
	}

	@Override
	public String toString() {
		return String.format(
			"%s %s %s %f %d",
			jmbag,
			lastName,
			firstName,
			getTotalPoints(),
			grade
		);
	}

	/**
	 * Returns the total number of points of this {@link StudentRecord} object.
	 * This includes {@link StudentRecord#midtermPoints}, {@link StudentRecord#finalPoints}
	 * and {@link StudentRecord#labPoints}.
	 *
	 * @return total number of points.
	 */
	public double getTotalPoints() {
		return midtermPoints + finalPoints + labPoints;
	}
}

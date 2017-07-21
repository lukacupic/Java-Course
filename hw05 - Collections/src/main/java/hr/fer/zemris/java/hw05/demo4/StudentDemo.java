package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A demonstration program which performs certain filtering operations
 * upon a list of {@link StudentRecord} objects.
 *
 * @author Luka Čupić
 */
public class StudentDemo {

	/**
	 * The main method.
	 *
	 * @param args command line arguments; not used in this program.
	 */
	public static void main(String[] args) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(
				Paths.get("src/main/resources/studenti.txt"),
				StandardCharsets.UTF_8
			);
		} catch (IOException e) {
			System.out.println("Unable to read the file!");
			System.exit(1);
		}

		List<StudentRecord> records = null;
		try {
			records = convert(lines);
		} catch (Exception ex) {
			System.out.println("Error reading student records!");
			System.exit(1);
		}

		long broj = vratiBodovaViseOd25(records);

		long broj5 = vratiBrojOdlikasa(records);

		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);

		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);

		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);

		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);

		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(records);

		Map<Boolean, List<StudentRecord>> prolazNeprolaz = razvrstajProlazPad(records);

		System.out.println();
	}

	/**
	 * Converts a list of strings into a list of {@link StudentRecord} objects.
	 *
	 * @param lines the lines to convert.
	 * @return a list of {@link StudentRecord} objects.
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> records = new ArrayList<>();

		for (String line : lines) {
			String[] parts = line.split("\t");

			StudentRecord record = new StudentRecord(
				parts[0],
				parts[1],
				parts[2],
				Double.parseDouble(parts[3]),
				Double.parseDouble(parts[4]),
				Double.parseDouble(parts[5]),
				Integer.parseInt(parts[6])
			);
			records.add(record);
		}
		return records;
	}

	/**
	 * Returns the number of {@link StudentRecord} objects whose total number
	 * of points is larger than 25.
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return the number of {@link StudentRecord} objects whose total number
	 * of points is larger than 25.
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
			.filter(s -> s.getTotalPoints() > 25)
			.count();
	}

	/**
	 * Returns the number of straight-A students in the given list of {@link StudentRecord}
	 * objects.
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return the number of straight-A students in the given list of {@link StudentRecord}
	 * objects.
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
			.filter(s -> s.getGrade() == 5)
			.count();
	}

	/**
	 * Returns a list of straight-A students from the given list of {@link StudentRecord}
	 * objects.
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return a list of straight-A students from the given list of {@link StudentRecord}
	 * objects.
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
			.filter(s -> s.getGrade() == 5)
			.collect(Collectors.toList());
	}

	/**
	 * Returns a sorted list of straight-A students from the given list of {@link StudentRecord}
	 * objects.
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return a sorted list of straight-A students from the given list of {@link StudentRecord}
	 * objects.
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return vratiListuOdlikasa(records).stream()
			.sorted((s1, s2) -> {
				Double o1TotalPoints = s1.getTotalPoints();
				Double o2TotalPoints = s2.getTotalPoints();

				return -o1TotalPoints.compareTo(o2TotalPoints);
			})
			.collect(Collectors.toList());
	}

	/**
	 * Returns a list of all the {@link StudentRecord} objects who haven't passed
	 * the class (i.e. whose grade is 1).
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return a list of all the {@link StudentRecord} objects who haven't passed
	 * the class (i.e. whose grade is 1).
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
			.filter(s -> s.getGrade() == 1)
			.map(StudentRecord::getJmbag)
			.collect(Collectors.toList());
	}

	/**
	 * Returns a map which maps each grade to a list of {@link StudentRecord} objects whose
	 * grade matches the key.
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return a map which maps grades to lists of students who have this grade.
	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream()
			.collect(Collectors.groupingBy(StudentRecord::getGrade));
	}

	/**
	 * Returns a map which maps each grade to the number of {@link StudentRecord}
	 * objects whose grade matches the key.
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return a map which maps grades to the number of students who have this grade.
	 */
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream()
			.collect(Collectors.toMap(StudentRecord::getGrade, s -> 1, (i1, i2) -> i1 + i2)
			);
	}

	/**
	 * Returns a map which maps a boolean value to a list of students, depending
	 * on whether the student passed the class or not (i.e. the key "true" will
	 * map to all the students who have passed the class and "false" will
	 * map to all those who haven't).
	 *
	 * @param records a list of {@link StudentRecord} objects.
	 * @return a map which maps a boolean value to a list of students, depending
	 * on whether the student passed the class or not.
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream()
			.collect(Collectors.partitioningBy(s -> s.getGrade() > 1));
	}
}

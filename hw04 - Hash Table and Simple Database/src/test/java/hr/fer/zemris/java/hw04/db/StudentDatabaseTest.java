package hr.fer.zemris.java.hw04.db;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StudentDatabaseTest {

    private static List<String> recordList;

    @BeforeClass
    public static void setUp() {
        try {
            recordList = Files.readAllLines(
                Paths.get("src/main/java/hr/fer/zemris/java/hw04/database.txt"),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.err.println("Testing error!");
        }
    }

    @Test
    public void testGettingARecordFromJMBAG() {
        StudentDatabase database = new StudentDatabase(recordList);

        List<StudentRecord> emptyList = database.filter(record -> false);
        List<StudentRecord> fullList = database.filter(record -> true);

        assertEquals(0, emptyList.size());
        assertEquals(recordList.size(), fullList.size());
    }
}
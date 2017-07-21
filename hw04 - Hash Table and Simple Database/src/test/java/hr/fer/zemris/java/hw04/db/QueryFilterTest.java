package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class QueryFilterTest {

    @Test
    public void testFilteringARecord() {
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

        ConditionalExpression expr = new ConditionalExpression(
            FieldValueGetters.JMBAG,
            "0000000044",
            ComparisonOperators.EQUALS
        );

        List<ConditionalExpression> query = new ArrayList<>();
        query.add(expr);

        StudentDatabase db = new StudentDatabase(lines);
        List<StudentRecord> recordList = db.filter(new QueryFilter(query));

        assertEquals(recordList.size(), 1);

        StudentRecord r = recordList.get(0);

        assertEquals(r.getJmbag(), "0000000044");
        assertEquals(r.getLastName(), "Pilat");
        assertEquals(r.getFirstName(), "Ivan");
        assertEquals(r.getFinalGrade(), 5);
    }

}
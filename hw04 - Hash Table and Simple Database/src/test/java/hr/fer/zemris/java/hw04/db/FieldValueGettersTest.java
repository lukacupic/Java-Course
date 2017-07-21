package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldValueGettersTest {

    @Test
    public void testGettingStudentRecordFields() {
        StudentRecord record = new StudentRecord(
            "0000000042",
            "Beeblebrox",
            "Zaphod",
            10
        );

        assertEquals("Zaphod", FieldValueGetters.FIRST_NAME.get(record));
        assertEquals("Beeblebrox", FieldValueGetters.LAST_NAME.get(record));
        assertEquals("0000000042", FieldValueGetters.JMBAG.get(record));
    }
}
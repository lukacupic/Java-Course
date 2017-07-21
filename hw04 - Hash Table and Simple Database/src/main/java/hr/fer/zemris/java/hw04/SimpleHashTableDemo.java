package hr.fer.zemris.java.hw04;

import hr.fer.zemris.java.hw04.collections.SimpleHashTable;

import java.util.Iterator;

public class SimpleHashTableDemo {

    public static void main(String[] args) {
        // create collection:
        SimpleHashTable<String, Integer> examMarks = new SimpleHashTable<>(2);

        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        // test output 1
        for (SimpleHashTable.TableEntry<String, Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }

        System.out.println();

        // test output 2
        for (SimpleHashTable.TableEntry<String, Integer> pair1 : examMarks) {
            for (SimpleHashTable.TableEntry<String, Integer> pair2 : examMarks) {
                System.out.printf(
                    "(%s => %d) - (%s => %d)%n",
                    pair1.getKey(), pair1.getValue(),
                    pair2.getKey(), pair2.getValue()
                );
            }
        }

        System.out.println();

        // test output 3
        Iterator<SimpleHashTable.TableEntry<String, Integer>> iter = examMarks.iterator();
        while (iter.hasNext()) {
            SimpleHashTable.TableEntry<String, Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        System.out.printf("Veličina: %d%n", examMarks.size());
    }
}


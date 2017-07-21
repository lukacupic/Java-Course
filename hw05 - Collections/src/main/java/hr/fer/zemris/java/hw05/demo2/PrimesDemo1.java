package hr.fer.zemris.java.hw05.demo2;

/**
 * A simple demonstration program of the {@link PrimesCollection} class
 * which demonstrates iterating through the collection.
 *
 * @author Luka Čupić
 */
public class PrimesDemo1 {

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program.
     */
    public static void main(String[] args) {

        PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them

        for (Integer prime : primesCollection) {
            System.out.println("Got prime: " + prime);
        }
    }
}

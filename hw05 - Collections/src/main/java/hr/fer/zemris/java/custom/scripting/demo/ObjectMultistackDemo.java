package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

/**
 * A demonstration program of the {@link ObjectMultistack} class.
 *
 * @author Luka Čupić
 */
public class ObjectMultistackDemo {

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program.
     */
    public static void main(String[] args) {
        ObjectMultistack multistack = new ObjectMultistack();

        ValueWrapper year = new ValueWrapper(2000);
        multistack.push("year", year);

        ValueWrapper price = new ValueWrapper(200.51);
        multistack.push("price", price);

        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());
        System.out.println("Current value for price: "
            + multistack.peek("price").getValue());

        multistack.push("year", new ValueWrapper(1900));
        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());

        multistack.peek("year").setValue(
            (Integer) multistack.peek("year").getValue() + 50
        );
        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());

        multistack.pop("year");
        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());

        multistack.peek("year").add("5");
        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());

        multistack.peek("year").add(5);
        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());

        multistack.peek("year").add(5.0);
        System.out.println("Current value for year: "
            + multistack.peek("year").getValue());
    }
}

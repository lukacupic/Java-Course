package hr.fer.zemris.java.gui.prim;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PrimListModelTest {

    @Test
    public void testGeneratingAPrimeNumberAndGettingThatNumber() {
        PrimListModel model = new PrimListModel();

        model.next();
        model.next();
        model.next();

        assertEquals(new Integer(5), model.getElementAt(2));
    }

    @Test
    public void testModelSize() {
        PrimListModel model = new PrimListModel();

        model.next();
        model.next();
        model.next();

        assertEquals(3, model.getSize());
    }
}
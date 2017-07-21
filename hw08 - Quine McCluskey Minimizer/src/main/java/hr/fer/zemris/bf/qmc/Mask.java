package hr.fer.zemris.bf.qmc;

import hr.fer.zemris.bf.utils.Util;

import java.util.*;

/**
 * This class represents a product (minterm) of a boolean function.
 * <p>
 * The mask object can be created by providing a truth-table index
 * of the minterm to represent, along with the number of variables
 * which the boolean function is consisted of - this will be the
 * length of the mask.
 * <p>
 * The mask can also be created by explicitly providing binary
 * values (0 and 1), representing the values of the minterm(s)
 * to represent, along with a set of minterm indexes. The reason
 * why multiple minterms can be represented, and why this is a
 * set of indexes instead of a single index, is because when the
 * mask values are provided, one can explicitly set a certain bit
 * (value) to be a "don't care" value, which is denoted by the
 * digit "2".
 *
 * @author Luka Čupić
 */
public class Mask {

    /**
     * Represents values of this mask.
     */
    private byte[] values;

    /**
     * The indexes which this mask represents in a boolean function.
     */
    private Set<Integer> indexes;

    /**
     * A flag which denotes whether the value of this product is a
     * "don't care".
     */
    private boolean dontCare;

    /**
     * Represents the hash code of {@link Mask#values}.
     */
    private int valuesHashCode;

    /**
     * A flag which denotes whether whether this mask was combined
     * with another mask to form a new mask.
     */
    private boolean combined;

    /**
     * Creates a new mask which represents a minterm of the given index.
     *
     * @param index             the index of the minterm to represent
     * @param numberOfVariables number of variables of the boolean function
     * @param dontCare          represents whether the minterm is a "don't care",
     *                          meaning that it's value is not important
     */
    public Mask(int index, int numberOfVariables, boolean dontCare) {
        if (index < 0 || numberOfVariables < 1 || index >= Math.pow(2, numberOfVariables)) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        this.values = Util.indexToByteArray(index, numberOfVariables);

        this.indexes = new TreeSet<>();
        this.indexes.add(index);

        this.dontCare = dontCare;

        initCommon();
    }

    /**
     * Creates a new mask from the given array, which represents a mask, and
     * a set of indexes, which represents the minterm indexes.
     *
     * @param values   represents a mask of the product
     * @param indexes  the corresponding indexes of the given mask
     * @param dontCare represents whether the minterm is a "don't care",
     *                 meaning that it's value is not important
     */
    public Mask(byte[] values, Set<Integer> indexes, boolean dontCare) {
        if (values == null || indexes == null || indexes.size() == 0) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        this.values = Arrays.copyOf(values, values.length);
        this.indexes = new TreeSet<>(indexes);
        this.dontCare = dontCare;

        initCommon();
    }

    /**
     * Performs initializations which are common for both constructors.
     */
    private void initCommon() {
        this.indexes = Collections.unmodifiableSet(this.indexes);

        valuesHashCode = Arrays.hashCode(this.values);
    }

    /**
     * Counts the number of bits in the mask whose value is equal to "1".
     *
     * @return the number of values "1" contained in this mask
     */
    public int countOfOnes() {
        int counter = 0;

        for (byte value : values) {
            if (value == 1) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gets the number of variables contained in this mask (i.e. the length
     * of the mask)
     *
     * @return the number of variables of this mask
     */
    public int size() {
        return values.length;
    }

    /**
     * Gets the value (bit) of the mask at the given position.
     *
     * @param position the position to get the value from
     * @return the value (bit) of the mask at the given position.
     */
    public byte getValueAt(int position) {
        if (position < 0 || position >= size()) {
            throw new IllegalArgumentException("Illegal mask index!");
        }
        return values[position];
    }

    /**
     * Combines this mask with another mask, but only if the combination can
     * be made, i.e. if both masks satisfy the condition of the minimization
     * theorem, which states that two products (in this case, masks) can be
     * combined if and only if they differ in at most one value.
     * The method returns an {@link Optional<Mask>} object representing the
     * new mask if the combination can be performed. Otherwise,
     * {@link Optional#empty()} is returned.
     *
     * @param other the mask to combine this mask with
     * @return a {@link Mask} object, wrapped inside the {@link Optional<Mask>}
     * class if the combination between masks can be made; {@link Optional#empty()}
     * otherwise
     */
    public Optional<Mask> combineWith(Mask other) {
        if (other == null || size() != other.size()) {
            throw new IllegalArgumentException("The given mask is invalid!");
        }

        int difIndex = -1;
        int difIndexCounter = 0;
        for (int i = 0; i < size(); i++) {
            byte first = getValueAt(i);
            byte second = other.getValueAt(i);

            if (first == 2 && second != 2 ||
                first != 2 && second == 2 ||
                difIndexCounter > 1) {
                return Optional.empty();
            }

            if (first != second) {
                difIndex = i;
                difIndexCounter++;
            }
        }

        boolean combDontCare = isDontCare() && other.isDontCare();

        if (difIndexCounter == 0) {
            return Optional.of(new Mask(values, indexes, combDontCare));
        }

        // difIndexCounter is now definitely equal to 1
        byte[] combValues = Arrays.copyOf(values, values.length);
        combValues[difIndex] = (byte) 2;

        Set<Integer> combIndexes = new TreeSet<>();
        combIndexes.addAll(getIndexes());
        combIndexes.addAll(other.getIndexes());

        Mask comb = new Mask(combValues, combIndexes, combDontCare);
        return Optional.of(comb);
    }

    /**
     * Checks if this product was used in a combination with another product.
     *
     * @return <code>true</code> if this product was used in a combination with
     * another product; <code>false</code> otherwise
     */

    public boolean isCombined() {
        return combined;
    }


    /**
     * Sets a flag which indicates whether this product was used in a combination
     * with another product.
     *
     * @param combined the flag to set
     */
    public void setCombined(boolean combined) {
        this.combined = combined;
    }

    /**
     * Checks if this mask represents a "don't care" minterm.
     *
     * @return <code>true</code> if the minterm is a "don't care";
     * <code>false</code> otherwise
     */
    public boolean isDontCare() {
        return dontCare;
    }

    /**
     * Gets a set of indexes of this mask.
     *
     * @return a set of indexes of this mask
     */
    public Set<Integer> getIndexes() {
        return indexes;
    }

    @Override
    public int hashCode() {
        return valuesHashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Mask mask = (Mask) o;

        if (this.valuesHashCode != mask.valuesHashCode) {
            return false;
        }

        return Arrays.equals(values, mask.values);
    }

    @Override
    public String toString() {
        String mask = Arrays.toString(values)
            .replace("[", "")
            .replace("]", "")
            .replace(", ", "")
            .replace("2", "-");

        return String.format(
            "%s %c %c %s",
            mask,
            dontCare ? 'D' : '.',
            combined ? '*' : ' ',
            indexes.toString()
        );
    }
}

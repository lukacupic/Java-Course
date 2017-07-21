package hr.fer.zemris.bf.qmc;

import hr.fer.zemris.bf.model.*;
import hr.fer.zemris.bf.utils.ExpressionCreator;
import hr.fer.zemris.bf.utils.Util;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a minimizer of boolean functions, which relies on the
 * Quine–McCluskey algorithm for finding the prime implicants of the boolean
 * function. {@see https://en.wikipedia.org/wiki/Quine-McCluskey_algorithm}
 *
 * @author Luka Čupić
 */
public class Minimizer {

    /**
     * Represents a logger object.
     */
    private static final Logger LOG = Logger.getLogger("hr.fer.zemris.bf.qmc");

    /**
     * Represents the minterms of the boolean function.
     */
    private Set<Integer> mintermSet;

    /**
     * Represents the "don't care" values of the boolean function.
     */
    private Set<Integer> dontCareSet;

    /**
     * Represents the variables of the boolean function.
     */
    private List<String> variables;

    /**
     * Represents the minimal forms of the boolean function.
     */
    private List<Set<Mask>> minimalForms;

    /**
     * Create a new instance of this class.
     *
     * @param mintermSet  a set of minterms representing the boolean function
     * @param dontCareSet a set of "don't care" values of the boolean function
     * @param variables   a set of variables of the boolean function
     * @throws IllegalArgumentException if set of minterms and set of "dont cares"
     *                                  are not disjoint (i.e. if they have at least
     *                                  one common element)
     */
    public Minimizer(Set<Integer> mintermSet, Set<Integer> dontCareSet,
                     List<String> variables) throws IllegalArgumentException {
        if (checkNonOverlapping(mintermSet, dontCareSet)) {
            throw new IllegalArgumentException("Minterms and don't cares must " +
                "represent different indexes!");
        }

        checkIndexes(mintermSet, variables.size());
        checkIndexes(dontCareSet, variables.size());

        this.mintermSet = new TreeSet<>(mintermSet);
        this.dontCareSet = new TreeSet<>(dontCareSet);
        this.variables = new ArrayList<>(variables);

        Set<Mask> primCover = findPrimaryImplicants();
        this.minimalForms = chooseMinimalCover(primCover);
    }

    /**
     * Checks if indexes of the specified set are a legal representation of
     * indexes of a boolean function of the given number of variables.
     * By "legal", it is meant that the indexes must be strictly smaller
     * than 2^{@param noOfVariables}.
     *
     * @param set           the set whose indexes are to be checked
     * @param noOfVariables the number of variables of the boolean function
     * @throws IllegalArgumentException if one of the indexes is illegal
     */
    private void checkIndexes(Set<Integer> set, int noOfVariables)
        throws IllegalArgumentException {
        for (int i : set) {
            if (i >= Math.pow(2, noOfVariables)) {
                throw new IllegalArgumentException("The index " + i + " " +
                    "is too large to represent an index of the given boolean function!");
            }
        }
    }

    /**
     * Checks whether the given sets are disjoint, i.e. if they have no
     * common elements.
     *
     * @param first  the first set
     * @param second the second set
     * @return true if the given sets are disjoint; false otherwise
     */
    private boolean checkNonOverlapping(Set<Integer> first, Set<Integer> second) {
        for (int f : first) {
            if (second.contains(f)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a set of prime implicants of the boolean function
     * which this minimizer is trying to minimize.
     *
     * @return a set of prime implicants
     */
    private Set<Mask> findPrimaryImplicants() {
        List<Set<Mask>> currentColumn = createFirstColumn();

        Set<Mask> primeImplicants = new LinkedHashSet<>();

        List<Set<Mask>> nextColumn = createList(currentColumn.size() - 1);

        boolean first = true;
        while (currentColumn.size() > 0) {
            for (int i = 0; i < currentColumn.size() - 1; i++) {
                Set<Mask> group1 = currentColumn.get(i);
                Set<Mask> group2 = currentColumn.get(i + 1);

                for (Mask m1 : group1) {
                    for (Mask m2 : group2) {
                        Optional<Mask> opt = m1.combineWith(m2);
                        if (!opt.isPresent()) continue;

                        m1.setCombined(true);
                        m2.setCombined(true);

                        nextColumn.get(i).add(opt.get());
                    }
                }
            }
            logColumn(extractMasks(currentColumn));

            for (Set<Mask> set : currentColumn) {
                for (Mask m : set) {
                    if (!m.isCombined() && !m.isDontCare()) {
                        primeImplicants.add(m);
                        logPrime(m);
                    }
                }
            }
            if (!first && nextColumn.size() != 0 &&
                LOG.isLoggable(Level.FINEST)) {
                logEmpty(Level.FINEST);
            }
            first = false;

            currentColumn = nextColumn;
            nextColumn = createList(currentColumn.size() - 1);
        }
        logPrimes(primeImplicants);

        return primeImplicants;
    }

    /**
     * Creates an empty log of the given level.
     *
     * @param level the level of the log
     */
    private void logEmpty(Level level) {
        if (!LOG.isLoggable(level)) return;
        LOG.log(level, "");
    }

    /**
     * Creates a new level {@link Level#FINE} log, which will
     * store all found prime implicants.
     *
     * @param set the set of prime implicants to log
     */
    private void logPrimes(Set<Mask> set) {
        if (set.size() == 0 || !LOG.isLoggable(Level.FINE)) return;

        LOG.log(Level.FINE, "Svi primarni implikanti:");
        set.forEach(s -> LOG.log(Level.FINE, s.toString()));
        LOG.log(Level.FINE, "");
    }

    /**
     * Creates a new level {@link Level#FINER} log, which stores
     * the column of products of the current QMC algorithm iteration.
     *
     * @param list the list of strings to log
     */
    private void logColumn(List<String> list) {
        if (list.size() == 0 || !LOG.isLoggable(Level.FINER)) return;

        LOG.log(Level.FINER, "Stupac tablice:");
        LOG.log(Level.FINER, "=================================");
        list.forEach(s -> LOG.log(Level.FINER, s));
        LOG.log(Level.FINER, "");
    }

    /**
     * Creates a new level {@link Level#FINEST} log, which stores
     * currently found primary implicant.
     *
     * @param m the primary implicant to log
     */
    private void logPrime(Mask m) {
        if (!LOG.isLoggable(Level.FINEST)) return;

        LOG.log(Level.FINEST, "Pronašao primarni implikant: " + m);
    }

    /**
     * Extracts all {@link Mask} objects from all the sets contained
     * in the specified list and returns them as strings representations
     * in a single list. Additional formatting strings are added to the
     * returned list, as to separate the pre-existing sets (groups):
     *
     * @param list the list of sets of masks to extract from
     * @return a new array list containing all masks from all sets
     * of the given list, represented as strings
     */
    private List<String> extractMasks(List<Set<Mask>> list) {
        List<String> masks = new ArrayList<>();

        for (Set<Mask> s : list) {
            for (Mask m : s) {
                masks.add(m.toString());
            }

            if (masks.size() != 0) {
                masks.add("-------------------------------");
            }
        }
        if (masks.size() > 0) {
            masks.remove(masks.size() - 1); // remove the last separator
        }
        return masks;
    }

    /**
     * Returns the first "column" of the Quine–McCluskey algorithm which
     * represents groups of products, where each group contains products
     * with the same number of values "1" in it's mask.
     *
     * @return the first column of the Quine–McCluskey algorithm, which
     * is represented by a list of sets of {@link Mask}s, where each
     * set holds all products with the same number of values "1", and
     * where the list itself represents the column, by holding all the
     * groups, i.e. sets
     */
    private List<Set<Mask>> createFirstColumn() {
        List<Set<Mask>> column = createList(variables.size() + 1);

        addToList(mintermSet, false, column);
        addToList(dontCareSet, true, column);
        return column;
    }

    /**
     * Creates and returns a new ArrayList<Set<Mask>> object of the
     * given number of empty set elements.
     *
     * @param n the number of elements for this list to contain
     * @return a new list, containing {@param n} empty sets
     */
    private List<Set<Mask>> createList(int n) {
        List<Set<Mask>> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add(new LinkedHashSet<>());
        }
        return list;
    }

    /**
     * A helper method which iterates the given set of integers, which
     * represents indexes of the boolean function's truth-table, and
     * creates masks of corresponding indexes which are then stored in
     * the given list.
     *
     * @param set        the set to iterate
     * @param isDontCare a flag to denote whether the created mask will
     *                   be a "don't care"
     */
    private void addToList(Set<Integer> set, boolean isDontCare, List<Set<Mask>> column) {
        int length = variables.size();

        for (int val : set) {
            byte[] bytes = Util.indexToByteArray(val, length);

            Set<Integer> indexes = new TreeSet<>();
            indexes.add(val);

            Mask mask = new Mask(bytes, indexes, isDontCare);
            column.get(mask.countOfOnes()).add(mask);
        }
    }

    /**
     * Returns the minimal cover of the boolean function.
     *
     * @param primCover the set of function's prime implicants
     * @return the minimal cover of the boolean function.
     */
    private List<Set<Mask>> chooseMinimalCover(Set<Mask> primCover) {
        Mask[] implicants = primCover.toArray(new Mask[primCover.size()]);
        Integer[] minterms = mintermSet.toArray(new Integer[mintermSet.size()]);

        boolean[][] table = buildCoverTable(implicants, minterms);

        boolean[] coveredMinterms = new boolean[minterms.length];

        Set<Mask> importantSet = selectImportantPrimaryImplicants(
            implicants, minterms, table, coveredMinterms);
        logImportants(importantSet);

        List<Set<BitSet>> pFunction = buildPFunction(table, coveredMinterms);
        logPFunction(pFunction);

        Set<BitSet> minset = findMinimalSet(pFunction);
        logMinset(minset);

        List<Set<Mask>> minimalForms = new ArrayList<>();
        for (BitSet bs : minset) {
            Set<Mask> set = new LinkedHashSet<>(importantSet);
            bs.stream().forEach(i -> set.add(implicants[i]));
            minimalForms.add(set);
        }
        logMinforms(minimalForms);
        return minimalForms;
    }

    /**
     * Creates a new level {@link Level#FINE} log, which will
     * store all found essential prime implicants.
     *
     * @param set the set to log
     */
    private void logImportants(Set<Mask> set) {
        if (!LOG.isLoggable(Level.FINE)) return;

        LOG.log(Level.FINE, "Bitni primarni implikanti su:");
        for (Mask m : set) {
            LOG.log(Level.FINE, m.toString());
        }
        LOG.log(Level.FINE, "");
    }

    /**
     * Creates a new level {@link Level#FINER} log, which
     * will store the p function.
     *
     * @param list the list to log
     */
    private void logPFunction(List<Set<BitSet>> list) {
        if (!LOG.isLoggable(Level.FINER)) return;

        LOG.log(Level.FINER, "p funkcija je:");
        LOG.log(Level.FINER, list.toString());
        LOG.log(Level.FINER, "");
    }

    /**
     * Creates a new level {@link Level#FINER} log, which will
     * store the minimal set of prime implicants.
     *
     * @param set the set to log
     */
    private void logMinset(Set<BitSet> set) {
        if (!LOG.isLoggable(Level.FINER)) return;

        LOG.log(Level.FINER, " Minimalna pokrivanja još trebaju:");
        LOG.log(Level.FINER, set.toString());
        LOG.log(Level.FINER, "");
    }

    /**
     * Creates a new level {@link Level#FINE} log, which will
     * store the minimal forms of the boolean function.
     *
     * @param list the list to log
     */
    private void logMinforms(List<Set<Mask>> list) {
        if (!LOG.isLoggable(Level.FINE)) return;

        LOG.log(Level.FINE, "Minimalni oblici funkcije su:");
        for (Set<Mask> s : list) {
            LOG.log(Level.FINE, s.toString());
        }
        LOG.log(Level.FINE, "");
    }

    /**
     * Returns a set of essential prime implicants of a boolean function.
     *
     * @param implicants      array of primary implicants
     * @param minterms        array of function's minterms
     * @param table           the table created by {@link Minimizer#buildCoverTable}
     * @param coveredMinterms a boolean array representing the minterms
     *                        which are covered by the found essential
     *                        prime implicants - this table will be modified on the
     *                        call of the function!
     * @return a set of essential prime implicants of a boolean function.
     */
    private Set<Mask> selectImportantPrimaryImplicants(
        Mask[] implicants, Integer[] minterms,
        boolean[][] table, boolean[] coveredMinterms) {
        Set<Mask> importants = new LinkedHashSet<>();

        for (int i = 0; i < minterms.length; i++) {
            int counter = 0;
            int index = -1;

            for (int j = 0; j < implicants.length; j++) {
                if (table[j][i]) {
                    counter++;
                    index = j;
                }
            }
            if (counter != 1) continue;

            Mask important = implicants[index];
            importants.add(important);

            for (int k = 0; k < minterms.length; k++) {
                coveredMinterms[k] =
                    important.getIndexes().contains(minterms[k]);
            }
        }
        return importants;
    }

    /**
     * Builds a "P function" of prime implicants of the boolean function.
     * The P function is modeled as a "product-of-sums" function, where
     * each product represents those prime implicants that cover a given
     * minterm.
     *
     * @param table           the table created by {@link Minimizer#buildCoverTable}
     * @param coveredMinterms a boolean array representing the minterms
     *                        which are not covered by an essential prime
     *                        implicant; such minterms are denoted as "false"
     * @return a function representing the product of sums, where products
     * represent a sum of prime implicants which cover a given minterm
     */
    private List<Set<BitSet>> buildPFunction(
        boolean[][] table, boolean[] coveredMinterms) {
        int noOfNonCovered = countNonCovered(coveredMinterms);
        if (table.length == 0) {
            return new ArrayList<>();
        }

        List<Set<BitSet>> pFunction = new ArrayList<>();
        for (int i = 0; i < table[0].length; i++) {
            if (coveredMinterms[i]) continue;

            Set<BitSet> setOfProducts = new LinkedHashSet<>();
            for (int j = 0; j < table.length; j++) {
                if (table[j][i]) {
                    BitSet product = new BitSet(noOfNonCovered);
                    product.set(j);
                    setOfProducts.add(product);
                }
            }
            pFunction.add(setOfProducts);
        }
        return pFunction;
    }

    /**
     * Counts the number of "false" elements in the given boolean array.
     *
     * @param coveredMinterms the array to perform the counting on
     * @return the number elements whose value is equal to {@code false}
     */
    private int countNonCovered(boolean[] coveredMinterms) {
        int count = 0;
        for (boolean min : coveredMinterms) {
            if (!min) {
                count++;
            }
        }
        return count;
    }

    /**
     * Minimizes the given p-function and returns a set of BitSets
     * which are needed to completely represent the function.
     *
     * @param pFunction the p-function
     * @return the minimal representation of the given function
     */
    private Set<BitSet> findMinimalSet(List<Set<BitSet>> pFunction) {
        Set<BitSet> sop = toSumOfProducts(pFunction);
        logSOP(sop);
        return minimizeProduct(sop);
    }

    private void logSOP(Set<BitSet> set) {
        if (!LOG.isLoggable(Level.FINER)) return;

        LOG.log(Level.FINER, "Nakon prevorbe p-funkcije u sumu produkata:");
        LOG.log(Level.FINER, set.toString());
        LOG.log(Level.FINER, "");
    }

    /**
     * Coverts a given function, in the form of product of sums, to it's
     * corresponding function in the form of sum of products.
     *
     * @param pFunction the function to convert
     * @return a sum-of-products representation of the given function
     */
    private Set<BitSet> toSumOfProducts(List<Set<BitSet>> pFunction) {
        if (pFunction.size() == 0) {
            return new LinkedHashSet<>();
        }

        Set<BitSet> totalProduct = new LinkedHashSet<>();
        totalProduct.addAll(pFunction.get(0)); // add the first product

        for (int i = 1; i < pFunction.size(); i++) {
            Set<BitSet> set = pFunction.get(i);

            Set<BitSet> currentProduct = new LinkedHashSet<>();
            for (BitSet first : totalProduct) {
                for (BitSet second : set) {
                    BitSet clone = (BitSet) first.clone();
                    clone.or(second);
                    currentProduct.add(clone);
                }
            }
            totalProduct = currentProduct;
        }
        return totalProduct;
    }

    /**
     * Returns a minimized form of the given product of sums.
     *
     * @param bitSets the set (representing the sum)
     *                of BitSets (representing the products)
     * @return the minimized product of sums
     */
    private Set<BitSet> minimizeProduct(Set<BitSet> bitSets) {
        // if the set is empty, the minimized form is also empty
        if (bitSets.size() == 0) {
            return new LinkedHashSet<>();
        }

        int minCardinality = minimalCardinality(bitSets);

        Set<BitSet> minimal = new LinkedHashSet<>();
        for (BitSet b : bitSets) {
            if (b.cardinality() == minCardinality) {
                minimal.add(b);
            }
        }
        return minimal;
    }

    /**
     * Returns the minimal cardinality of the given set of {@link BitSet}s.
     *
     * @param bitSets the set to find the minimal cardinality
     * @return the cardinality of the BitSet with the smallest cardinality
     */
    private int minimalCardinality(Set<BitSet> bitSets) {
        Iterator<BitSet> it = bitSets.iterator();
        int min = it.next().cardinality();

        while (it.hasNext()) {
            BitSet current = it.next();
            if (current.cardinality() < min) {
                min = current.cardinality();
            }
        }
        return min;
    }

    /**
     * Creates and returns a table where rows represent prime implicants and
     * columns represent minterms. Each element indicates whether the implicant
     * of the corresponding row covers a minterm of the corresponding column.
     *
     * @param implicants the array of prime implicants
     * @param minterms   the array of minterms
     * @return a table where each element is true only if the corresponding
     * implicant covers a corresponding minterm
     */
    private boolean[][] buildCoverTable(Mask[] implicants, Integer[] minterms) {
        boolean[][] table = new boolean[implicants.length][minterms.length];

        for (int i = 0; i < implicants.length; i++) {
            for (int j = 0; j < minterms.length; j++) {
                table[i][j] = implicants[i].getIndexes().contains(minterms[j]);
            }
        }
        return table;
    }

    /**
     * Returns the minimal forms of the specified boolean function.
     *
     * @return all minimal forms of the boolean function, in the form
     * of a list of sets, where each set represents a set of function's
     * implicants.
     */
    public List<Set<Mask>> getMinimalForms() {
        return this.minimalForms;
    }

    /**
     * Returns minimal forms of the specified boolean function
     * as a list of expressions.
     *
     * @return a list of {@link Node}s, where each node represents
     * a single minimal form of the boolean function
     */
    public List<Node> getMinimalFormsAsExpressions() {
        List<Node> expressions = new ArrayList<>();

        // if the function is a contradiction, then it's value is false
        if (minimalForms.size() == 0) {
            expressions.add(new ConstantNode(false));
            return expressions;
        }

        // if the function is a tautology, then it's value is true
        if (minimalForms.size() == 1) {
            if (minimalForms.get(0).size() == 0) {
                expressions.add(new ConstantNode(true));
                return expressions;
            }
        }

        for (Set<Mask> s : minimalForms) {
            Node sumNode;

            List<Node> sumList = new ArrayList<>();
            for (Mask m : s) {
                Node productNode;

                List<Node> productList = new ArrayList<>();
                for (int i = 0; i < m.size(); i++) {
                    byte b = m.getValueAt(i);
                    if (b == 2) continue;

                    Node current;

                    VariableNode var = new VariableNode(variables.get(i));
                    if (b == 1) {
                        current = var;
                    } else {
                        current = new UnaryOperatorNode("not", var, a -> !a);
                    }
                    productList.add(current);
                }
                productNode = new BinaryOperatorNode("and", productList, Boolean::logicalAnd);
                sumList.add(productNode);
            }
            sumNode = new BinaryOperatorNode("or", sumList, Boolean::logicalOr);
            expressions.add(sumNode);
        }
        return expressions;
    }

    /**
     * Returns string representations of minimal forms of the
     * specified boolean function.
     *
     * @return a list of strings, where each strings represents
     * one minimal form of the function
     */
    public List<String> getMinimalFormsAsString() {
        List<String> expressions = new ArrayList<>();
        ExpressionCreator c = new ExpressionCreator();

        List<Node> nodes = getMinimalFormsAsExpressions();
        for (Node node : nodes) {
            node.accept(c);
            expressions.add(c.getExpression());
        }
        return expressions;
    }
}

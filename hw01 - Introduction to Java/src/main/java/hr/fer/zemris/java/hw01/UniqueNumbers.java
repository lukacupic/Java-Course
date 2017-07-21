package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This class represents a simple binary tree structure which allows the user to store and
 * manipulate the elements of a tree. The tree is a set, and therefore stores only unique values.
 * k
 *
 * @author Luka Čupić
 */
public class UniqueNumbers {

    /**
     * A helper class which represents the node of a tree.
     */
    public static class TreeNode {

        /**
         * The left child of the node.
         */
        public TreeNode left;

        /**
         * The right child of the node.
         */
        public TreeNode right;

        /**
         * The value held by the node.
         */
        public int value;
    }

    /**
     * The beginning method of the class.
     *
     * @param args this program requires no command line arguments.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TreeNode head = null;

        System.out.printf("Unesite broj > ");
        while (sc.hasNext()) {
            String input = sc.next();

            if (input.compareTo("kraj") == 0) {
                break;
            }

            try {
                int value = Integer.parseInt(input);

                if (!containsValue(head, value)) {
                    head = addNode(head, value);
                    System.out.println("Dodano!");
                } else {
                    System.out.println("Broj već postoji. Preskačem.");
                }

            } catch (NumberFormatException ex) {
                System.out.printf("'%s' nije cijeli broj!%n", input);
            }

            System.out.printf("Unesite broj > ");
        }

        // prints elements inorder
        System.out.println("Ispis od najmanjeg: ");
        printInorder(head);
        System.out.printf("%n");

        // prints elements in reversed inorder
        System.out.println("Ispis od najvećeg: ");
        printReversedInorder(head);
        System.out.printf("%n");

        sc.close();
    }

    /**
     * Adds a new element, specified by {@code value}, to the tree.
     *
     * @param head  the head node of the tree.
     * @param value the value of a new element to be added to the tree.
     */
    public static TreeNode addNode(TreeNode head, int value) {
        if (head == null) {
            head = new TreeNode();
            head.value = value;
        } else {
            if (value < head.value) {
                head.left = addNode(head.left, value);
            } else if (value > head.value) {
                head.right = addNode(head.right, value);
            }
        }
        return head;
    }

    /**
     * Returns the size of the tree, i.e. the number of nodes.
     *
     * @param head of the tree.
     * @return the number of nodes.
     */
    public static int treeSize(TreeNode head) {
        if (head == null) {
            return 0;
        }

        // recursively searches all nodes and for each, adds 1 to the total number
        return 1 + treeSize(head.left) + treeSize(head.right);
    }

    /**
     * Checks whether an element with the value {@code value} exists in the tree.
     *
     * @param head  the head of the tree.
     * @param value of the element to search for.
     * @return true if and only if the element of the value {@code value} exists in the tree.
     */
    public static boolean containsValue(TreeNode head, int value) {
        if (head == null) {
            return false;
        }

        if (head.value == value) {
            return true;
        }

        return containsValue(head.left, value) || containsValue(head.right, value);
    }

    /**
     * Iterates the tree and prints sorted values in an ascending (natural) order.
     *
     * @param head the head of the tree.
     */
    private static void printInorder(TreeNode head) {
        if (head == null) {
            return;
        }

        printInorder(head.left);
        System.out.printf("%d ", head.value);
        printInorder(head.right);
    }

    /**
     * Iterates the tree and prints sorted values in a descending order.
     *
     * @param head the head of the tree.
     */
    private static void printReversedInorder(TreeNode head) {
        if (head == null) {
            return;
        }

        printReversedInorder(head.right);
        System.out.printf("%d ", head.value);
        printReversedInorder(head.left);
    }
}

/**
 * $Id: QuickSizes.java, v 1.0 13/06/2014 22:04 oscarfabra Exp $
 * {@code QuickSizes} Is a class that sorts an array of lists according to the
 * size of each of its lists using quicksort.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 13/06/2014
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class that sorts an array of lists according to the size of each of its
 * lists using quicksort.
 */
public class QuickSizes
{
    //-------------------------------------------------------------------------
    // CLASS ATTRIBUTES
    //-------------------------------------------------------------------------

    // Array of keys to sort the given Map in the sortSizes() method
    private static int [] keys = null;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    // This class should not be instantiated
    private QuickSizes(){ }

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Sorts the given map of lists of integers according to the size of each
     * of its lists and returns the array of keys of such lists in such order.
     * @param lists Map of lists of integers to examine.
     * @return List of keys in descending order of the sizes of their lists.
     */
    public static int [] sortSizes(Map<Integer, List<Integer>> lists)
    {
        // Initializes the keys class array to sort the given map according to
        // the size of each of its lists, as expected
        QuickSizes.keys = new int[lists.keySet().size()];

        // Creates and fills an array with the size of each of its lists.
        // sizes[i] = size of list i of the given map lists. Also fills the
        // keys class array as a tool to sort later the given map
        int[] sizes = new int[lists.keySet().size()];
        Iterator<Integer> iterator = lists.keySet().iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            int key = iterator.next();
            List<Integer> ids = lists.get(key);
            sizes[i] = ids.size();
            QuickSizes.keys[i] = key;
            i++;
        }

        // Sorts sizes array AND the class array keys
        sort(sizes);

        // Returns the sorted array of keys from the given map
        return QuickSizes.keys;
    }

    /**
     * Sorts the given array using quicksort pivoting over the first element.
     * @param a Array of int numbers.
     */
    private static void sort(int[] a)
    {
        sort(a, 0, a.length - 1);
    }

    /**
     * Prints the elements of the given array in standard output.
     * @param a Array of int numbers.
     */
    private static void show(int[] a)
    {
        for (int i = 0; i < a.length; i++)
        {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Sorts array a in [lb...ub] using the first element as pivot.
     * @param a  Array of int numbers.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     */
    private static void sort(int[] a, int lb, int ub)
    {
        if (lb >= ub) {
            return;
        }
        int p = partitionFirst(a, lb, ub);
        sort(a, lb, p - 1);
        sort(a, p + 1, ub);
    }

    /**
     * Partitions the array a in [index...ub] using the first element as pivot.
     * @param a  Array of int numbers.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     * @return Index of the pivot element.
     */
    private static int partitionFirst(int[] a, int lb, int ub)
    {
        int p = a[lb];
        int i = lb + 1;
        for (int j = lb + 1; j <= ub; j++) {
            if (a[j] < p) {
                swap(a, j, i);
                i++;
            }
        }
        swap(a, lb, i - 1);
        return i - 1;
    }

    /**
     * Swaps the elements in array a at indices j and i. <br/>
     * @param a Array of int numbers.
     * @param j Index of element to swap.
     * @param i Index of element to swap.
     */
    private static void swap(int[] a, int j, int i)
    {
        // Swap elements in a
        int aux = a[j];
        a[j] = a[i];
        a[i] = aux;

        // Swap elements in class array keys accordingly
        aux = QuickSizes.keys[j];
        QuickSizes.keys[j] = QuickSizes.keys[i];
        QuickSizes.keys[i] = aux;
    }

    /**
     * Converts the given array of String into an array of int.
     * @param a    Array of int numbers.
     * @param args List of numbers separated by spaces.
     */
    private static void convertStrings(int[] a, String[] args)
    {
        // Stores again the arguments received by the main
        for (int i = 0; i < args.length; i++)
        {
            a[i] = Integer.parseInt(args[i]);
        }
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method for the QuickSizes class. <br/>
     * @param args List of numbers separated by spaces.
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.exit(-1);
        }

        // converts the given array into an array of integers
        int[] a = new int[args.length];
        QuickSizes.convertStrings(a, args);
        System.out.println("Array received: ");
        QuickSizes.show(a);

        // Sorts array a using quicksort and then shows it
        sort(a);
        System.out.println("Sorted array: ");
        QuickSizes.show(a);
    }
}
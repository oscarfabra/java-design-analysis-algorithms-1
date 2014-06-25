/**
 * $Id: QuickSort.java, v 1.1 20/05/14 21:57 oscarfabra Exp $
 * {@code QuickSort} Class that sorts an array of numbers using quicksort.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.1
 * @since 20/05/14
 */


/**
 * Class that sorts an array of numbers using quicksort.
 */
public class QuickSort
{
    // This class should not be instantiated
    private QuickSort(){}

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Sorts the given array using quicksort pivoting over the first element.
     * @param a Array of int numbers.
     */
    public static void sort(long [] a)
    {
        sort(a, 0, a.length - 1);
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Sorts array a in [lb...ub] using the first element as pivot.
     * @param a Array of int numbers.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     */
    private static void sort(long[] a, int lb, int ub)
    {
        if(lb >= ub){ return; }
        int p = partitionFirst(a, lb, ub);
        sort(a, lb, p - 1);
        sort(a, p + 1, ub);
    }

    /**
     * Partitions the array a in [index...ub] using the first element as pivot.
     * @param a Array of int numbers.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     * @return Index of the pivot element.
     */
    private static int partitionFirst(long[] a, int lb, int ub)
    {
        long p = a[lb];
        int i = lb + 1;
        for(int j = lb + 1; j <= ub; j++)
        {
            if(a[j] < p)
            {
                swap(a, j, i);
                i++;
            }
        }
        swap(a, lb, i -1);
        return i - 1;
    }

    /**
     * Swaps the elements in array a at indices j and i. <br/>
     * @param a Array of int numbers.
     * @param j Index of element to swap.
     * @param i Index of element to swap.
     */
    private static void swap(long[] a, int j, int i)
    {
        long aux = a[j];
        a[j] = a[i];
        a[i] = aux;
    }
}

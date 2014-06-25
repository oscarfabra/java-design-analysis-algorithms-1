/**
 * $Id: TwoSum.java, v 1.0 22/06/14 17:43 oscarfabra Exp $
 * {@code TwoSum} Class that computes the 2-SUM algorithm for a given interval
 * of integers and a given array a.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.1
 * @since 20/05/14
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that computes the number of target values t in a given interval such
 * that there are distinct numbers x,y in a given array a that satisfy
 * x + y = t.
 */
public class TwoSum
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    // HashMap to store the values of the given array
    private static Map<Long,Long> h;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private TwoSum(){}      // This class should not be instantiated.

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the target values t in the interval [lb,ub] (inclusive) such that
     * there are distinct numbers x,y in a that satisfy x + y = t.
     *
     * @param a Array to examine.
     * @param lb Lower-bound number to look for.
     * @param ub Upper-bound number to look for.
     * @return List of numbers that satisfy the given conditions.
     */
    public static List<Long> getTwoSumDistinctNumbersInInterval(long[] a,
                                                                   long lb,
                                                                   long ub)
    {
        // Sorts the given array a
        System.out.print("Sorting given array...");
        QuickSort.sort(a);
        System.out.println("done.");

        // Inserts elements of a into HashMap h
        System.out.print("Inserting elements into HashMap...");
        TwoSum.h = new HashMap<Long, Long>(100000);
        for(int i = 0; i < a.length; i++)
        {
            TwoSum.h.put(a[i], a[i]);
        }
        System.out.println("done.");

        // Looks for each possible sum in the given range [lb...ub]
        System.out.println("Looking for target sums in the range [" + lb +
                "," + ub + "]...");
        List<Long> list = new ArrayList<Long>();
        for(long t = lb; t <= ub; t++)
        {
            if(distinctTwoSumT(a, t))
            {
                list.add(t);
                System.out.print("-- [" + list.size());
                System.out.print((list.size() == 1) ? " sum" : " sums");
                System.out.println(" found so far].");
            }

            // Shows a message in standard output for logging purposes
            if((t - lb + 1) % 1000 == 0)
            {
                System.out.println("-- " + (t - lb + 1) +" targets covered...");
            }
        }
        return list;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the 2-SUM problem for the given array a and the target sum t.
     * @param a Array to examine.
     * @param t Target sum to look for.
     * @return Whether there is a solution of the 2-SUM problem for the given
     * parameters.
     */
    private static boolean distinctTwoSumT(long[] a, long t)
    {
        // For each value x in a, looks up for t - x in h, guaranteeing that
        // both numbers are distinct
        for(int i = 0; i < a.length; i++)
        {
            // Guarantees that both values aren't the same
            if((2 * a[i]) != t)
            {
                if(TwoSum.h.containsKey(t - a[i]))
                {
                    return true;
                }
            }
        }
        return false;
    }
}

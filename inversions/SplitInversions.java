/**
 * Class to count the split inversions of a given array of integers.
 */
public class SplitInversions
{
    /**
     * Sorts the array a and counts the number of split inversions.
     * @param a The array to count the inversions for.
     * @return The number of split inversions in array a.
     */
    public static long sortAndCount(int [] a)
    {
        return sortAndCount(a,0,a.length - 1);
    }

    /**
     * Sorts the array a and counts the number of split inversions between the
     * given numbers.
     * @param a The array to count the inversions for.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     * @return The number of split inversions in array a.
     */
    private static long sortAndCount(int [] a, int lb, int ub)
    {
        if(ub <= lb)
        {
            return 0;
        }
        else
        {
            // Divide and conquer
            int mid = lb + (ub - lb)/2;
            long x = sortAndCount(a, lb, mid);
            long y = sortAndCount(a, mid + 1, ub);

            // Gets left and right sub-arrays
            int [] b = subArray(a, lb, mid);
            int [] c = subArray(a, mid + 1, ub);

            // Merges b and c into a and counts split inversions
            long z = mergeAndCountSplitInv(a, b, c, lb, mid, ub);

            return (x + y + z);
        }
    }

    /**
     * Gets a sub-array of integers from array a.
     * @param a Array to get a sub-array from.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     * @return The sub-array of integers.
     */
    private static int[] subArray(int[] a, int lb, int ub)
    {
        int [] b = new int[ub - lb + 1];

        for(int k = lb; k <= ub; k++)
        {
            b[k - lb] = a[k];
        }

        return b;
    }

    /**
     * Merges b and c into a and counts split inversions.
     * @param a Array to merge numbers into.
     * @param b Array with the first-half numbers of a.
     * @param c Array with the second half numbers of a.
     * @param lb Lower-bound index.
     * @param mid Index of final element in b.
     * @param ub Upper-bound index.
     * @return The number of split inversions.
     */
    private static long mergeAndCountSplitInv(int[] a, int [] b, int [] c,
                                              int lb, int mid, int ub)
    {
        int inversions = 0;
        int i = 0;
        int j = 0;

        for(int k = lb; k <= ub; k++)
        {
            if(i >= b.length)
            {
                a[k] = c[j];
                j++;
            }
            else if(j >= c.length)
            {
                a[k] = b[i];
                i++;
            }
            else if(b[i] <= c[j])
            {
                a[k] = b[i];
                i++;
            }
            else
            {
                inversions += mid - lb - i + 1;
                a[k] = c[j];
                j++;
            }
            //System.out.println("i = " + i + ", j = " + j + ", k = " + k);
        }
        return inversions;
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args List of numbers to form the array separated by spaces.
     */
    public static void main(String [] args)
    {
        if(args.length == 0)
        {
            System.out.println("Must enter the numbers of the array " +
                    "separated by spaces.");
            System.exit(1);
        }

        int [] a = new int[args.length];
        for(int i = 0; i < args.length; i++)
        {
            a[i] = Integer.parseInt(args[i]);
        }

        long inversions = SplitInversions.sortAndCount(a, 0, a.length - 1);

        System.out.println("The number of split inversions is: " + inversions);
    }
}

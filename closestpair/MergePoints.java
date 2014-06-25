/**
 *  The <tt>MergePoints</tt> class provides static methods for sorting an
 *  array of Point objects using mergesort.
 *  <p>
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Oscar Fabra (Adaptation for Point objects)
 */
public class MergePoints
{
    //-------------------------------------------------------------------------
    // CLASS ATTRIBUTES
    //-------------------------------------------------------------------------

    // Array of Point objects to order
    private static Point [] P;

    // Auxiliary array of Point objects
    private static Point [] auxP;

    // This class should not be instantiated.
    private MergePoints() { }

    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a)
    {
        Comparable[] aux = new Comparable[a.length];
        auxP = new Point[a.length];
        sort(a, aux, 0, a.length-1);
        assert isSorted(a);
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
    {
        if (hi <= lo)
            return;

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    private static void merge(Comparable[] a, Comparable[] aux, int lo,
                              int mid, int hi)
    {
        // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted sub-arrays
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid+1, hi);

        // copy to aux[]
        for (int k = lo; k <= hi; k++)
        {
            aux[k] = a[k];
            auxP[k] = new Point(P[k]);
        }

        // merge back to a[]
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++)
        {
            if (i > mid)
            {
                a[k] = aux[j];   // this copying is unnecessary
                P[k].copy(auxP[j]);
                j++;
            }
            else if (j > hi)
            {
                a[k] = aux[i];
                P[k].copy(auxP[i]);
                i++;
            }
            else if (less(aux[j], aux[i]))
            {
                a[k] = aux[j];
                P[k].copy(auxP[j]);
                j++;
            }
            else
            {
                a[k] = aux[i];
                P[k].copy(auxP[i]);
                i++;
            }
        }

        // postcondition: a[lo .. hi] is sorted
        assert isSorted(a, lo, hi);
    }

    //-------------------------------------------------------------------------
    // HELPER SORTING METHODS
    //-------------------------------------------------------------------------

    // is v < w ?
    private static boolean less(Comparable v, Comparable w)
    {
        return (Integer.parseInt(v.toString()) <
                Integer.parseInt(w.toString()));
    }
        
    // exchange a[i] and a[j]... and the corresponding Point objects.
    private static void exch(Object[] a, int i, int j)
    {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;

        // Sorts the array of points P
        Point point = new Point(P[i]);
        P[i].copy(P[j]);
        P[j].copy(point);
    }

    //-------------------------------------------------------------------------
    // DEBUGGING METHODS
    //-------------------------------------------------------------------------

    // Checks if array a is sorted
    private static boolean isSorted(Comparable[] a)
    {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi)
    {
        for (int i = lo + 1; i <= hi; i++)
        {
            if (less(a[i], a[i-1]))
                return false;
        }
        return true;
    }

    //-------------------------------------------------------------------------
    // SORTS CLASS POINTS ARRAY
    //-------------------------------------------------------------------------

    /**
     * Sorts the given array of Point objects either by the X or the Y
     * coordinate.
     *
     * @param P Array of Point objects to sort.
     * @param coord 'X' to sort by the x-coordinate, 'Y' to sort by the
     *              y-coordinate.
     */
    public static void sortByCoordinate(Point[] P, char coord)
    {
        // Copies each Point of the given array for memory performance
        MergePoints.P = new Point[P.length];
        Point.copyPoints(MergePoints.P, P);

        // Converts the array of int coordinates into an array of Comparable
        int [] coordinates = Point.coordinates(P, coord);
        String [] a = turnComparable(coordinates);

        // Sorts the given array and the MergePoints.P array
        sort(a);

        // Again copies the resulting ordered array of points
        Point.copyPoints(P, MergePoints.P);
    }

    /**
     * Turns an array of int into an array of Strings.
     * @param a Array of int to convert.
     * @return Array of String with the corresponding int values.
     */
    private static String[] turnComparable(int[] a)
    {
        String [] comparable = new String[a.length];
        for(int i = 0; i < a.length; i++)
        {
            comparable[i] = String.valueOf(a[i]);
        }
        return comparable;
    }
}

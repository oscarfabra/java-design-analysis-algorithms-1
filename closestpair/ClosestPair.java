/**
 * $Id: ClosestPair.java, v 1.0 4/05/14 21:57 oscarfabra Exp $
 * {@code ClosestPair}
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 4/05/14
 */

import java.util.List;
import java.util.Vector;

/**
 * Class that implements the closestpair algorithm to find the closes pair of a
 * given set of points in the xy plane.
 */
public class ClosestPair
{
    //-------------------------------------------------------------------------
    // CLASS CONSTANTS
    //-------------------------------------------------------------------------

    public static final char X_COORDINATE = 'X';    // x-coordinate
    public static final char Y_COORDINATE = 'Y';    // y-coordinate

    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the closest pair in a given set of points.
     * @param P Array of Point objects.
     * @return The closest pair of points [Point1, Point2].
     */
    public static Point [] findClosestPair(Point [] P)
    {
        Point [] Px = new Point[P.length];
        Point [] Py = new Point[P.length];
        copyAndSortByXAndY(P, Px, Py);
        return closestPair(Px, Py);
    }

    /**
     * Copies the array P into Px and Py and sorts by x and y coordinates
     * respectively.
     *
     * @param P Array of Point objects to copy and sort.
     * @param Px Array of Point objects to fill and order by x coordinates.
     * @param Py Array of Point objects to fill and order by y coordinates.
     */
    public static void copyAndSortByXAndY(Point [] P, Point [] Px, Point [] Py)
    {
        // Copies the elements of P into Px and Py
        for(int i = 0; i < P.length; i++)
        {
            Px[i] = new Point(P[i]);
            Py[i] = new Point(P[i]);
        }

        // Sorts Px by x-coordinates
        MergePoints.sortByCoordinate(Px, X_COORDINATE);

        // Sorts Py by y-coordinates
        MergePoints.sortByCoordinate(Py, Y_COORDINATE);
    }

    /**
     * Finds the closest pair of points given the set of points ordered by
     * x-coordinate and by y-coordinate.
     *
     * @param Px Set of Point objects ordered by their x-coordinates.
     * @param Py Set of Point objects ordered by their y-coordinates.
     * @return An array with two Points [P,Q] which are the closest pair.
     */
    public static Point [] closestPair(Point [] Px, Point [] Py)
    {
        if(Px.length <= 3)
        {
            // Creates pair1 with points Px[0] and Px[1]
            Point [] pair1 = new Point[2];
            pair1[0] = new Point(Px[0]);
            pair1[1] = new Point(Px[1]);

            if(Px.length == 3)
            {
                // Creates pair2 with points Px[0] and Px[2]
                Point [] pair2 = new Point[2];
                pair2[0] = new Point(Px[0]);
                pair2[1] = new Point(Px[2]);

                return bestPair(pair1, pair2);
            }

            // If there's just one pair of points, return it.
            return pair1;
        }

        // Forms Qx, Qy and Rx, Ry
        Point [] Qx = leftPoints(Px, Py, X_COORDINATE);
        Point [] Qy = leftPoints(Px, Py, Y_COORDINATE);
        Point [] Rx = rightPoints(Px, Py, X_COORDINATE);
        Point [] Ry = rightPoints(Px, Py, Y_COORDINATE);

        Point [] pair1 = closestPair(Qx, Qy);
        Point [] pair2 = closestPair(Rx, Ry);

        // Finds the min distance between points (p1,q1) and (p2,q2)
        double delta = Math.min(Point.d(pair1), Point.d(pair2));
        Point [] bestPair12 = bestPair(pair1, pair2);

        // Finds the closest split pair and stores it in p3 and q3
        Point [] pair3 = closestSplitPair(Px, Py, bestPair12, delta);

        // Returns the pair with the minimum distance between its points
        return bestPair(bestPair12, pair3);
    }

    /**
     * Returns a list with the left points of P ordered by the coordinate
     * given in coord.
     *
     * @param Px List of points sorted by x-coordinate.
     * @param Py List of points sorted by y-coordinate.
     * @param coord 'X' or 'Y' whether P is sorted by x or y coordinates.
     * @return A list of the left points of P ordered by the given coordinate.
     */
    private static Point[] leftPoints(Point[] Px, Point [] Py, char coord)
    {
        int n = Px.length;
        int mid = (n%2 == 1) ? n/2 + 1 : n/2;
        Point [] Q = new Point[n];

        // Always look for the x-coordinate
        for(int i = 0; i < mid; i++)
        {
            if(coord == X_COORDINATE)
            {
                Q[i] = Px[i];
            }
            else
            {
                Q[correspondingYIndex(Py, Px[i])] = new Point(Px[i]);
            }
        }

        return removeWhiteSpaces(Q);
    }

    /**
     * Returns a list with the right points of P ordered by the coordinate
     * given in coord.
     *
     * @param Px List of points sorted by x-coordinate.
     * @param Py List of points sorted by y-coordinate.
     * @param coord 'X' or 'Y' whether P is sorted by x or y coordinates.
     * @return A list of the right points of P ordered by the given coordinate.
     */
    private static Point[] rightPoints(Point[] Px, Point [] Py, char coord)
    {
        int n = Px.length;
        int mid = (n%2 == 1) ? n/2 + 1 : n/2;
        Point [] R = new Point[n];
        // Always look for the x-coordinate
        for(int i = mid; i < n; i++)
        {
            if(coord == X_COORDINATE)
            {
                R[i - mid] = Px[i];
            }
            else
            {
                R[correspondingYIndex(Py, Px[i])] = new Point(Px[i]);
            }
        }
        return removeWhiteSpaces(R);
    }

    /**
     * Removes all null points from array P
     * @param P List of points to remove nulls from.
     * @return Array of Point objects without null objects.
     */
    private static Point[] removeWhiteSpaces(Point[] P)
    {
        List<Point> Q = new Vector<Point>();

        for(int i = 0; i < P.length; i++)
        {
            if(P[i] != null)
            {
                Q.add(P[i]);
            }
        }
        return Point.toArray(Q);
    }

    /**
     * Returns the corresponding index in Py where point p is located.
     * @param Py List of points sorted by y-coordinate.
     * @param p Point to look for.
     * @return The index where point p is located.
     */
    private static int correspondingYIndex(Point[] Py, Point p)
    {
        int i = 0;
        while(!Py[i].equals(p)) { i++; }
        return i;
    }

    /**
     * Finds the closest split pair given a delta and the set of points ordered
     * by x-coordinates and y-coordinates.
     *
     * @param Px List of points to look for ordered by x-coordinates.
     * @param Py List of points to look for ordered by y-coordinates.
     * @param delta Minimum distance of the right and left set of points.
     * @return The pair with the minimum distance between them.
     */
    private static Point[] closestSplitPair(Point[] Px, Point[] Py,
                                            Point [] bestPair, double delta)
    {
        int midX = medianXCoordinate(Px);
        Point [] Sy = pointsWithinDelta(Px, Py, midX, delta);
        double bestDistance = delta;
        for(int i = 0; i < Sy.length - 1; i++)
        {
            Point p = new Point(Sy[i]);
            for(int j = 1; j + i < Math.min(7, Sy.length); j++)
            {
                Point q = new Point(Sy[i + j]);
                if(Point.d(p, q) < bestDistance)
                {
                    bestPair[0] = p;
                    bestPair[1] = q;
                    bestDistance = Point.d(p,q);
                }
            }
        }
        return bestPair;
    }

    /**
     * Finds the index in Px of the biggest x-coordinate in left of P.
     *
     * @param Px List of points sorted by x-coordinate.
     * @return The biggest x-coordinate in left of P.
     */
    private static int medianXCoordinate(Point[] Px)
    {
        int n = Px.length;
        int range = Px[n-1].x() - Px[0].x();
        int i = 0;
        while(Px[i].x() <= Px[0].x() + range/2){ i++; }
        return Px[i - 1].x();
    }

    /**
     * Returns the list of points of P with x-coordinate in
     * [mid - delta, mid + delta], sorted by y-coordinate.
     *
     * @param Px List of points sorted by x-coordinate.
     * @param Py List of points sorted by y-coordinate.
     * @param mid Index in Px of the median point.
     * @param delta Minimum distance of the right and left set of points.
     * @return List of points with x-coordinate in [mid - delta, mid + delta],
     * sorted by y-coordinate.
     */
    private static Point[] pointsWithinDelta(Point[] Px, Point[] Py, int mid,
                                             double delta)
    {
        int n = Px.length;
        List<Point> Sy = new Vector<Point>();
        double lb = mid - delta;
        double ub = mid + delta;
        for(int i = 0; i < n ; i++)
        {
            if(lb <= Py[i].x() && Py[i].x() <= ub)
            {
                Sy.add(Py[i]);
            }
        }
        return Point.toArray(Sy);
    }

    /**
     * Returns the pair with the minimum distance between its points.
     * @param pair1 First pair of points, pair1[0] = p1, pair1[1] = q1.
     * @param pair2 Second pair of points, pair2[0] = p2, pair2[1] = q2.
     * @return Array of points with p in position [0] and q in position [1].
     */
    private static Point[] bestPair(Point[] pair1, Point[] pair2)
    {
        double d1 = Point.d(pair1);
        double d2 = Point.d(pair2);
        double min = Math.min(d1, d2);
        if(min == d1)
        {
            return pair1;
        }
        return pair2;
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args Array of integers separated by commas. Odd positions are x
     *             values, even positions are y values of the points.
     */
    public static void main(String [] args)
    {
        if(args.length == 0 || args.length%2 == 1)
        {
            System.out.println("Bad input given. Must be an even number of " +
                    "numbers.");
        }
        // Creates the set of Point objects
        Point [] P = new Point[args.length / 2];
        System.out.println("The given set of points P contains: ");
        for(int i = 0; i < args.length; i+=2)
        {
            int x = Integer.parseInt(args[i]);
            int y = Integer.parseInt(args[i+1]);
            Point point = new Point(x,y);
            P[i/2] = point;
            System.out.print("(" + x + "," + y + ")");
            if (i < args.length - 2)
            {
                System.out.print(" , ");
            }
        }
        System.out.println("");
        Point [] pair = ClosestPair.findClosestPair(P);
        System.out.println("The closest pair is: (" + pair[0].x() + "," + pair[0].y() +
                ") and (" + pair[1].x() + "," + pair[1].y() + ")" );
    }
}

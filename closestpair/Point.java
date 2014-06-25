/**
 * $Id: Point.java, v 1.0 4/05/14 22:05 oscarfabra Exp $
 * {@code Point} Represents a point in the xy plane.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 4/05/14
 */

import java.util.List;

/**
 * Class that represents a point in the xy plane.
 */
public class Point
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    private int x;  // x coordinate
    private int y;  // y coordinate

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new point in the xy plane
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     * @param that Point object to copy.
     */
    public Point(Point that)
    {
        this.x = that.x;
        this.y = that.y;
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Tells whether this point has the same coordinates as the given point.
     * @param that The Point object to compare with.
     * @return Whether coordinates are the same.
     */
    public boolean equals(Point that)
    {
        return this.x == that.x && this.y == that.y;
    }

    /**
     * Returns the x value for this point.
     * @return The x value.
     */
    public int x()
    {
        return this.x;
    }

    /**
     * Returns the y value for this point.
     * @return The y value.
     */
    public int y()
    {
        return this.y;
    }

    /**
     * Copies the given Point.
     * @param that The Point object to copy.
     */
    public void copy(Point that)
    {
        this.x = that.x;
        this.y = that.y;
    }

    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Copies the array of points Q into the array of points P.
     * @param P Array of Point objects to copy into.
     * @param Q Array of Point objects to copy from.
     */
    public static void copyPoints(Point[] P, Point[] Q)
    {
        for(int i = 0; i < Q.length; i++)
        {
            if(P[i] == null)
            {
                P[i] = new Point(Q[i]);
            }
            else
            {
                P[i].copy(Q[i]);
            }
        }
    }

    /**
     * Returns an array with the X coordinates or the Y coordinates of the
     * given array of Point objects. </br>
     * <b>Pre: </b> coord = 'X' or coord = 'Y'
     * @param P Array of Point objects.
     * @return Array with the x or y coordinates of the given array of points.
     */
    public static int [] coordinates(Point [] P, char coord)
    {
        int [] coordinates = new int[P.length];
        for(int i = 0; i < P.length; i++)
        {
            coordinates[i] = (coord == 'X')? P[i].x : P[i].y;
        }
        return coordinates;
    }

    /**
     * Calculates the euclidean distance between two given points.
     * @param pair Array with 2 points, point1 in pair[0] and point2 in pair[1]
     * @return The euclidean distance.
     */
    public static double d(Point[] pair)
    {
        Point p = new Point(pair[0]);
        Point q = new Point(pair[1]);
        return d(p,q);
    }

    /**
     * Calculates the euclidean distance between two given points.
     * @param p Initial Point.
     * @param q Final Point.
     * @return The euclidean distance.
     */
    public static double d(Point p, Point q)
    {
        return Math.sqrt( Math.pow((p.x - q.x),2) + Math.pow((p.y - q.y), 2));
    }

    /**
     * Converts the given List of points into an array of points.
     * @param P List of Point objects.
     * @return Array of Point objects from the given list.
     */
    public static Point [] toArray(List<Point> P)
    {
        Point [] Q = new Point[P.size()];
        for(int i = 0; i < P.size(); i++)
        {
            Q[i] = new Point(P.get(i));
        }
        return Q;
    }
}

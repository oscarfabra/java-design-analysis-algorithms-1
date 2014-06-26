/**
 * $Id: Dijkstra.java, v 1.0 24/06/14 21:50 oscarfabra Exp $
 * {@code Dijkstra} Is an implementation of Dijkstra's shortest path
 * algorithm. <br/>
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 24/06/14
 */


import java.util.List;

/**
 * Class that implements Dijkstra's shortest path algorithm.
 */
public class Dijkstra
{
    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private Dijkstra(){}    // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves Dijkstra's shortest path algorithm starting on vertex with id s
     * in the given graph object, s in [1...n], where n is the number of
     * vertices in graph.
     * @param s Id of the starting vertex in graph.
     * @param graph Graph to search the shortest path for.
     * @return List of shortest distances from s to each of the other vertices.
     */
    public static List<Integer> solve(int s, Graph graph)
    {
        // TODO: Compute Dijkstra's algorithm...

        return null;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

}

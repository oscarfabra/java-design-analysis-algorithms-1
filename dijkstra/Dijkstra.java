/**
 * $Id: Dijkstra.java, v 1.0 24/06/14 21:50 oscarfabra Exp $
 * {@code Dijkstra} Is an implementation of Dijkstra's shortest path
 * algorithm. <br/>
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 24/06/14
 */


import java.util.*;

/**
 * Class that implements Dijkstra's shortest path algorithm.
 */
public class Dijkstra
{
    //-------------------------------------------------------------------------
    // CLASS ATTRIBUTES
    //-------------------------------------------------------------------------

    // List of vertices processed so far
    private static List<Integer> x;

    // List of computed shortest path distances from s to any other vertex of
    // the given graph
    private static int [] a;

    // Heap on which to store vertices not yet processed during execution
    private static PriorityQueue<Integer> heap;

    // Maps vertices with their corresponding heap keys
    private static Map<Integer,Integer> vertexHeapKey;

    // Maps heap keys with their corresponding vertex ids
    private static Map<Integer,Integer> heapKeyVertex;

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
    public static int [] solve(int s, Graph graph)
    {
        // Initializes the x list on which to store the ids of the vertices
        // processed so far, the path list a on which to store shortest path
        // distances from s to each other vertex, the heap on which to store
        // vertices not yet explored, and the vertexHeapKey and heapKeyVertex
        // arrays on which to store the key of each vertex in the heap
        int n = graph.getN();
        Dijkstra.x = new ArrayList<Integer>(n);
        Dijkstra.a = new int [n];
        Dijkstra.heap = new PriorityQueue<Integer>(n);
        Dijkstra.vertexHeapKey = new HashMap<Integer, Integer>(n);
        Dijkstra.heapKeyVertex = new HashMap<Integer, Integer>(n);

        // Assumes infinite distances between s and all the other vertices
        for(int i = 0; i < n; i++)
        {
            Dijkstra.a[i] = 1000000;
        }

        // Mark vertex s as processed and the distance to itself as 0
        Dijkstra.x.add(s);
        Dijkstra.a[s - 1] = 0;
        Dijkstra.vertexHeapKey.put(-1, s);
        Dijkstra.heapKeyVertex.put(s, -1);

        // Walks through each vertex not yet explored, calculates its key and
        // adds it to the vertexHeapKey and heapKeyVertex HashMaps
        for(int i = 1; i < n; i++)
        {
            int vertexScore = Dijkstra.minGreedyScore(i, graph);
            Dijkstra.heap.add(vertexScore);
            Dijkstra.vertexHeapKey.put(vertexScore, i + 1);
            Dijkstra.heapKeyVertex.put(i + 1, vertexScore);
        }

        // Walks through each vertex in the graph assigning the shortest path
        // from s to such vertex
        while(Dijkstra.x.size() < n)
        {
            // Extracts the key for the minimum path vertex not yet explored
            int wKey = Dijkstra.heap.poll();
            int wId = Dijkstra.vertexHeapKey.get(wKey);
            Dijkstra.x.add(wId);
            Dijkstra.a[wId - 1] = Dijkstra.heapKeyVertex.get(wId);

            // Updates key to the implicated edges (those whose tail is in X,
            // but their heads are in V - X)
            List<Edge> edgesLeaving = graph.getVertexEdgesLeaving(wId);
            for(Edge edge : edgesLeaving)
            {
                int vId = edge.getHead();
                int vKey = Dijkstra.heapKeyVertex.get(vId);
                if(!Dijkstra.x.contains(vId))
                {
                    // Removes head vertex with id vId from the heap, updates
                    // HashMaps accordingly
                    Dijkstra.heap.remove(vKey);
                    Dijkstra.vertexHeapKey.remove(vKey);
                    Dijkstra.heapKeyVertex.remove(vId);

                    // Recomputes the smalles greedy score for this vertex
                    int vScore = Math.min(vKey, a[wId - 1] + edge.getLength());

                    // Re-inserts vertex vId into the heap, updates HashMaps
                    // accordingly
                    Dijkstra.heap.add(vScore);
                    Dijkstra.vertexHeapKey.put(vScore, vId);
                    Dijkstra.heapKeyVertex.put(vId, vScore);
                }
            }
        }

        // Returns the computed array with shortest paths
        return a;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Computes the smallest greedy score of vertex with id vertexId in the
     * given graph. <br/>
     * @param vertexId Id of the not-yet-explored vertex.
     * @param graph Graph to examine.
     * @return Smallest greedy score of the given vertex.
     */
    private static int minGreedyScore(int vertexId, Graph graph)
    {
        // Obtains the edges that point to the vertex with the given id
        List<Edge> edgesArriving = graph.getVertexEdgesArriving(vertexId);
        // Finds the smallest greedy score of the given vertex
        int min = 1000000;
        for(Edge edge : edgesArriving)
        {
            min = Math.min(min,
                    Dijkstra.a[edge.getTail() - 1] + edge.getLength());
        }
        // Returns the smallest greedy score of the given vertex
        return min;
    }


}

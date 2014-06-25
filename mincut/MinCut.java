/**
 * $Id: MinCut.java, v 1.0 3/06/14 20:42 oscarfabra Exp $
 * {@code MinCut} Is a class that finds and counts the minimum cut of a given
 * graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 3/06/14
 */

/**
 * Class that finds and counts the minimum cut of a given graph.
 */
public class MinCut
{
    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    // This class should not be instantiated
    private MinCut(){}

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Determines the minimum cut for this graph running the random-contraction
     * algorithm a large number of times.
     * @param graph Graph to find a min cut for.
     * @return The number of crossing edges of the min cut found for the graph.
     */
    public static int findMinCut(Graph graph)
    {
        // Determines the number of times N that the random contraction
        // algorithm should be run as the square of the number of vertices.
        int n = graph.getN();
        int N = n*(n - 1) / 2;
        int i = 0;
        int minCut = N;
        Graph auxGraph = new Graph(graph);
        Graph minCutGraph = new Graph();
        System.out.println("Running the random contraction algorithm " + N +
                " times...");

        // runs the randomContraction algorithm N times
        while(i < N)
        {
            int cut = randomContraction(graph);
            if(cut <= minCut)
            {
                minCut = cut;
                minCutGraph.copy(graph);
            }
            System.out.println("Time " + (i + 1) + ", cut size: " + cut +
                    ", min cut so far: " + minCut + "\n");
            graph.copy(auxGraph);
            i++;
        }

        // Copies the min cut graph found
        graph.copy(minCutGraph);

        return minCut;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Runs the random-contraction algorithm to find a min cut of this graph.
     * @return Number of crossing edges of the resulting graph.
     */
    private static int randomContraction(Graph graph)
    {
        int i = 1;
        while(graph.getN() > 2)
        {
            Edge edge = pickRandomEdge(graph);
            int newId = graph.mergeVertices(edge);
            graph.removeSelfLoops(newId);
            System.out.print("Merged " + i);
            System.out.println((i > 1) ? " vertices..." : " vertex...");
            i++;
        }
        return graph.getM();
    }

    /**
     * Picks a random edge from the given graph.
     * @param graph Graph to find a min cut for.
     * @return Edge object from the given graph.
     */
    private static Edge pickRandomEdge(Graph graph)
    {
        int edgeIndex = (int) (Math.random() * graph.getM());
        Edge edge = graph.getEdgeByIndex(edgeIndex);
        return edge;
    }
}

/**
 * $Id: Connected.java, v 1.0 13/06/2014 18:31 oscarfabra Exp $
 * {@code Connected} Is a class used to compute the largest strongly connected
 * components of any given graph. <br/>
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 13/06/2014
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that finds the strongly connected components (SCCs) of a given graph.
 * @see Graph
 * @see Edge
 * @see Vertex
 */
public class Connected
{
    //-------------------------------------------------------------------------
    // CLASS ATTRIBUTES
    //-------------------------------------------------------------------------

    // Variable used for finishing times in the first pass of the depth-first
    // search (DFS)
    private static int t = 0;

    // Vertex used to know the leaders in the second pass of the depth-first
    // search (DFS)
    private static int s = 0;

    // Map to store the strongly-connected components (SCCs) as they are being
    // found. Keys are leader vertices. Values are lists of the ids of the
    // comprising vertices.
    private static Map<Integer, List<Integer>> sccs;

    // Auxiliary list used to store each of the SCCs that are being found to
    // store it in the sccs map above
    private static List<Integer> scc;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private Connected(){}   // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds and returns the five largest (or less if 5 not found) strongly
     * connected components (SCCs) of the given graph in descending order.
     * @param graph Graph to examine.
     * @return The five largest SCCs in the given graph, or the available ones
     * if there are less, in descending order.
     */
    public static List<Integer>[] findLargestSccs(Graph graph)
    {
        // Finds all SCCs of the given graph and returns a list with the ids of
        // each of the vertices that comprise them.
        System.out.println("Finding all SCCs...");
        findAllSccs(graph);
        System.out.println("...all SCCs found.");

        // Sorts the array of SCCs according to their respective sizes using
        // quicksort and returns an array of keys with the sorted order of
        // their lists' sizes in the sccs map
        System.out.print("Sorting discovered SCCs...");
        int [] keys = QuickSizes.sortSizes(Connected.sccs);
        System.out.println("done.");

        // Creates an array of lists and adds the 5 largest SCCs or the
        // available SCCs if there are less than 5. Take into account that,
        // keys.length = Connected.sccs.size()
        int n = (keys.length >= 5) ? 5 : keys.length;
        List<Integer>[] largestSccs = (LinkedList<Integer>[])new LinkedList[n];
        int ub = keys.length - 1;
        for(int i = ub, j = 0; j < n; i--, j++)
        {
            int key = keys[i];
            largestSccs[j] = Connected.sccs.get(key);
        }

        // Returns the respective array of lists that correspond to each scc
        return largestSccs;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds all the strongly connected components (SCCs) of the given graph
     * using Kosaraju's two-pass algorithm.
     * @param graph Graph to examine.
     * @return Array of lists of integers with the ids of the vertices that
     * comprise each of the SCCs found.
     */
    private static Map<Integer, List<Integer>> findAllSccs(Graph graph)
    {
        // Obtains a graph with the same nodes as graph but with its edges in
        // reverse order
        // System.out.println("-- Reversing graph...");
        // Graph graphRev = graph.reverseGraph();
        // System.out.println("-- graph reversed.");

        // Computes and sets the finishing times for each of the vertices in
        // the given graph using the reversed graph
        System.out.println("-- Computing magical ordering...");
        setMagicalOrdering(graph);
        System.out.println("-- ...finished computing magical ordering.");

        // Gets the SCCs one by one processing vertices in decreasing order of
        // finishing times
        System.out.println("-- Discovering SCCs...");
        Map<Integer, List<Integer>> sccs = discoverSccs(graph);
        System.out.println("-- ...finished discovering SCCs.");

        return sccs;
    }

    /**
     * Computes and sets the finishing times for each of the vertices in
     * graph using depth-first search in the reversed graph graphRev.
     * @param graph Graph to compute the finishing times for.
     */
    private static void setMagicalOrdering(Graph graph)
    {
        // Variable used to assign each vertex a finishing time
        Connected.t = 0;

        // Gets the finishing times for each vertex of graphRev
        System.out.println("---- Getting finishing times for each vertex in " +
                "graphRev...");
        for(int i = graph.getN(); i > 0; i--)
        {
            Vertex vertex = graph.getVertex(i);
            if(vertex != null && !vertex.isExplored())
            {
                DFSForFinishingTimes(graph, i);
            }
            // Shows a message, for logging purposes
            if((graph.getN() - i + 1) % 20000 == 0)
            {
                System.out.println("------ "+ (graph.getN() - i + 1) +
                        " finishing times setup.");
            }
        }
        System.out.println("---- ...finished getting times for vertices.");

        // Updates graph with the corresponding finishing times from graphRev
        /*System.out.print("---- Updating finishing times in graph...");
        for(int i = 1; i <= graph.getN(); i++)
        {
            int t = graphRev.getFinishingTime(i);
            graph.setFinishingTime(i, t);
        }
        System.out.println("done.");*/
    }

    /**
     * Gets the strongly connected components (SCCs) one-by-one processing
     * vertices in decreasing order of finishing times.
     * @param graph Graph to examine.
     * @return Array of lists with the ids of the vertices that comprise each
     * of the SCCs found.
     */
    private static Map<Integer, List<Integer>> discoverSccs(Graph graph)
    {
        // HashMap to add each of the SCCs. Keys corresponds to leader
        // vertices, values are the lists of ids of the comprising vertices.
        Connected.sccs = new HashMap<Integer, List<Integer>>();

        // Gets the finishing times for each vertex of graph
        for(int t = graph.getN(); t > 0; t--)
        {
            // Gets the vertex by its finishing time
            int vertexId = -1;
            Vertex vertex = graph.getVertexByFinishingTime(t);
            if(vertex != null)
            {
                vertexId = vertex.getId();
            }

            if(!vertex.isExplored())
            {
                // Initializes the scc that is about to be built
                Connected.scc = new LinkedList<Integer>();
                // Sets current vertex id as the leader vertex
                Connected.s = vertexId;
                // Finds each of the SCCs adding the corresponding vertices
                // to list Connected.scc
                DFSForFindingLeaders(graph, vertexId);
                // Adds only sccs of size at least 3, for performance issues.
                // Change if required.
                // if(Connected.scc.size() >= 3)
                // {
                    Connected.sccs.put(Connected.s, Connected.scc);
                    // Shows a message in standard output for logging purposes
                    System.out.print("---- "+ Connected.sccs.size());
                    System.out.println((Connected.sccs.size() > 1)? " SCCs found.":
                            " SCC found.");
                // }
            }
            // Shows a message for logging purposes
            if((graph.getN() - t + 1) % 100 == 0)
            {
                System.out.println("---- ["+ (graph.getN() - t + 1) +
                        " vertices covered so far.]");
            }
        }

        // Returns the constructed HashMap of SCCs
        return Connected.sccs;
    }

    /**
     * Runs depth-first search (DFS) backwards (crossing edges from head to
     * tail) on the given graph to set finishing times for its vertices. It's
     * run backwards to simulate a reverse version of given graph.
     * @param graph Graph to examine as if it were reversed.
     * @param vertexId Id of the vertex to start running DFS at.
     */
    private static void DFSForFinishingTimes(Graph graph, int vertexId)
    {
        // Sets the current vertex as explored
        graph.setVertexAsExplored(vertexId);
        // Walks through the adjacent vertices with depth-first search
        for(Vertex adjVertex : graph.getHeadVertices(vertexId))
        {
            if(adjVertex != null && !adjVertex.isExplored())
            {
                DFSForFinishingTimes(graph, adjVertex.getId());
            }
        }
        // Sets the finishing time for the vertex with id vertexId
        Connected.t++;
        graph.setFinishingTime(vertexId, t);
    }

    /**
     * Runs depth-first search (DFS) on the given graph to find the leader
     * vertices of each of the strongly-connected components (SCCs) and build
     * each of the SCCs.
     * @param graph Graph to examine.
     * @param vertexId Id of the vertex to start running DFS at.
     */
    private static void DFSForFindingLeaders(Graph graph, int vertexId)
    {
        // Sets the current vertex as explored
        graph.setVertexAsExplored(vertexId);
        //Walks through the adjacent vertices with depth-first search
        for(Vertex adjVertex : graph.getHeadVertices(vertexId))
        {
            if(adjVertex!= null && !adjVertex.isExplored())
            {
                DFSForFindingLeaders(graph, adjVertex.getId());
            }
        }
        // Adds current vertexId to the corresponding scc
        Connected.scc.add(vertexId);
    }
}

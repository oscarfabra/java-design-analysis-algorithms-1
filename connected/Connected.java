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

    // Stores the sizes of the 5 largest SCCs.
    private static int [] largestSccs;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private Connected(){}   // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the five largest strongly connected components (SCCs) of given
     * graph and returns their sizes in decreasing order.
     * @param graph Graph to examine.
     * @return Size of the largest 5 SCCs of the graph in decreasing order.
     */
    public static int [] findLargestSccs(Graph graph)
    {
        // Initializes array
        Connected.largestSccs = new int[5];

        // Finds all SCCs of the given graph and returns a list with the ids of
        // each of the vertices that comprise them.
        System.out.println("Finding all SCCs...");
        findAllSccs(graph);
        System.out.println("...all SCCs found.");

        // Returns the size of the 5 largest SCCs.
        return Connected.largestSccs;
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
        // Computes and sets the finishing times for each of the vertices in
        // the given graph using the reversed graph
        System.out.println("-- Computing magical ordering...");
        setMagicalOrdering(graph);
        System.out.println("-- ...finished computing magical ordering.");

        System.out.print("Updating vertices to unexplored...");
        for (int i= 1; i <= graph.getN(); i++)  // Assumes id's from 1 to n
        {
            graph.setVertexAsUnexplored(i);
        }
        System.out.println("done.");

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

                // Adds scc and updates largestSccs array
                Connected.sccs.put(Connected.s, Connected.scc);
                int size = Connected.scc.size();
                int i = 0;
                while(i < 5 && size < Connected.largestSccs[i]) { i++; }
                if(i < 5)
                {
                    for (int j = 4; j > i; j-- )
                    {
                        Connected.largestSccs[j] = Connected.largestSccs[j - 1];
                    }
                    Connected.largestSccs[i] = size;
                }

                // Shows a message in standard output for logging purposes
                System.out.print("---- "+ Connected.sccs.size());
                System.out.println((Connected.sccs.size() > 1)? " SCCs found.":
                        " SCC found.");
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
     * @param vId Id of the vertex to start running DFS at.
     */
    private static void DFSForFinishingTimes(Graph graph, int vId)
    {
        // Sets the current vertex as explored
        graph.setVertexAsExplored(vId);
        // Walks through the adjacent vertices using depth-first search
        for(Vertex adjVertex : graph.getTailVertices(vId))
        {
            if(adjVertex != null && !adjVertex.isExplored())
            {
                DFSForFinishingTimes(graph, adjVertex.getId());
            }
        }
        // Sets the finishing time for vId
        Connected.t++;
        graph.setFinishingTime(vId, t);
    }

    /**
     * Runs depth-first search (DFS) on the given graph to find the leader
     * vertices of each of the strongly-connected components (SCCs) and build
     * each of the SCCs.
     * @param graph Graph to examine.
     * @param vId Id of the vertex to start running DFS at.
     */
    private static void DFSForFindingLeaders(Graph graph, int vId)
    {
        // Sets the current vertex as explored
        graph.setVertexAsExplored(vId);
        //Walks through the adjacent vertices with depth-first search
        for(Vertex adjVertex : graph.getHeadVertices(vId))
        {
            if(adjVertex!= null && !adjVertex.isExplored())
            {
                DFSForFindingLeaders(graph, adjVertex.getId());
            }
        }
        // Adds current vId to the corresponding scc
        Connected.scc.add(vId);
    }
}

/**
 * $Id: Connected.java, v 1.0 13/06/2014 18:31 oscarfabra Exp $
 * {@code Connected} Is a class used to compute the largest strongly connected
 * components of any given graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 13/06/2014
 */

import java.util.*;

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

    // Map of arrays of size 2 used to get head vertices faster.
    // indices.get(i) returns an array v[] of size 2 containing:
    // v[0] => starting index in which to find mentions of vertex i + 1 in edges
    // v[1] => occurrences of vertex i + 1 in edges, assuming all of them
    // are found after index v[0] of edges.
    private static Map<Integer, int[]> indices;

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
        findAllSccs(graph);

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

    /**
     * Extracts the corresponding adjacency lists to initialize a new Graph
     * from the given list of edges. The input format must be a list of String,
     * each in the form "a b" where a is the tail and b the head of each edge,
     * a, b in [1...n]. <br/>
     * <b>Pre: </b> Any given starting point in edges a is found after the
     * first mention of a (i.e. there's no disorganized edge in the given list)
     * @param edges List of String, each with the head and tail of each edge.
     * @return Array of lists of Integers with the adjacent vertices of each
     * vertex.
     */
    public static Map<Integer, List<Integer>> buildAdjacencyLists(
            List<String> edges)
    {
        Connected.indices = new HashMap<Integer, int[]>();
        int tailId = 0;
        int edgeIndex = 0;
        int n = 0;

        // Stores the values from the given list into a matrix of vertex ids
        System.out.println("-- Initializing endPoints matrix...");
        int [][] endPoints = new int[edges.size()][2];
        for(int i = 0; i < edges.size(); i++)
        {
            String edge = edges.get(i);
            String[] vertices = edge.split(" ");
            endPoints[i][0] = Integer.parseInt(vertices[0]);
            endPoints[i][1] = Integer.parseInt(vertices[1]);

            // Operations done to build the corresponding indices arrays v
            if(tailId != endPoints[i][0])
            {
                // Adds a new value in the indices list
                if(i != 0)
                {
                    addEndPointsIndex(tailId, edgeIndex, n);
                }
                // Updates variables
                tailId++;
                edgeIndex = i;
                n = 0;
            }
            n++;
            // Message in standard output for logging purposes
            if((i + 1)%20000==0)
            {
                System.out.println("---- "+(i + 1)+" endPoints built.");
            }
        }
        // Adds the last value for the indices list of arrays
        addEndPointsIndex(tailId, edgeIndex, n);
        System.out.println("-- ...endPoints matrix initialized.");

        // Finds the number of vertices n as the max of the numbers in the list
        // assuming biggest number is in the last 20% of elements
        n = 0;
        System.out.print("-- Finding vertices number n...");
        for(int i = (endPoints.length / 5) * 4; i < endPoints.length; i++)
        {
            int v = Math.max(endPoints[i][0],endPoints[i][1]);
            n = Math.max(n, v);
        }
        System.out.println("done.");

        // Initializes the adjacency array of vectors
        System.out.print("-- Initializing hashmap of adjacency lists...");
        Map<Integer, List<Integer>> adj = new HashMap<Integer, List<Integer>>(n);
        System.out.println("done.");

        // Assigns each of the adjacency lists. adj[i] contains the adjacent
        // vertices of vertex i + 1, i in [0...n-1]
        for(int i = 0; i < n; i++)
        {
            adj.put(i + 1, buildAdjacencyList(i + 1, endPoints));
            // Message in standard output for logging purposes
            if((i + 1)%20000==0)
            {
                System.out.println("---- "+(i + 1)+" adjacency lists built.");
            }
        }
        return adj;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Adds a new array of size 2 with the inidices map for faster retrieval of
     * data from the endPoints matrix.
     * @param tailId Id of the tail vertex of the set of edges to add.
     * @param edgeIndex Index of a new set of edges with the same head vertex.
     * @param n Number of such edges following the one at position edgeIndex.
     */
    private static void addEndPointsIndex(int tailId, int edgeIndex, int n)
    {
        // Creates the new array v and adds it to the indices map
        int [] v = new int[2];
        v[0] = edgeIndex;
        v[1] = n;
        Connected.indices.put(tailId, v);
    }

    /**
     * Builds the adjacency list of a given vertex.
     * @param vertexId Number of the vertex to look for, vertexId in [1...n].
     * @param endPoints Matrix of integers; head and tail of each edge.
     * @return Adjacency list of vertex u.
     */
    private static List<Integer> buildAdjacencyList(int vertexId,
                                                    int[][] endPoints)
    {
        List<Integer> list = new Vector<Integer>();
        int [] v = Connected.indices.get(vertexId);
        for(int i = v[0]; i < v[0] + v[1]; i++)
        {
            if(endPoints[i][0] == vertexId)
            {
                list.add(endPoints[i][1]);
            }
        }
        return list;
    }

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
        System.out.print("Reversing graph...");
        Graph graphRev = reverseGraph(graph);
        System.out.println("done.");

        // Computes and sets the finishing times for each of the vertices in
        // the given graph using the reversed graph
        System.out.println("Computing magical ordering...");
        setMagicalOrdering(graph, graphRev);
        System.out.println("...finished computing magical ordering.");

        // Gets the SCCs one by one processing vertices in decreasing order of
        // finishing times
        System.out.println("Discovering SCCs...");
        Map<Integer, List<Integer>> sccs = discoverSccs(graph);
        System.out.println("...finished discovering SCCs.");

        return sccs;
    }

    /**
     * Creates a new Graph with the same vertices as the given graph but with
     * its edges reversed.
     * @param graph Graph to reverse the edges for.
     * @return A graph with the same vertices as the given graph but with its
     * edges reversed.
     */
    private static Graph reverseGraph(Graph graph)
    {
        Graph graphRev = new Graph(graph);

        // Creates a reversed edge for each edge of the given graph in E and
        // assigns it to graphRev in the corresponding position
        for(int i = 0; i < graph.getM(); i++)
        {
            Edge edge = graph.getEdgeByIndex(i);
            Edge edgeRev = new Edge(edge.getId(), edge.getHead(),
                    edge.getTail());
            graphRev.setEdgeByIndex(i, edgeRev);
        }
        return graphRev;
    }

    /**
     * Computes and sets the finishing times for each of the vertices in
     * graph using depth-first search in the reversed graph graphRev.
     * @param graph Graph to compute the finishing times for.
     * @param graphRev Graph with the same vertices as graph but with its edges
     *                 reversed.
     */
    private static void setMagicalOrdering(Graph graph, Graph graphRev)
    {
        // Variable used to assign each vertex a finishing time
        Connected.t = 0;

        // Gets the finishing times for each vertex of graphRev
        System.out.println("-- Getting finishing times for each vertex in " +
                "graphRev...");
        for(int i = graphRev.getN(); i > 0; i--)
        {
            if(!graphRev.getVertexByIndex(i - 1).isExplored())
            {
                DFSForFinishingTimes(graphRev, i);
            }
            // Shows a message for logging purposes
            if((graphRev.getN() - i + 1) % 20000 == 0)
            {
                System.out.println("---- "+ (graphRev.getN() - i + 1) +
                        " finishing times setup.");
            }
        }
        System.out.println("-- ...finished getting times for vertices.");

        // Updates graph with the corresponding finishing times from graphRev
        System.out.print("-- Updating finishing times in graph...");
        for(int i = 0; i < graphRev.getN(); i++)
        {
            int f = graphRev.getFinishingTime(i);
            graph.setFinishingTime(i, f);
        }
        System.out.println("done.");
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
            int vertexId = graph.getVertexByFinishingTime(t).getId();
            if(!graph.getVertexByIndex(vertexId - 1).isExplored())
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
                if(Connected.scc.size() >= 3)
                {
                    Connected.sccs.put(Connected.s, Connected.scc);
                    // Shows a message in standard output for logging purposes
                    System.out.print("---- "+ Connected.sccs.size());
                    System.out.println((Connected.sccs.size() > 1)? " SCCs found.":
                            " SCC found.");
                }
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
     * Runs depth-first search (DFS) on the given graph to set the finishing
     * times of each of its vertices given a vertexId to start at.
     * @param graphRev Graph to examine.
     * @param vertexId Id of the vertex to start running DFS at.
     */
    private static void DFSForFinishingTimes(Graph graphRev, int vertexId)
    {
        // Sets the current vertex as explored
        graphRev.setVertexAsExplored(vertexId);
        // Walks through the adjacent vertices with depth-first search
        for(Vertex adjVertex : graphRev.getHeadVertices(vertexId))
        {
            if(!adjVertex.isExplored())
            {
                DFSForFinishingTimes(graphRev, adjVertex.getId());
            }
        }
        // Sets the finishing time for the vertex with id vertexId
        Connected.t++;
        graphRev.setFinishingTime(vertexId - 1, t);
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
            if(!adjVertex.isExplored())
            {
                DFSForFindingLeaders(graph, adjVertex.getId());
            }
        }
        // Adds current vertexId to the corresponding scc
        Connected.scc.add(vertexId);
    }
}

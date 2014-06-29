/**
 * $Id: Graph.java, v 1.1 3/06/14 20:42 oscarfabra Exp $
 * {@code Graph} Represents a directed graph with n vertices and m edges.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.2
 * @since 3/06/14
 */

import java.util.*;

/**
 * Represents a directed graph with n vertices and m edges.
 */
public class Graph
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // Number of vertices, n = V.size()
    private int n;

    // Number of edges, m = E.size()
    private int m;

    // List of vertices
    private List<Vertex> V;

    // List of edges
    private List<Edge> E;

    // Map of lists of vertices that indicates endpoints of each vertex,
    // vertexEndpoints[i] contains the ids of the vertices to where the vertex
    // with id i + 1 points at, i in [0...n-1]
    private Map<Integer,List<Integer>> vertexEndpoints;

    // Finishing times of each vertex, finishingTimes.size() = n
    private List<Integer> finishingTimes;

    // Vertices for each finishing time, finishingVertices.size() = n
    private List<Integer> finishingVertices;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new graph from the given parameters.
     * <b>Pre: </b> The Vertex class has been initialized with Vertex.init()
     * @param n Number of vertices of the graph.
     * @param vertexEndpoints Map of Lists with the endpoints of each vertex.
     */
    public Graph(int n, Map<Integer, List<Integer>> vertexEndpoints)
    {
        // Initializes the list of vertices, O(n) algorithm
        this.n = n;
        int newVertexId = 1;
        this.V = new ArrayList<Vertex>(n);
        System.out.print("-- Initializing list of vertices V...");
        for(int i = 0; i < n; i++)
        {
            this.V.add(i, new Vertex(newVertexId++));
        }
        System.out.println("done.");

        // Initializes the list of edges and vertexEdgesIndices, O(n*m) algorithm
        this.m = 0;
        int newEdgeId = 1;
        this.E = new ArrayList<Edge>(n * 2);
        System.out.println("-- Initializing list of edges E...");
        for(int i = 0; i < n; i++)
        {
            // Walks through list vertexHeads assigning the respective head
            // vertices of vertex i + 1
            List<Integer> vertexHeads = vertexEndpoints.get(i + 1);
            for(int j = 0; j < vertexHeads.size(); j++)
            {
                Edge edge = new Edge(newEdgeId++, i + 1, vertexHeads.get(j));
                this.E.add(edge);
                this.m++;

                // Message in standard output for logging purposes
                if((newEdgeId % 20000) == 0)
                {
                    System.out.println("---- "+ newEdgeId +" edges setup.");
                }
            }
        }
        System.out.println("-- ...list of edges E initialized.");

        // Initializes the vertexEndpoints HashMap
        this.vertexEndpoints = new HashMap<Integer, List<Integer>>(this.n);
        for(Integer key : vertexEndpoints.keySet())
        {
            this.vertexEndpoints.put(key, vertexEndpoints.get(key));
        }

        // Initializes the finishingTimes list.
        this.finishingTimes = new ArrayList<Integer>(this.n);
        System.out.print("-- Initializing finishingTimes list...");
        for(int i = 0; i < n; i++)
        {
            this.finishingTimes.add(0);
        }
        System.out.println("done.");

        // Initializes the finishingVertices list.
        this.finishingVertices = new ArrayList<Integer>(this.n);
        System.out.print("-- Initializing finishingVertices list...");
        for(int i = 0; i < n; i++)
        {
            this.finishingVertices.add(0);
        }
        System.out.println("done.");
    }

    /**
     * Copy constructor.
     * @param that Graph to copy attributes from.
     */
    public Graph(Graph that)
    {
        copy(that);
    }

    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Extracts the corresponding adjacency lists to initialize a new Graph
     * from the given list of edges.
     * <b>Pre: </b>The input format must be a list of String, each in the form
     * "a b" where a is the tail and b the head of each edge, a, b in [1...n].
     * <br/>
     * @param edges List of String, each with the head and tail of each edge.
     * @return Array of lists of Integers with the adjacent vertices of each
     * vertex.
     */
    public static Map<Integer, List<Integer>> buildVertexEndpoints(
            List<String> edges)
    {
        // Walks through the given list of strings constructing the hashmap
        System.out.println("-- Initializing endPoints hashmap...");
        Map<Integer, List<Integer>> vertexEndpoints = new HashMap<Integer,
                List<Integer>>();
        for(int i = 0; i < edges.size(); i++)
        {
            // Extracts and adds the corresponding pair (key, value of list) to
            // the hashmap. Each pair (key, list) in the hashmap is the id of a
            // vertex, and the list of head vertices that come out from it
            String edge = edges.get(i);
            String[] vertices = edge.split(" ");
            int key = Integer.parseInt(vertices[0]);
            int value = Integer.parseInt(vertices[1]);
            Graph.addVertexEndpoint(vertexEndpoints, key, value);

            // Message in standard output for logging purposes
            if((i + 1) % 20000 == 0)
            {
                System.out.println("---- "+(i + 1)+" endPoints stored.");
            }
        }
        System.out.println("-- ...endPoints hashmap initialized.");

        return vertexEndpoints;
    }

    /**
     * Adds a key and value to the given map. The value is added to the chained
     * list of the given key.
     * @param vertexEndpoints Map to add the value to.
     * @param key Key of the list on which to add the given value.
     * @param value Value to add to the list of the given key in the map.
     */
    private static void addVertexEndpoint(Map<Integer, List<Integer>> vertexEndpoints,
                                          int key, int value)
    {
        // Removes the list of the given key, adds the value, and puts it
        // again
        List<Integer> headVertices = vertexEndpoints.remove(key);
        if(headVertices == null)
        {
            headVertices = new ArrayList<Integer>();
        }
        headVertices.add(value);
        vertexEndpoints.put(key, headVertices);
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Copies the attributes from the given graph to this graph.
     * @param that Graph to copy attributes from.
     */
    public void copy(Graph that)
    {
        // Copies the list of vertices
        this.n = that.n;
        this.V = new ArrayList<Vertex>(this.n);
        for(int i = 0; i < that.V.size(); i++)
        {
            this.V.add(new Vertex(that.V.get(i)));
        }

        // Copies the list of edges
        this.m = that.m;
        this.E = new ArrayList<Edge>(this.n * 2);
        for(int i = 0; i < that.E.size(); i++)
        {
            this.E.add(new Edge(that.E.get(i)));
        }

        // Initializes the vertexEndpoints HashMap
        this.vertexEndpoints = new HashMap<Integer, List<Integer>>(this.n);
        for(Integer key : that.vertexEndpoints.keySet())
        {
            this.vertexEndpoints.put(key,that.vertexEndpoints.get(key));
        }

        // Copies the finishingTimes list
        this.finishingTimes = new ArrayList<Integer>(that.finishingTimes);

        // Copies the finishingVertices list
        this.finishingVertices = new ArrayList<Integer>(that.finishingVertices);
    }

    /**
     * Gets the number of vertices n of this graph.
     * @return The number of vertices n.
     */
    public int getN()
    {
        return this.n;
    }

    /**
     * Gets the number of edges m of this graph.
     * @return The number of edges m.
     */
    public int getM()
    {
        return this.m;
    }

    /**
     * Returns the vertex at position vertexIndex in V.
     * @param vertexIndex Index of the vertex in list of vertices V.
     * @return The vertex in the given position.
     */
    public Vertex getVertexByIndex(int vertexIndex)
    {
        return this.V.get(vertexIndex);
    }

    /**
     * Gets an Edge from E by its index number.
     * @param edgeIndex Index number of the edge in E.
     * @return The Edge object.
     */
    public Edge getEdgeByIndex(int edgeIndex)
    {
        return this.E.get(edgeIndex);
    }

    /**
     * Sets the Edge from E by its index number.
     * @param edgeIndex Index number of the edge in E.
     * @param edge Edge object to put in the given position.
     */
    public void setEdgeByIndex(int edgeIndex, Edge edge)
    {
        this.E.set(edgeIndex, edge);
    }

    /**
     * Sets the vertex with the given id as explored in V.
     * @param vertexId Id of the vertex to look for.
     */
    public void setVertexAsExplored(int vertexId)
    {
        Vertex vertex = this.V.get(vertexId - 1);
        vertex.setExplored();
        this.V.set(vertexId - 1, vertex);
    }

    /**
     * Gets the finishing time of vertex with id vertexIndex + 1.
     * @param vertexIndex Index of the vertex in list finishingTimes.
     * @return Finishing time of the given vertex's index.
     */
    public int getFinishingTime(int vertexIndex)
    {
        return this.finishingTimes.get(vertexIndex);
    }

    /**
     * Sets the finishing time of vertex with id vertexIndex + 1.
     * @param vertexIndex Index of the vertex in list finishingTimes.
     * @param t Finishing time to assign to such vertex.
     */
    public void setFinishingTime(int vertexIndex, int t)
    {
        this.finishingTimes.set(vertexIndex, t);
        this.finishingVertices.set(t - 1, vertexIndex + 1);
    }

    /**
     * Finds and gets the vertex in V with the given finishing time.
     * @param t Finishing time to look for.
     * @return Vertex in V with the given finishing time.
     */
    public Vertex getVertexByFinishingTime(int t)
    {
        int vertexId = this.finishingVertices.get(t - 1);
        return this.V.get(vertexId - 1);
    }

    /**
     * Returns a list with the vertices to where the vertex with the given id
     * points at.
     * @param vertexId Id of the vertex to look for.
     * @return List of vertices to where the vertex with the given id points
     * at.
     */
    public List<Vertex> getHeadVertices(int vertexId)
    {
        // Walks through the list of vertices head of the vertex with the
        // given id
        List<Vertex> headVertices = new ArrayList<Vertex>((int)
                Math.ceil(this.n / 100));
        for(Integer wId : this.vertexEndpoints.get(vertexId))
        {
            headVertices.add(this.getVertexByIndex(wId - 1));
        }
        return headVertices;
    }
}

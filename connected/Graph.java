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

    // Map of vertices with (vertexId, Vertex) pairs
    private Map<Integer,Vertex> V;

    // List of edges
    private List<Edge> E;

    // Map of lists of vertices that indicates endpoints of each vertex
    private Map<Integer,List<Integer>> vertexEndpoints;

    // Finishing times of each vertex, finishingTimes.size() = n
    private Map<Integer,Integer> finishingTimes;

    // Vertices for each finishing time, finishingVertices.size() = n
    private Map<Integer,Integer> finishingVertices;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new graph from the given map of head vertices of each vertex.
     * @param vertexEndpoints Map of Lists with the endpoints of each vertex.
     */
    public Graph(Map<Integer, List<Integer>> vertexEndpoints)
    {
        // Initializes the map of vertices, O(n) algorithm
        this.V = new HashMap<Integer, Vertex>();
        System.out.print("-- Initializing list of vertices V...");
        for(Integer key : vertexEndpoints.keySet())
        {
            this.V.put(key, new Vertex(key));
        }
        this.n = this.V.size();
        System.out.println("done.");

        // Initializes the list of edges and vertexEdgesIndices, O(n*m) algorithm
        int newEdgeId = 1;
        this.E = new ArrayList<Edge>(n * 2);
        System.out.println("-- Initializing list of edges E...");
        for(Integer key : vertexEndpoints.keySet())
        {
            // Walks through list vertexHeads creating the appropriate edges
            for(Integer head : vertexEndpoints.get(key))
            {
                Edge edge = new Edge(newEdgeId++, key, head);
                this.E.add(edge);

                // Message in standard output for logging purposes
                if((newEdgeId % 20000) == 0)
                {
                    System.out.println("---- "+ newEdgeId +" edges setup.");
                }
            }
        }
        this.m = this.E.size();
        System.out.println("-- ...list of edges E initialized.");

        // Initializes the vertexEndpoints HashMap
        this.vertexEndpoints = new HashMap<Integer, List<Integer>>(this.n);
        for(Integer key : vertexEndpoints.keySet())
        {
            this.vertexEndpoints.put(key, vertexEndpoints.get(key));
        }

        // Initializes the finishingTimes list.
        this.finishingTimes = new HashMap<Integer, Integer>(this.n);

        // Initializes the finishingVertices list.
        this.finishingVertices = new HashMap<Integer, Integer>(this.n);
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
        this.V = new HashMap<Integer, Vertex>(this.n);
        for(Integer key : that.V.keySet())
        {
            this.V.put(key, that.V.get(key));
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
        this.finishingTimes = new HashMap<Integer, Integer>(this.n);
        this.finishingTimes.putAll(that.finishingTimes);

        // Copies the finishingVertices list
        this.finishingVertices = new HashMap<Integer, Integer>(this.n);
        this.finishingVertices.putAll(that.finishingVertices);
    }

    /**
     * Creates a new Graph with the same vertices as this graph but with its
     * edges reversed.
     * @return A graph with the same vertices as this graph but with its edges
     * reversed.
     */
    public Graph reverseGraph()
    {
        // Gets vertexEndpoints hashmap with reversed vertices
        Map<Integer,List<Integer>> vertexEndpoints =
                new HashMap<Integer, List<Integer>>(this.n);
        for(Integer key : this.vertexEndpoints.keySet())
        {
            for(Integer head : this.vertexEndpoints.get(key))
            {
                // Adds a new entry in the hashmap in reverse order
                Graph.addVertexEndpoint(vertexEndpoints, head, key);
            }
        }
        // Creates and returns the graph with its edges reversed
        return new Graph(vertexEndpoints);
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
     * Gets the vertex with the given id in V.
     * @param vertexId Id of the vertex to look for.
     * @return Vertex with the given id.
     */
    public Vertex getVertex(int vertexId)
    {
        if(!this.V.containsKey(vertexId))
        {
            return null;
        }
        return this.V.get(vertexId);
    }

    /**
     * Sets the vertex with the given id as explored in V.
     * @param vertexId Id of the vertex to look for.
     */
    public void setVertexAsExplored(int vertexId)
    {
        Vertex vertex = this.V.get(vertexId);
        vertex.setExplored();
        this.V.put(vertexId, vertex);
    }

    /**
     * Gets the finishing time of vertex with the given id.
     * @param vertexId Id of the vertex in map finishingTimes.
     * @return Finishing time of the given vertex.
     */
    public int getFinishingTime(int vertexId)
    {
        if(!this.finishingTimes.containsKey(vertexId))
        {
            return -1;
        }
        return this.finishingTimes.get(vertexId);
    }

    /**
     * Sets the finishing time of vertex with the given id.
     * @param vertexId Id of the vertex in list finishingTimes.
     * @param t Finishing time to assign to such vertex.
     */
    public void setFinishingTime(int vertexId, int t)
    {
        this.finishingTimes.put(vertexId, t);
        this.finishingVertices.put(t, vertexId);
    }

    /**
     * Finds and gets the vertex in V with the given finishing time.
     * @param t Finishing time to look for.
     * @return Vertex in V with the given finishing time.
     */
    public Vertex getVertexByFinishingTime(int t)
    {
        if(!this.finishingVertices.containsKey(t))
        {
            return null;
        }
        // Gets the id and returns the vertex from V
        int vertexId = this.finishingVertices.get(t);
        return this.V.get(vertexId);
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
        // Returns null if vertex with the given id doesn't have head vertices
        if(!this.vertexEndpoints.containsKey(vertexId))
        {
            return null;
        }
        // Walks through the list of vertices head of the vertex with the
        // given id
        List<Vertex> headVertices = new ArrayList<Vertex>();
        for(Integer wId : this.vertexEndpoints.get(vertexId))
        {
            headVertices.add(this.V.get(wId));
        }
        return headVertices;
    }

}

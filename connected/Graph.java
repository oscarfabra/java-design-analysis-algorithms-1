/**
 * $Id: Graph.java, v 1.1 3/06/14 20:42 oscarfabra Exp $
 * {@code Graph} Represents a directed graph with n vertices and m edges.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.2
 * @since 3/06/14
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Integer,Edge> E;

    // Maps vertices with the edges that leave from them
    private Map<Integer,List<Integer>> vertexEdgesLeaving;

    // Maps vertices with the edges that arrive at them
    private Map<Integer,List<Integer>> vertexEdgesArriving;

    // Finishing times of each vertex, finishingTimes.size() = n
    private Map<Integer,Integer> finishingTimes;

    // Vertices for each finishing time, finishingVertices.size() = n
    private Map<Integer,Integer> finishingVertices;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new graph from the given map of head vertices of each vertex.
     * @param verticesLeaving Maps vertices with vertices they point at.
     */
    public Graph(Map<Integer, List<Integer>> verticesLeaving)
    {
        // Initializes the map of vertices, O(n) algorithm
        this.V = new HashMap<Integer, Vertex>();
        System.out.print("-- Initializing list of vertices V...");
        for(Integer key : verticesLeaving.keySet())
        {
            this.V.put(key, new Vertex(key));
        }
        this.n = this.V.size();
        System.out.println("done.");

        // Initializes the list of edges and vertexEdges, O(m) algorithm
        int newEdgeId = 1;
        this.E = new HashMap<Integer, Edge>(this.n * 2);
        this.vertexEdgesLeaving = new HashMap<Integer, List<Integer>>(this.n);
        this.vertexEdgesArriving = new HashMap<Integer, List<Integer>>(this.n);
        System.out.println("-- Initializing list of edges E...");
        for(Integer key : this.V.keySet())
        {
            // Guarantees that there's a value for each key in V
            this.vertexEdgesLeaving.put(key, new ArrayList<Integer>());
            List<Integer> edgesIds = this.vertexEdgesArriving.remove(key);
            if(edgesIds == null) { edgesIds = new ArrayList<Integer>(); }
            this.vertexEdgesArriving.put(key, edgesIds);

            // Walks through list verticesLeaving creating the appropriate edges
            for(Integer head : verticesLeaving.get(key))
            {
                Edge edge = new Edge(newEdgeId, key, head);
                this.E.put(newEdgeId, edge);
                this.addVertexEdge(key, head, newEdgeId++);

                // Message in standard output for logging purposes
                if((newEdgeId % 20000) == 0)
                {
                    System.out.println("---- "+ newEdgeId +" edges setup.");
                }
            }
        }
        this.m = this.E.size();
        System.out.println("-- ...list of edges E initialized.");

        // Initializes the finishingTimes list.
        this.finishingTimes = new HashMap<Integer, Integer>(this.n);

        // Initializes the finishingVertices list.
        this.finishingVertices = new HashMap<Integer, Integer>(this.n);
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
     * @param verticesLeaving Maps vertices with vertices they point at.
     */
    public static void buildVertexEndpoints(List<String> edges, Map<Integer,
            List<Integer>> verticesLeaving)
    {
        // Walks through the given list of strings constructing the hashmap
        for(int i = 0; i < edges.size(); i++)
        {
            // Extracts and adds the corresponding pair (key, value of list) to
            // the hashmap. Each pair (key, list) in the hashmap is the id of a
            // vertex, and the list of head vertices that come out from it
            String edge = edges.get(i);
            String[] vertices = edge.split(" ");
            int key = Integer.parseInt(vertices[0]);
            int value = Integer.parseInt(vertices[1]);
            Graph.addVertexEndpoints(key, value, verticesLeaving);

            // Message in standard output for logging purposes
            if((i + 1) % 20000 == 0)
            {
                System.out.println("---- "+(i + 1)+" endPoints stored.");
            }
        }
    }

    /**
     * Adds vertices to the given maps.
     * @param v Id of one vertex endpoint.
     * @param w Id of another vertex endpoint.
     * @param verticesLeaving Maps vertices with its head vertices.
     */
    private static void addVertexEndpoints(
            int v,
            int w,
            Map<Integer, List<Integer>> verticesLeaving)
    {
        // Adds value to chained list of head vertices.
        List<Integer> headVertices = verticesLeaving.remove(v);
        if(headVertices == null) { headVertices = new ArrayList<Integer>(); }
        headVertices.add(w);
        verticesLeaving.put(v, headVertices);

        // Guarantees w has an associated list even if it's empty
        headVertices = verticesLeaving.remove(w);
        if(headVertices == null) { headVertices = new ArrayList<Integer>(); }
        verticesLeaving.put(w, headVertices);
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

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
     * Sets the vertex with the given id as explored.
     * @param vertexId Id of the vertex to look for.
     */
    public void setVertexAsExplored(int vertexId)
    {
        Vertex vertex = this.V.remove(vertexId);
        vertex.setExplored();
        this.V.put(vertexId, vertex);
    }

    /**
     * Sets the vertex with the given id as unexplored.
     * @param vertexId Id of the vertex to look for.
     */
    public void setVertexAsUnexplored(int vertexId)
    {
        Vertex vertex = this.V.remove(vertexId);
        vertex.removeExplored();
        this.V.put(vertexId, vertex);
    }

    /**
     * Gets the finishing time of vertex with the given id.
     * @param vId Id of the vertex in map finishingTimes.
     * @return Finishing time of the given vertex.
     */
    public int getFinishingTime(int vId)
    {
        if(!this.finishingTimes.containsKey(vId))
        {
            return -1;
        }
        return this.finishingTimes.get(vId);
    }

    /**
     * Sets the finishing time of vertex with the given id.
     * @param vId Id of the vertex in list finishingTimes.
     * @param t Finishing time to assign to such vertex.
     */
    public void setFinishingTime(int vId, int t)
    {
        this.finishingTimes.put(vId, t);
        this.finishingVertices.put(t, vId);
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
     * Returns a list with vertices to where vertex with given id points at.
     * @param vId Id of the vertex to look for.
     * @return List of vertices to where given vertex points at.
     */
    public List<Vertex> getHeadVertices(int vId)
    {
        // Walks through the list of vertices head of vertex with given id
        List<Vertex> headVertices = new ArrayList<Vertex>();
        for(Integer edgeId : this.vertexEdgesLeaving.get(vId))
        {
            int headId = this.E.get(edgeId).getHead();
            headVertices.add(this.V.get(headId));
        }
        return headVertices;
    }

    /**
     * Returns a list with tail vertices of given vertex.
     * @param vId Id of the vertex to look for.
     * @return List of tail vertices of given vertex.
     */
    public List<Vertex> getTailVertices(int vId)
    {
        // Walks through the list of tail vertices of vertex with given id
        List<Vertex> tailVertices = new ArrayList<Vertex>();
        for(Integer edgeId : this.vertexEdgesArriving.get(vId))
        {
            int tailId = this.E.get(edgeId).getTail();
            tailVertices.add(this.V.get(tailId));
        }
        return tailVertices;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Adds a new entry to vertexEdgesLeaving and vertexEdgesArriving hashmaps.
     * @param vId Id of one vertex.
     * @param wId Id of another vertex.
     * @param edgeId Id of the edge to add to the hashmaps.
     */
    private void addVertexEdge(Integer vId, Integer wId, int edgeId)
    {
        // Gets the list of edge's ids related to the given vertexId, adds the
        // new edgeId and puts it again in the hashmap.
        List<Integer> edgesIds = this.vertexEdgesLeaving.remove(vId);
        if(edgesIds == null) { edgesIds = new ArrayList<Integer>(); }
        edgesIds.add(edgeId);
        this.vertexEdgesLeaving.put(vId, edgesIds);

        // Does it again for the vertexEdgesArriving hashmap
        edgesIds = this.vertexEdgesArriving.remove(wId);
        if(edgesIds == null) { edgesIds = new ArrayList<Integer>(); }
        edgesIds.add(edgeId);
        this.vertexEdgesArriving.put(wId, edgesIds);
    }
}

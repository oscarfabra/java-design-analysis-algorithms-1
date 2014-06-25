/**
 * $Id: Graph.java, v 1.1 24/06/14 20:42 oscarfabra Exp $
 * {@code Graph} Represents a directed graph with n vertices and m edges for
 * computing Dijkstra's shortest path algorithm.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.3
 * @since 24/06/14
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a directed graph with n vertices and m edges for
 * computing Dijkstra's shortest path algorithm.
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

    // Endpoints of each edge in E
    private List<Integer> edgesEndpoints;

    // Edges incident on each vertex in V
    private List<Integer> vertexEdges;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new graph from the given parameters.
     * <b>Pre: </b> The Vertex class has been initialized with Vertex.init()
     * @param n Number of vertices of the graph.
     * @param adj Array of lists with the adjacent vertices, and edges' lengths
     *            starting from each vertex.
     */
    public Graph(int n, List<Integer>[] adj)
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
        this.E = new ArrayList<Edge>();

        // TODO: Initialize remaining lists...
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
        this.V = new ArrayList<Vertex>();
        for(int i = 0; i < that.V.size(); i++)
        {
            this.V.add(new Vertex(that.V.get(i)));
        }

        // Copies the list of edges
        this.m = that.m;
        this.E = new ArrayList<Edge>();
        for(int i = 0; i < that.E.size(); i++)
        {
            this.E.add(new Edge(that.E.get(i)));
        }

        // TODO: Copy remaining lists...
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
     * Returns a list with the vertices to where the vertex with the given id
     * points at.
     * @param vertexId Id of the vertex to look for.
     * @return List of vertices to where the vertex with the given id points
     * to.
     */
    public Vertex [] getHeadVertices(int vertexId)
    {
        // TODO: Get head vertices...

        return new Vertex[0];
    }
}

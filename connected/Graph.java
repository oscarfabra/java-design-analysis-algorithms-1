/**
 * $Id: Graph.java, v 1.1 3/06/14 20:42 oscarfabra Exp $
 * {@code Graph} Represents a directed graph with n vertices and m edges.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.2
 * @since 3/06/14
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
     * @param adj Map of Lists with the adjacent vertices of each vertex.
     */
    public Graph(int n, Map<Integer, List<Integer>> adj)
    {
        // Initializes the list of vertices, O(n) algorithm
        this.n = n;
        int newVertexId = 1;
        this.V = new Vector<Vertex>(n);
        System.out.print("-- Initializing list of vertices V...");
        for(int i = 0; i < n; i++)
        {
            this.V.add(i, new Vertex(newVertexId++));
        }
        System.out.println("done.");

        // Initializes the list of edges and vertexEdgesIndices, O(n*m) algorithm
        this.m = 0;
        int newEdgeId = 1;
        this.E = new Vector<Edge>();
        System.out.println("-- Initializing list of edges E...");
        for(int i = 0; i < n; i++)
        {
            // Walks through list vertexHeads assigning the respective head
            // vertices of vertex i + 1
            List<Integer> vertexHeads = adj.get(i + 1);
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

        // Initializes the finishingTimes Vector.
        this.finishingTimes = new Vector<Integer>(this.n);
        System.out.print("-- Initializing finishingTimes list...");
        for(int i = 0; i < n; i++)
        {
            this.finishingTimes.add(0);
        }
        System.out.println("done.");

        // Initializes the finishingVertices Vector.
        this.finishingVertices = new Vector<Integer>(this.n);
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
        this.V = new Vector<Vertex>();
        for(int i = 0; i < that.V.size(); i++)
        {
            this.V.add(new Vertex(that.V.get(i)));
        }

        // Copies the list of edges
        this.m = that.m;
        this.E = new Vector<Edge>();
        for(int i = 0; i < that.E.size(); i++)
        {
            this.E.add(new Edge(that.E.get(i)));
        }

        // Copies the finishingTimes list
        this.finishingTimes = new Vector<Integer>(that.finishingTimes);

        // Copies the finishingVertices list
        this.finishingVertices = new Vector<Integer>(that.finishingVertices);
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
     * to.
     */
    public Vertex [] getHeadVertices(int vertexId)
    {
        // Walks through the list of edges adjacent to the vertex and forms a
        // list with the head vertices of such edges
        Vertex [] adj = new Vertex[this.n];

        // TODO: Get head vertices...

        return adj;
    }
}

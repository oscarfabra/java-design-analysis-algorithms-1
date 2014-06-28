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

    // Each vertex in V points to its arriving edges
    private List<Integer>[] vertexEdgesArriving;

    // Each vertex in V points to its leaving edges
    private List<Integer>[] vertexEdgesLeaving;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new graph from the given parameters.
     * @param n Number of vertices of the graph.
     * @param edgesArray Array of lists of edges coming out from each vertex.
     */
    public Graph(int n, List<Edge>[] edgesArray)
    {
        // Initializes the list of vertices, and vertexEdgesArriving and
        // vertexEdgesLeaving arrays of lists, O(n) algorithm
        this.n = n;
        int newVertexId = 1;
        this.V = new ArrayList<Vertex>(this.n);
        this.vertexEdgesArriving = (ArrayList<Integer>[])new ArrayList[this.n];
        this.vertexEdgesLeaving = (ArrayList<Integer>[])new ArrayList[this.n];
        for(int i = 0; i < n; i++)
        {
            this.V.add(i, new Vertex(newVertexId++));
            this.vertexEdgesArriving[i] = new ArrayList<Integer>();
            this.vertexEdgesLeaving[i] = new ArrayList<Integer>();
        }

        // Initializes list of edges, and vertexEdgesArriving and
        // vertexEdgesLeaving array of lists, O(n*m) algorithm
        this.E = new ArrayList<Edge>();
        int i = 0;
        for(List<Edge> vertexEdges : edgesArray)
        {
            for(Edge edge : vertexEdges)
            {
                // Adds the edge to E
                this.E.add(edge);
                // Adds the edge's id to its corresponding list in
                // vertexEdgesArriving
                this.vertexEdgesArriving[edge.getHead() - 1].add(edge.getId());
                // Adds the edge's id to its corresponding list in
                // vertexEdgesLeaving
                this.vertexEdgesLeaving[i].add(edge.getId());
            }
            i++;
        }

        // Assigns m as the size of E
        this.m = this.E.size();
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
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Reads the given lines of String and gets an array of lists of edges.
     * <b>Pre:</b> There are exactly n lines; each of them contains a String of
     * the form "v1 v2,l2 v3,l3 ..." which indicates that vertex with id v1 has
     * an edge pointing to v2 of length l2, and an edge pointing to v3 of
     * length l3, and so on. <br/>
     * <b>Post:</b> edgesArray[i] contains the list of edges whose tail is
     * vertex with id i + 1, i in [0...n-1]
     * @param lines List of String with the values to get the edges from.
     * @return Array of lists of edges with edges coming out from each vertex.
     */
    public static List<Edge>[] readEdgesArray(List<String> lines)
    {
        // Initializes the edgesArray array of lists
        List<Edge>[] edgesArray = (ArrayList<Edge>[])new ArrayList[lines.size()];

        // Constructs the edgesArray array of lists of edges
        int i = 0;
        int newEdgeId = 1;
        for(String line : lines)
        {
            // Extracts the values of the edges from each line
            String[] values = line.split("\t");

            // Creates the new ArrayList in position i of the edgesArray array
            // of lists of edges and adds each of the edges
            edgesArray[i] = new ArrayList<Edge>(values.length - 1);
            for(int j = 1; j < values.length; j++)
            {
                String[] headAndLength = values[j].split(",");
                int headId = Integer.parseInt(headAndLength[0]);
                int length = Integer.parseInt(headAndLength[1]);
                Edge edge = new Edge(newEdgeId++, i + 1, headId, length);
                edgesArray[i].add(edge);
            }
            i++;
        }

        // Returns the constructed array of lists of edges
        return edgesArray;
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
        // Copies the list of vertices, vertexEdgesArriving and
        // vertexEdgesLeaving arrays of lists
        this.n = that.n;
        this.V = new ArrayList<Vertex>();
        this.vertexEdgesArriving = (ArrayList<Integer>[])new ArrayList[that.n];
        for(int i = 0; i < that.n; i++)
        {
            this.V.add(new Vertex(that.V.get(i)));
            this.vertexEdgesArriving[i] =
                    new ArrayList<Integer>(that.vertexEdgesArriving[i]);
            this.vertexEdgesLeaving[i] =
                    new ArrayList<Integer>(that.vertexEdgesLeaving[i]);
        }

        // Copies the list of edges
        this.m = that.m;
        this.E = new ArrayList<Edge>();
        for(int i = 0; i < that.m; i++)
        {
            this.E.add(new Edge(that.E.get(i)));
        }
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
     * Obtains and returns a list of the edges that point to the vertex with
     * the given id.
     * @param vertexId Id of the vertex to look for.
     * @return List of edges that point to the given vertex.
     */
    public List<Edge> getVertexEdgesArriving(int vertexId)
    {
        List<Edge> edgesArriving = new ArrayList<Edge>();
        for(Integer edgeId : this.vertexEdgesArriving[vertexId - 1])
        {
            edgesArriving.add(this.E.get(edgeId - 1));
        }
        return edgesArriving;
    }

    /**
     * Obtains and returns a list of the edges that come out from the vertex
     * with the given id.
     * @param vertexId Id of the vertex to look for.
     * @return List of edges that come out from the given vertex.
     */
    public List<Edge> getVertexEdgesLeaving(int vertexId)
    {
        List<Edge> edgesLeaving = new ArrayList<Edge>();
        for(Integer edgeId : this.vertexEdgesLeaving[vertexId - 1])
        {
            edgesLeaving.add(this.E.get(edgeId - 1));
        }
        return edgesLeaving;
    }
}

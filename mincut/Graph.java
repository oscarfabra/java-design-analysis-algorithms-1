/**
 * $Id: Graph.java, v 1.0 3/06/14 20:42 oscarfabra Exp $
 * {@code Graph} Represents an undirected graph with N nodes and E edges.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 3/06/14
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Represents an undirected graph with n vertices and m edges.
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

    // Each vertex point to the id of the edges incident on it
    private Map<Integer, List<Integer>> vertexEdges;

    // Indicates the initial number of vertices, for control purposes
    private int verticesNumber;

    // Identifier to assign to the next new Vertex
    private int newId;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates an empty new Graph.
     */
    public Graph()
    {
        this.n = 0;
        this.m = 0;
        this.V = new Vector<Vertex>();
        this.E = new Vector<Edge>();
        this.vertexEdges = new HashMap<Integer, List<Integer>>();
        this.verticesNumber = 0;
        this.newId = 1;
    }

    /**
     * Creates a new graph from the given parameters.
     * <b>Pre: </b> The Vertex class has been initialized with Vertex.init()
     * @param n Number of vertices of the graph.
     * @param adj Array of Lists with the adjacent vertices of each vertex.
     */
    public Graph(int n, List<Integer>[] adj)
    {
        // Initializes the list of vertices, O(n) algorithm
        this.n = n;
        this.verticesNumber = this.n;
        this.newId = 1;
        this.V = new Vector<Vertex>(n);
        for(int i = 0; i < n; i++)
        {
            this.V.add(i, new Vertex(this.newId++));
        }

        // Initializes the list of edges, O(n*m) algorithm
        this.m = 0;
        this.E = new Vector<Edge>();
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < adj[i].size(); j++)
            {
                if(!this.hasEdge(i + 1, adj[i].get(j)))
                {
                    Edge edge = new Edge(i + 1, adj[i].get(j));
                    this.E.add(this.m, edge);
                    this.m++;
                }
            }
        }

        // Initializes the vertexEdges HashMap, O(n*m) algorithm
        this.vertexEdges = new HashMap<Integer, List<Integer>>(n);
        for(int i = 0; i < n; i++)
        {
            this.vertexEdges.put(i + 1, this.getIncidentEdges(i + 1));
        }
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
        this.verticesNumber = that.verticesNumber;
        this.newId = that.newId;
        this.V = new Vector<Vertex>(that.V);

        // Copies the list of edges
        this.m = that.m;
        this.E = new Vector<Edge>(that.E);

        // Copies the vertexEdges list of lists
        this.vertexEdges =
                new HashMap<Integer, List<Integer>>(that.vertexEdges);
    }

    /**
     * Returns the edge with id edgeId.
     * @param edgeId Id of the edge to look for.
     * @return Edge object with the given id.
     */
    public Edge getEdge(int edgeId)
    {
        for(Edge edge : this.E)
        {
            if(edge.getId() == edgeId)
            {
                return edge;
            }
        }
        return null;
    }

    /**
     * Merges the vertices of the given ids.
     * @param edge The edge to look for and remove.
     * @return The number of the new created Vertex.
     */
    public int mergeVertices(Edge edge)
    {
        // Copies ids of initial and final vertices and removes edge from E
        int startId = edge.getStart();
        int endId = edge.getEnd();
        this.removeEdge(edge.getId());

        // Gets the adjacent edges' lists from the vertexEdges HashMap
        List<Integer> startEdges = this.removeFromVertexEdges(startId);
        List<Integer> endEdges = this.removeFromVertexEdges(endId);
        startEdges.addAll(endEdges);

        // Removes vertices from V and gets the new vertex
        Vertex start = this.removeVertex(startId);
        Vertex end = this.removeVertex(endId);
        Vertex newVertex = Vertex.mergeVertices(start, end, newId++, verticesNumber);

        // Adds the new vertex to the vertexEdges HashMap and to V
        this.vertexEdges.put(newVertex.getId(), startEdges);
        this.addVertex(newVertex);

        // Returns the id of the vertex that resulted from the merge
        return newVertex.getId();
    }

    /**
     * Removes the self loops of the given vertex, if any.
     * @param vertexId The id of the vertex to remove the loops from.
     */
    public void removeSelfLoops(int vertexId)
    {
        List<Integer> edgesIds = this.removeFromVertexEdges(vertexId);
        List<Integer> auxEdgesIds = new Vector<Integer>();
        List<Integer> edgesToRemove = new Vector<Integer>();

        // Adds only those edges that aren't self-loops
        for(Integer edgeId : edgesIds)
        {
            Edge edge = this.getEdge(edgeId);
            if(!isSelfLoop(vertexId, edge.getStart(), edge.getEnd()))
            {
                auxEdgesIds.add(edgeId);
            }
            else if(!edgesToRemove.contains(edgeId))
            {
                edgesToRemove.add(edgeId);
            }
        }

        // Updates the vertexEdges HashMap
        this.vertexEdges.put(vertexId, auxEdgesIds);

        // Removes from E edges that are self-loops
        for(Integer edgeId : edgesToRemove)
        {
            this.removeEdge(edgeId);
        }
    }

    /**
     * Gets the edges incident to the vertex corresponding to the given id.
     * @param vertexId Id of the vertex.
     * @return List of ids of the edges incident to the given vertex.
     */
    public List<Integer> getIncidentEdges(int vertexId)
    {
        List<Integer> aux = new Vector<Integer>();
        for(Edge edge : this.E)
        {
            if(edge.getStart() == vertexId || edge.getEnd() == vertexId)
            {
                aux.add(edge.getId());
            }
        }
        return aux;
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
     * Gets an Edge from E by its index number.
     * @param edgeIndex Index number of the Edge in E.
     * @return The Edge object.
     */
    public Edge getEdgeByIndex(int edgeIndex)
    {
        return this.E.get(edgeIndex);
    }

    /**
     * Returns a String representation to print in standard output.
     * <b>Pre: </b> The graph has only two vertices, this.V.size() = 2.
     * @return A String representation of this graph.
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        Vertex A = this.V.get(0);
        Vertex B = this.V.get(1);

        // Appends group A of the cut
        buffer.append("A = {");
        int n = A.getItems();
        for(int i = 0; i < n; i++)
        {
            buffer.append(A.getBagItem(i));
            buffer.append((i != n - 1)? ", " : "}" );
        }
        buffer.append("\n");

        // Appends group B of the cut
        buffer.append("B = {");
        n = B.getItems();
        for(int i = 0; i < n; i++)
        {
            buffer.append(B.getBagItem(i));
            buffer.append((i != n - 1)? ", " : "}" );
        }
        buffer.append("\n");

        return buffer.toString();
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Determines if the given edge already exists.
     * @param startId Id of the initial vertex.
     * @param endId Id of the final vertex.
     * @return Whether edge exists, taking into account that edges with equal
     * initial and final vertices are the same.
     */
    private boolean hasEdge(int startId, int endId)
    {
        for(Edge edge : this.E)
        {
            if(edge.getStart() == endId &&
                    edge.getEnd() == startId)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes edge with the given id from E and from vertexEdges.
     * @param edgeId Id of the edge to look for.
     */
    private void removeEdge(int edgeId)
    {
        // Removes the edge from E
        List<Edge> auxE = new Vector<Edge>();
        for(Edge edge : this.E)
        {
            if(edge.getId() != edgeId)
            {
                auxE.add(edge);
            }
        }
        this.E = auxE;
        this.m = this.E.size();

        // Removes the edge wherever it may be in vertexEdges
        for(Integer vertexId : this.vertexEdges.keySet())
        {
            List<Integer> adjEdges = this.vertexEdges.get(vertexId);
            List<Integer> auxAdjEdges = new Vector<Integer>();
            for(Integer id : adjEdges)
            {
                if(id != edgeId)
                {
                    auxAdjEdges.add(id);
                }
            }
            this.vertexEdges.put(vertexId, auxAdjEdges);
        }
    }

    /**
     * Returns the vertex that has or contains the given id.
     * @param vertexId Id of the vertex to look for.
     * @return Vertex object that has or contains the given id.
     */
    private Vertex getVertex(int vertexId)
    {
        for(Vertex vertex : this.V)
        {
            if(vertex.hasItem(vertexId))
            {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Adds the given vertex to V and updates n.
     * @param newVertex The vertex to add.
     */
    private void addVertex(Vertex newVertex)
    {
        this.V.add(newVertex);
        this.n = this.V.size();
    }

    /**
     * Gets the vertex with id vertexId from V and removes it.
     * @param vertexId Id of the vertex.
     * @return The Vertex with id vertexId.
     */
    private Vertex removeVertex(int vertexId)
    {
        List<Vertex> auxV = new Vector<Vertex>();
        Vertex auxVertex = null;
        for(Vertex vertex : this.V)
        {
            if(!vertex.hasItem(vertexId))
            {
                auxV.add(vertex);
            }
            else
            {
                auxVertex = new Vertex(vertex);
            }
        }
        // Updates V and n
        this.V = auxV;
        this.n = this.V.size();

        // Returns the found vertex
        return auxVertex;
    }

    /**
     * Determines whether there's a self-loop given the vertex id and an edge.
     * @param vertexId Id of the vertex.
     * @param startId Id of the initial vertex.
     * @param endId Id of the final vertex.
     * @return Whether there's a self-loop or not.
     */
    private boolean isSelfLoop(int vertexId, int startId, int endId)
    {
        Vertex vertex = this.getVertex(vertexId);
        return vertex.hasItem(startId) && vertex.hasItem(endId);
    }

    /**
     * Removes form vertexEdges the vertex that has or contains the given id.
     * @param vertexId Id of the vertex to look for.
     * @return The List of edges adjacent to such vertex.
     */
    private List<Integer> removeFromVertexEdges(int vertexId)
    {
        int auxId = this.getVertexEdges(vertexId);
        return this.vertexEdges.remove(Integer.valueOf(auxId));

    }

    /**
     * Gets the vertex from vertexEdges that has or contains the given id.
     * @param vertexId Id of the vertex to look for.
     * @return The id of the vertex in the vertexEdges HashMap.
     */
    private int getVertexEdges(int vertexId)
    {
        for(Integer auxId : this.vertexEdges.keySet())
        {
            Vertex vertex = this.getVertex(auxId);
            if(vertex != null && vertex.hasItem(vertexId))
            {
                return auxId;
            }
        }
        return 0;
    }
}

/**
 * $Id: Vertex.java, v 1.0 8/06/14 10:52 oscarfabra Exp $
 * {@code Vertex} Represents a Vertex for the Graph class.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 8/06/14
 */

import java.util.List;
import java.util.Vector;

/**
 * Class that represents a Vertex for the Graph class.
 * @see Graph
 * @see Edge
 */
public class Vertex
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // Identifies a vertex unequivocally
    private int id;

    // Number of items in this vertex, items = bag.size()
    private int items;

    // Collection of vertex ids
    private List<Integer> bag;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new Vertex assigning a unique identifier.
     */
    public Vertex(int newId)
    {
        this.id = newId;
        this.bag = new Vector<Integer>();
        this.items = 0;
    }

    /**
     * Copy constructor.
     * @param that Vertex to copy attributes from.
     */
    public Vertex(Vertex that)
    {
        this.id = that.id;
        this.bag = new Vector<Integer>(that.bag);
        this.items = that.items;
    }

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Merges the given vertices and returns the resulting vertex.
     * @param start Initial vertex.
     * @param end Final vertex.
     * @param newId Id to assign to the new vertex in the graph.
     * @param verticesNumber Initial number of vertices in the graph.
     * @return Vertex result from the merge.
     */
    public static Vertex mergeVertices(Vertex start, Vertex end, int newId,
                                       int verticesNumber)
    {
        Vertex newVertex = new Vertex(newId);
        newVertex.addItems(start, verticesNumber);
        newVertex.addItems(end, verticesNumber);
        return newVertex;
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Gets the id of this vertex.
     * @return Id of this vertex.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Adds all items of the given vertex to this vertex.
     * @param that Vertex to copy elements from.
     * @param verticesNumber Initial number of vertices in the graph.
     */
    public void addItems(Vertex that, int verticesNumber)
    {
        // Adds that's id only if its an original vertex, not a merged one
        if(that.id <= verticesNumber)
        {
            this.bag.add(that.id);
            this.items++;
        }
        // Adds items from the bag of that vertex
        for(Integer item : that.bag)
        {
            this.bag.add(item);
            this.items++;
        }
    }

    /**
     * Determines whether this vertex is or contains the given vertex id.
     * @param vertexId Id to search for.
     * @return Whether this vertex is or contains the given vertex id.
     */
    public boolean hasItem(int vertexId)
    {
        if(this.id == vertexId)
        {
            return true;
        }
        for(Integer item : bag)
        {
            if(item == vertexId){ return true; }
        }
        return false;
    }

    /**
     * Gets the item at position index in this vertex's bag.
     * @param index Position to look for in the bag, index in [0...items-1]
     * @return Item in position index of the bag.
     */
    public int getBagItem(int index)
    {
        return this.bag.get(index);
    }

    /**
     * Gets the number of items in this vertex's bag.
     * @return The number of items in this vertex.
     */
    public int getItems()
    {
        return this.items;
    }
}

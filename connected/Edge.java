/**
 * $Id: Edge.java, v 1.0 08/06/14 10:52 oscarfabra Exp $
 * {@code Edge} Represents an Edge in the Graph class containing the id of its
 * tail and head vertices. <br/>
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.1
 * @since 08/06/14
 */

/**
 * Class that represents an Edge in the Graph class containing the id of its
 * tail and head vertices. <br/>
 * @see Graph
 * @see Vertex
 */
public class Edge
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // Identifies the edge unequivocally
    private int id;

    // Id of tail vertex
    private int tail;

    // Id of head vertex
    private int head;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new Edge given its id, the id of its head vertex, and the id
     * of its tail vertex.
     * @param id Id to assign to this edge.
     * @param tail Id of the tail vertex.
     * @param head Id of the head vertex.
     */
    public Edge(int id, int tail, int head)
    {
        this.id = id;
        this.tail = tail;
        this.head = head;
    }

    /**
     * Copy constructor.
     * @param that Edge to copy attributes from.
     */
    public Edge(Edge that)
    {
        this.id = that.id;
        this.tail = that.tail;
        this.head = that.head;
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Gets the id of the tail vertex.
     * @return The id of the tail vertex.
     */
    public int getTail()
    {
        return this.tail;
    }

    /**
     * Gets the id of the head vertex.
     * @return The id of the head vertex.
     */
    public int getHead()
    {
        return this.head;
    }

    /**
     * Gets the id of this edge.
     * @return The id of this edge.
     */
    public int getId()
    {
        return this.id;
    }
}

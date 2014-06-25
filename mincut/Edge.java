/**
 * $Id: Edge.java, v 1.0 08/06/14 10:52 oscarfabra Exp $
 * {@code Edge} Represents an Edge in the Graph class containing the id of its
 * initial and final vertices. <br/>
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 08/06/14
 */

/**
 * Class that represents an Edge in the Graph class containing the id of its
 * initial and final vertices. <br/>
 * @see Graph
 * @see Vertex
 */
public class Edge
{
    //-------------------------------------------------------------------------
    // CLASS ATTRIBUTES
    //-------------------------------------------------------------------------

    // Identifier to assign to the next new Edge
    private static int newId = 1;

    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // Identifies the edge unequivocally
    private int id;

    // Id of initial vertex
    private int start;

    // Id of final vertex
    private int end;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new Edge with the ids of its initial and final vertices.
     * @param start Id of the initial vertex.
     * @param end Id of the final vertex.
     */
    public Edge(int start, int end)
    {
        this.id = Edge.newId++;
        this.start = start;
        this.end = end;
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Gets the id of the initial vertex.
     * @return The id of the initial vertex.
     */
    public int getStart()
    {
        return this.start;
    }

    /**
     * Gets the id of the final vertex.
     * @return The id of the final vertex.
     */
    public int getEnd()
    {
        return this.end;
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

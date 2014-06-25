/**
 * $Id: Pivot.java, v 1.0 31/05/14 21:57 oscarfabra Exp $
 * {@code Pivot} Enumeration that holds the possible pivots.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 31/05/14
 */

/**
 * Enumeration that holds the possible pivots.
 */
public enum Pivot
{
    FIRST,              // Pivots over the first element
    LAST,               // Pivots over the last element
    MEDIAN_OF_THREE     // Pivots using the median-of-three rule
}

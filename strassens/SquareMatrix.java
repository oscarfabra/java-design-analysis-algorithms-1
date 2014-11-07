/**
 * Class that represents a bi-dimensional matrix.
 *
 * @author Oscar Fabra, oscarfabra@gmail.com
 * @since 08/05/2014
 */
public class SquareMatrix
{
    //-------------------------------------------------------------------------
    // CONSTANTS
    //-------------------------------------------------------------------------

    public static final int UPPER_LEFT_CORNER = 1;
    public static final int UPPER_RIGHT_CORNER = 2;
    public static final int LOWER_LEFT_CORNER = 3;
    public static final int LOWER_RIGHT_CORNER = 4;

    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    private int [][] table;     // Integer matrix in which to store the values.
    private int n;              // Size of a side of the SquareMatrix.

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new SquareMatrix.
     * @param n Size of a side of the SquareMatrix.
     */
    public SquareMatrix(int n)
    {
        this.table = new int[n][n];
        this.n = n;
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Sets the specified element in the given indices.
     * @param i Row position, i in [0,1,...,n-1]
     * @param j Column position, j in [0,1,...,n-1]
     * @param element Integer to add to the SquareMatrix.
     */
    public void set(int i, int j, int element)
    {
        this.table[i][j] = element;
    }

    /**
     * Returns the element of this SquareMatrix aat the specified position.
     *
     * @param i Row position, i in [0,1,...,n-1]
     * @param j Column position, j in [0,1,...,n-1]
     * @return Integer in the given position.
     */
    public int get(int i, int j)
    {
        return this.table[i][j];
    }

    /**
     * Returns a given string with the corresponding values of this
     * SquareMatrix.
     *
     * @return A String with the values of this SquareMatrix to show in
     * standard output.
     */
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                buffer.append(this.table[i][j] + " ");
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }

    /**
     * Puts the values of the given square matrix in the corresponding place of
     * this matrix.
     *
     * @param a SquareMatrix whose values are going to be copied.
     * @param subSquareMatrix Where to put the values? (see class constants)
     */
    private void put(SquareMatrix a, int subSquareMatrix)
    {
        int row = 0;
        int col = 0;
        switch(subSquareMatrix)
        {
            case UPPER_LEFT_CORNER: row = 0; col = 0; break;
            case UPPER_RIGHT_CORNER: row = 0; col = a.n; break;
            case LOWER_LEFT_CORNER: row = a.n; col = 0; break;
            case LOWER_RIGHT_CORNER: row = a.n; col = a.n; break;
        }

        // Fills the sub-square matrix with the corresponding values of a
        for(int i = row; i < row + a.n; i++)
        {
            for(int j = col; j < col + a.n; j++)
            {
                this.table[i][j] = a.get(i-row,j-col);
            }
        }
    }

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Multiplies two given square matrices using Strassen's Sub-cubic
     * Multiplication Algorithm.
     *
     * @param x First SquareMatrix.
     * @param y Second SquareMatrix.
     * @return The SquareMatrix product of a and b.
     */
    public static SquareMatrix strassensAlgorithm(SquareMatrix x,
                                                  SquareMatrix y)
    {
        return strassensMultiplication(x,y, x.n);
    }

    //-------------------------------------------------------------------------
    // PRIVATE CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Recursively multiplies two given square matrices using Strassen's
     * Sub-cubic Multiplication Algorithm.
     *
     * @param x First SquareMatrix.
     * @param y Second SquareMatrix.
     * @param n Size of a side of a SquareMatrix.
     * @return The product SquareMatrix.
     */
    private static SquareMatrix strassensMultiplication(SquareMatrix x,
                                                        SquareMatrix y,
                                                        int n)
    {
        if(n == 2)
        {
            SquareMatrix m = new SquareMatrix(n);
            m.set(0,0, x.get(0,0)* y.get(0,0) + x.get(0,1)* y.get(1,0));
            m.set(0,1, x.get(0,0)* y.get(0,1) + x.get(0,1)* y.get(1,1));
            m.set(1,0, x.get(1,0)* y.get(0,0) + x.get(1,1)* y.get(1,0));
            m.set(1,1, x.get(1,0)* y.get(0,1) + x.get(1,1)* y.get(1,1));
            return m;
        }

        SquareMatrix a = subSquareMatrix(x, UPPER_LEFT_CORNER);
        SquareMatrix b = subSquareMatrix(x, UPPER_RIGHT_CORNER);
        SquareMatrix c = subSquareMatrix(x, LOWER_LEFT_CORNER);
        SquareMatrix d = subSquareMatrix(x, LOWER_RIGHT_CORNER);
        SquareMatrix e = subSquareMatrix(y, UPPER_LEFT_CORNER);
        SquareMatrix f = subSquareMatrix(y, UPPER_RIGHT_CORNER);
        SquareMatrix g = subSquareMatrix(y, LOWER_LEFT_CORNER);
        SquareMatrix h = subSquareMatrix(y, LOWER_RIGHT_CORNER);

        SquareMatrix p1 = strassensMultiplication(a,subtract(f,h),n/2);
        SquareMatrix p2 = strassensMultiplication(add(a,b),h,n/2);
        SquareMatrix p3 = strassensMultiplication(add(c,d),e,n/2);
        SquareMatrix p4 = strassensMultiplication(d,subtract(g,e),n/2);
        SquareMatrix p5 = strassensMultiplication(add(a,d),add(e,h),n/2);
        SquareMatrix p6 = strassensMultiplication(subtract(b,d),add(g,h),n/2);
        SquareMatrix p7 = strassensMultiplication(subtract(a,c),add(e,f),n/2);

        return computeProducts(p1,p2,p3,p4,p5,p6,p7,n);
    }

    /**
     * Computes the 7 corresponding products according to Strassen's Algorithm.
     *
     * @param p1 SquareMatrix resulting from the first product.
     * @param p2 SquareMatrix resulting from the second product.
     * @param p3 SquareMatrix resulting from the third product.
     * @param p4 SquareMatrix resulting from the fourth product.
     * @param p5 SquareMatrix resulting from the fifth product.
     * @param p6 SquareMatrix resulting from the sixth product.
     * @param p7 SquareMatrix resulting from the seventh product.
     * @return The adequate product of matrices x and y.
     */
    private static SquareMatrix computeProducts(SquareMatrix p1,
                                                SquareMatrix p2,
                                                SquareMatrix p3,
                                                SquareMatrix p4,
                                                SquareMatrix p5,
                                                SquareMatrix p6,
                                                SquareMatrix p7,
                                                int n)
    {
        SquareMatrix m = new SquareMatrix(n);
        m.put(subtract(add(add(p5,p4),p6),p2), UPPER_LEFT_CORNER);
        m.put(add(p1, p2), UPPER_RIGHT_CORNER);
        m.put(add(p3, p4), LOWER_LEFT_CORNER);
        m.put(subtract(subtract(add(p1,p5),p3),p7), LOWER_RIGHT_CORNER);
        return m;
    }

    /**
     * Returns a SquareMatrix from the given SquareMatrix.
     *
     * @param x The SquareMatrix to take the sub-square matrix from
     * @param subSquareMatrix Which square Matrix? (see the class constants)
     * @return The corresponding SquareMatrix object.
     */
    private static SquareMatrix subSquareMatrix(SquareMatrix x, int subSquareMatrix)
    {
        int row = 0;
        int col = 0;
        int r = (x.n % 2 == 1)? 1: 0;
        switch(subSquareMatrix)
        {
            case UPPER_LEFT_CORNER: row = 0; col = 0; break;
            case UPPER_RIGHT_CORNER: row = 0; col = x.n/2 + r; break;
            case LOWER_LEFT_CORNER: row = x.n/2 + r; col = 0; break;
            case LOWER_RIGHT_CORNER: row = x.n/2 + r; col = x.n/2 + r; break;
        }

        // Creates and fills the sub-square matrix with the corresponding
        // values of x
        SquareMatrix a = new SquareMatrix(x.n/2 + r);
        for(int i = row; i < row + x.n/2 + r; i++)
        {
            for(int j = col; j < col + x.n/2 + r; j++)
            {
                a.set(i - row, j - col, x.get(i, j));
            }
        }
        return a;
    }


    /**
     * Adds the two given SquareMatrices.
     *
     * @param x First SquareMatrix.
     * @param y Second SquareMatrix.
     * @return The resulting SquareMatrix.
     */
    private static SquareMatrix add(SquareMatrix x, SquareMatrix y)
    {
        int n = x.n;
        SquareMatrix m = new SquareMatrix(n);
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                m.set(i,j,x.get(i,j) + y.get(i,j));
            }
        }
        return m;
    }

    /**
     * Subtracts the two given SquareMatrices.
     *
     * @param x First SquareMatrix.
     * @param y Second SquareMatrix.
     * @return The resulting SquareMatrix.
     */
    private static SquareMatrix subtract(SquareMatrix x, SquareMatrix y)
    {
        int n = x.n;
        SquareMatrix m = new SquareMatrix(n);
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                m.set(i,j,x.get(i,j) - y.get(i,j));
            }
        }
        return m;
    }

}

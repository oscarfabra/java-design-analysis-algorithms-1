import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class that reads and solves a file with two square matrices using Strassen's
 * Sub-cubic Matrix Multiplication Algorithm.
 *
 * @author Oscar Fabra, oscarfabra@gmail.com
 * @since 08/05/2014
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Reads the lines from standard input and arranges them in a List.
     *
     * @param args Parameters for the matrix multiplication problem.
     * @return A List of lines with the data for the problem.
     * @throws IOException If the lines of the file couldn't be read.
     */
    public static List<String> readLines(String [] args) throws IOException
    {
        List<String> lines = new Vector<String>();

        String filename = null;

        // get the temp file name
        for(String arg : args)
        {
            if(arg.startsWith("-file="))
            {
                filename = arg.substring(6);
            }
        }

        if(filename == null)
        {
            return null;
        }

        // read the lines out of the file
        BufferedReader input = new BufferedReader(new FileReader(filename));

        try
        {
            String line = null;
            while((line = input.readLine()) != null)
            {
                lines.add(line);
            }
        }
        finally
        {
            input.close();;
        }

        return lines;
    }

    /**
     * Solves the instance and prints the solution in standard output.
     *
     * @param lines Input list with the variables of the problem.
     */
    public static void solve(List<String> lines)
    {
        // Parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");
        int n = Integer.parseInt(firstLine[0]);

        // Reads the first matrix
        SquareMatrix x = new SquareMatrix(n);
        readSquareMatrix(x, 0, n, lines);

        // Reads the second matrix
        SquareMatrix y = new SquareMatrix(n);
        readSquareMatrix(y, n, n, lines);

        // Multiplies the two square matrices using Strassen's Sub-cubic
        // Multiplication algorithm
        SquareMatrix z = SquareMatrix.strassensAlgorithm(x,y);

        // Prints the product in standard output
        System.out.println(z.toString());
    }

    /**
     * Reads the given list of lines and stores them in the given SquareMatrix.
     *
     * @param m SquareMatrix object to put the values into.
     * @param lb Initial row of lines to read.
     * @param n Size of the side of the square matrix.
     * @param lines Input list with the variables of the problem.
     */
    private static void readSquareMatrix(SquareMatrix m, int lb, int n,
                                         List<String> lines)
    {
        for(int i = lb; i < lb + n; i++)
        {
            String line = lines.get(i + 1);
            String [] row = line.split("\\s+");
            for(int j = 0; j < n ; j++)
            {
                m.set(i - lb, j, Integer.parseInt(row[j]));
            }
        }
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     *
     * @param args filepath relative to the program of the file with two square
     *             matrices of n x n dimensions. First row must contain n,
     *             followed by the n x n numbers of the first matrix, and then
     *             the n x n numbers of the second matrix.
     */
    public static void main(String [] args)
    {
        try
        {
            // Reads the lines from the standard input
            List<String> lines = Solver.readLines(args);

            Solver.solve(lines);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

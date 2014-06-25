import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class that reads and solves a file with the parameter for the array to count
 * the split inversions for.
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
     * @param args Parameters for the inversions problem.
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
            input.close();
        }

        return lines;
    }

    /**
     * Solves the instance and prints the solution in standard output.
     * @param lines Input list with the variables of the problem.
     */
    public static void solve(List<String> lines)
    {
        // List to store the values temporarily
        int [] a = new int[lines.size()];

        for(int i = 0; i < lines.size(); i++)
        {
            a[i] = Integer.parseInt(lines.get(i));
        }

        // Counts the number of split inversions
        long inversions = SplitInversions.sortAndCount(a);

        // Prints the answer in standard output
        System.out.println("The number of split inversions is: " + inversions);
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args filepath relative to the file with the list of numbers, in
     *             the form -file=filepath
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

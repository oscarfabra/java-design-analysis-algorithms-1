/**
 * $Id: Solver.java, v 1.0 22/06/14 21:01 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves a variant of the 2-SUM
 * problem.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 22/06/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class that reads and solves a file with the parameters for solving the 2-SUM
 * problem in a given interval of integers.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    // Lower-bound target value
    private static final int LOWER_BOUND = -10000;

    // Upper-bound target value
    private static final int UPPER_BOUND = 10000;

    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        // Gets an array of int from the given list of Strings
        int n = lines.size();
        long [] a = new long[n];
        for(int i = 0; i < n; i++)
        {
            try
            {
                a[i] = Long.parseLong(lines.get(i));
            }
            catch(NumberFormatException nfe)
            {
                System.out.println("Exception at line " + i + 1 + ": " +
                        nfe.getMessage());
            }
        }

        // Computes the number of target values t in the interval [LOWER_BOUND,
        // UPPER_BOUND] (inclusive) such that there are distinct numbers x,y in
        // a that satisfy x + y = t
        List<Long> list = TwoSum.getTwoSumDistinctNumbersInInterval(a,
                LOWER_BOUND, UPPER_BOUND);

        // Shows the size and numbers of the resulting array v
        show(list);
    }

    /**
     * Shows the length and each of the elements in the given array in
     * standard output.
     * @param list List to show the values for.
     */
    private static void show(List<Long> list)
    {
        System.out.print("The number of target values t in the interval [");
        System.out.println(LOWER_BOUND + "," + UPPER_BOUND + "] (inclusive) ");
        System.out.print("such that there are distinct numbers x,y in the ");
        System.out.print("input file that satisfy x + y = t is: ");
        System.out.println(list.size());
        System.out.println("Such numbers are the following: ");
        for(Long x : list)
        {
            System.out.println(x);
        }
    }

    /**
     * Reads the lines received from standard input and arranges them in a
     * list.
     * @param args Array of String with the filepath of the file to read.
     * @return A list of lines with the data for the problem.
     * @throws FileNotFoundException If the file couldn't be found.
     */
    private static List<String> readLines(String[] args)
            throws FileNotFoundException
    {
        List<String> lines = new Vector<String>();
        String filename = null;

        // get the file name
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

        // Reads the lines out of the file
        FileReader fileReader = new FileReader(filename);
        BufferedReader input = new BufferedReader(fileReader);
        String line = null;
        try
        {
            while((line = input.readLine()) != null)
            {
                lines.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return lines;
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args filepath relative to the file with the variables for the
     *             problem in the form -file=filepath
     */
    public static void main(String [] args)
    {
        List<String> lines = null;
        try
        {
            lines = Solver.readLines(args);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Solver.solve(lines);
    }
}

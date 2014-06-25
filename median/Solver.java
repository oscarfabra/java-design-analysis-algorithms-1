/**
 * $Id: Solver.java, v 1.0 22/06/14 21:01 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves a file with integers for the
 * "median maintenance" algorithm.
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
 * Class that reads and solves a file with the parameters for solving the
 * "median maintenance" algorithm.
 */
public class Solver
{
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
        int [] a = new int[n];
        for(int i = 0; i < n; i++)
        {
            try
            {
                a[i] = Integer.parseInt(lines.get(i));
            }
            catch(NumberFormatException nfe)
            {
                System.out.println("Exception at line " + i + 1 + ": " +
                        nfe.getMessage());
            }
        }

        List<Integer> medians = Median.getAllMedians(a, n);

        // Sums all the elements of the medians list
        int sum = 0;
        for(Integer median : medians)
        {
            sum += median;
        }

        // Shows the modulo 10000 of the sum (i.e., only the last 4 digits
        int mod = 10000;
        System.out.println("The sum of all the medians of the array is: "+sum);
        System.out.println("The sum modulo " + mod + " is: " + (sum % mod));
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


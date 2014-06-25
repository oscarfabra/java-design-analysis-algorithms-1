import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class that reads and solves a file with the parameters for the array to
 * sort in ascending order using quicksort.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Reads the lines from the standard input and arranges them in a List.
     * @param args Array of String with the filepath of the file to read.
     * @return A List of lines with the data for the problem.
     * @throws java.io.FileNotFoundException If the file couldn't be found.
     */
    public static List<String> readLines(String [] args) throws FileNotFoundException
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

        // reads the lines out of the file
        BufferedReader input = new BufferedReader(new FileReader(filename));
        String line = null;

        try
        {
            while((line = input.readLine())!= null)
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

    /**
     * Solves the instance and prints the solution in standard output.
     * @param lines lines Input list with the variables of the problem.
     */
    public static void solve(List<String> lines)
    {
        int [] a = new int[lines.size()];
        int [] aux = new int[lines.size()];

        for(int i = 0; i < lines.size(); i++)
        {
            a[i] = Integer.parseInt(lines.get(i));
            aux[i] = a[i];
        }

        // sorts and counts comparisons using the first element as pivot
        long comparisons = QuickSort.sortAndCountComparisons(a, Pivot.FIRST);
        System.out.println("Sorted array using first element as pivot: ");
        QuickSort.show(a);
        System.out.println("Number of comparisons: " + comparisons);
        System.out.println();

        // Stores again the arguments received by the main
        copy(a, aux);

        // Sorts and counts comparisons using the last element as pivot
        comparisons = QuickSort.sortAndCountComparisons(a, Pivot.LAST);
        System.out.println("Sorted array using last element as pivot: ");
        QuickSort.show(a);
        System.out.println("Number of comparisons: " + comparisons);
        System.out.println();

        // Stores again the arguments received by the main
        copy(a, aux);

        // Sorts and counts comparisons using the median-of-three pivoting rule
        comparisons = QuickSort.sortAndCountComparisons(a, Pivot.MEDIAN_OF_THREE);
        System.out.println("Sorted array using median-of-three elements as pivots: ");
        QuickSort.show(a);
        System.out.println("Number of comparisons: " + comparisons);
        System.out.println();
    }

    /**
     * Copies elements of integers array aux into a.
     * <b>Pre: </b>Booth arrays have the same length.
     * @param a Array of integers to copy into.
     * @param aux Array of integers to copy from.
     */
    private static void copy(int[] a, int[] aux)
    {
        for(int i = 0; i < a.length; i++)
        {
            a[i] = aux[i];
        }
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
            List<String> lines = null;
            // Reads the lines from the file
            lines = Solver.readLines(args);
            Solver.solve(lines);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

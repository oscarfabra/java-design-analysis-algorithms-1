/**
 * $Id: Solver.java, v 1.0 11/06/14 21:01 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves the problem of finding the
 * strongly connected components of a given directed graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 11/06/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Class that reads and solves a file with the parameters for computing
 * strongly connected components (SCCs) in a directed graph.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        System.out.println("Building endpoints list for each vertex...");
        Map<Integer,List<Integer>> vertexEndpoints =
                Graph.buildVertexEndpoints(lines);
        System.out.println("...finished building endpoints lists.");

        // Creates a new Graph
        System.out.println("Building new Graph object...");
        Graph graph = new Graph(vertexEndpoints);
        System.out.println("...new Graph object built.");

        // Finds its 5 largest SCCs (Strongly connected components)
        List<Integer>[] largestSccs = Connected.findLargestSccs(graph);

        // Shows the sizes of the largest SCCs
        System.out.println("The sizes of the largest strongly connected " +
                "components are: ");
        int i = 0;
        for(List<Integer> scc : largestSccs)
        {
            System.out.print(scc.size());
            System.out.print((i != largestSccs.length - 1)?", ":".");
            i++;
        }
        System.out.println();

        // Shows the actual vertices that comprise the obtained SCCs
        // show(largestSccs);
    }

    /**
     * Prints the size and vertices of the given strongly connected components
     * (SCCs). Each SCC corresponds to a list of integers with the vertices
     * that comprise it.
     * @param largestSccs Array of lists with the corresponding ids of the
     *                    vertices that comprise each SCC.
     */
    private static void show(List<Integer>[] largestSccs)
    {
        int k = 1;
        for(List<Integer> scc : largestSccs)
        {
            System.out.println("SCC " + k + ": ");
            System.out.print("-- Vertices: ");
            for(int i = 1; i <= scc.size(); i++)
            {
                System.out.print(scc.get(i - 1));
                System.out.print((i != scc.size())?", ":"\n");
            }
            k++;
        }
    }

    /**
     * Reads the lines received from standard input and arranges them in a
     * list.
     * @param args Array of String with the filepath of the file to read.
     * @return A list of lines with the data for the problem.
     * @throws FileNotFoundException If the file couldn't be found.
     */
    private static List<String> readLines(String[] args)  throws FileNotFoundException
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
     * @param args filepath relative to the file with the representation of a
     *             directed graph in the form -file=filepath
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

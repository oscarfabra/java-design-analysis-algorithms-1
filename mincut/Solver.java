/**
 * $Id: Solver.java, v 1.0 3/06/14 20:31 oscarfabra Exp $
 * {@code Solver} Reads and solves a file with the parameters of the graph for
 * which to find the minimum number of cuts.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 3/06/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Class that reads and solves a file with the parameters for the min cut graph
 * problem.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Reads the lines received from standard input and arranges them in a
     * List.
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

    /**
     * Solves the instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    public static void solve(List<String> lines)
    {
        // Converts the given List of String into an Array of List of Integers
        List<Integer>[] adjacencyList = toArrayOfLists(lines);

        // Finds the number of vertices V
        int V = lines.size();

        // Creates a new Graph and finds a min cut running the random
        // contraction algorithm a large number of times.
        Graph graph = new Graph(V, adjacencyList);
        int minCut = MinCut.findMinCut(graph);

        System.out.println("A min cut found from the given graph: ");
        System.out.print(graph.toString());

        System.out.println("The number of crossing edges is: " + minCut);
    }

    /**
     * Extracts the corresponding adjacency list from the given List of lines.
     * @param lines Input list with the variables for the problem.
     * @return Array of Lists of Integers with the adjacent vertices of each
     * vertex.
     */
    private static List<Integer>[] toArrayOfLists(List<String> lines)
    {
        // Initializes the adjacency Vector
        int V = lines.size();
        List<Integer> [] adj = (Vector<Integer>[]) new Vector[V];
        for(int i = 0; i < adj.length; i++)
        {
            adj[i] = new Vector<Integer>();
        }
        // Stores the values received in the List lines
        int i = 0;
        for(String line : lines)
        {
            String [] vertices = line.split("\t");
            // Adds adjacent vertices, without including the number of the
            // initial vertex.
            for(int j = 1; j < vertices.length; j++)
            {
                adj[i].add(Integer.valueOf(vertices[j]));
            }
            i++;
        }
        return adj;
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args filepath relative to the file with the representation of a
     *             simple undirected graph in the form -file=filepath
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

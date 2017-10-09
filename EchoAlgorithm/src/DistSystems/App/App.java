package DistSystems.App;

import DistSystems.Echo.EchoNode;
import DistSystems.Election.ElectionNodeImp;
import DistSystems.Interfaces.NodeAbstract;
import DistSystems.Utilities.GraphFileParser;
import DistSystems.Utilities.RandomGraphBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

/**
 * Entrypoint of app. Contains cli and starts algrorithms.
 * Created by Michael Guenster, Andre Schriever, Hendrik Mahrt on 09.10.2017.
 */
public class App {
    private List<NodeAbstract> nodes;
    private boolean fromFile = false;
    private String fileName = null;
    private boolean fromRandom = false;
    private Type algorithmType;

    public static void main(String[] args) {
        App app = new App();
        app.start(args);
    }

    private void start(String[] args) {
        if (args.length == 0)
            printUsage();

        parseArguments(args);
        generateGraph();

        printAllNodesNeighbours();
        startAllNodes();
        joinAllNodes();
    }

    private void generateGraph() {
        if (fromFile) {
            GraphFileParser fileParser = new GraphFileParser(algorithmType, fileName);
            try {
                nodes = fileParser.parse();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } else if (fromRandom) {

            int numberOfNodes;
            int numberOfInitiators = 1;
            int density;

            Scanner scanner = new Scanner(System.in);

            // ask for number of nodes
            numberOfNodes = scanInt(scanner, "Enter the number of nodes desired (min. 2)");
            numberOfNodes = numberOfNodes < 2 ? 2 : numberOfNodes;

            // ask for number of initiators, if election selected
            if (algorithmType == ElectionNodeImp.class) {
                numberOfInitiators = scanInt(scanner, "Enter number of initiators (min. 1)");
                numberOfInitiators = numberOfInitiators < 1 ? 1 : numberOfInitiators;
            }

            RandomGraphBuilder graphBuilder = new RandomGraphBuilder(
                    algorithmType,
                    numberOfNodes,
                    numberOfInitiators);

            // ask for complete graph
            if (scanBool(scanner, "Generate a complete graph? (y/n)"))
                graphBuilder.completeGraph();
            else {
                // if no: ask for density
                density = scanInt(scanner, "Enter the density of the graph (0-100)");
                double densityDouble = (double)density / 100.0;
                graphBuilder.setDensity(densityDouble);
                // if no: ask for slings allowed
                if (scanBool(scanner, "Allow slings? (y/n)"))
                    graphBuilder.allowSlings();
            }

            nodes = graphBuilder.build();
            scanner.close();
        } else {
            printErrorAndExit("Missing argument!");
        }
    }

    private boolean scanBool(Scanner scanner, String message) {
        System.out.println(message + ":");

        String result = "";
        if (scanner.hasNext())
            result = scanner.next();

        return result.equals("y");
    }

    private int scanInt(Scanner scanner, String message) {
        System.out.println(message + ":");

        int result = 0;
        if (scanner.hasNextInt())
            result = scanner.nextInt();

        return result;
    }

    private void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            final String val = i + 1 < args.length ? args[i+1] : null;
            parseAlgorithm(arg);
            parseSource(arg, val);
        }
    }

    private void parseSource(String arg, String val) {
        if (arg.equals("-f")) {
            if (fromRandom)
                printErrorAndExit("Only one type of source allowed");
            fromFile = true;
            fileName = val;
        } else if (arg.equals("-r")) {
            if (fromFile)
                printErrorAndExit("Only one type of source allowed");
            fromRandom = true;
        }
    }

    private void parseAlgorithm(String arg) {
        switch (arg) {
            case "--echo":
                if(algorithmType != null)
                    printErrorAndExit("Only one algorithmType allowed");
                algorithmType = EchoNode.class;
                break;
            case "--election":
                if(algorithmType != null)
                    printErrorAndExit("Only one algorithmType allowed");
                algorithmType = ElectionNodeImp.class;
                break;
            default:
                break;
        }
    }

    private void printAllNodesNeighbours() {
        nodes.forEach(node -> ((EchoNode)node).printNeighbours());
    }

    private void startAllNodes() {
        nodes.forEach(Thread::start);
    }

    private void joinAllNodes() {
        try {
            for (NodeAbstract node : nodes) {
                node.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printErrorAndExit(String message) {
        System.out.println("Error:" + message);
        System.exit(1);
    }

    private void printUsage() {
        System.out.println("Usage: \n");
        System.out.println("--echo \t\t performs echo algorithmType");
        System.out.println("--election \t performs echo/election algorithmType");
        System.out.println("\nuse file with graph config: \n");
        System.out.println("-f <file>");
        System.out.println("\nor generate random graph: \n");
        System.out.println("-r");

        System.out.println("\nMichael Guenster, Andre Schriever, Hendrik Mahrt");

        System.exit(1);
    }
}

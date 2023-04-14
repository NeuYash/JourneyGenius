package com.project;

import com.project.model.*;
import com.project.visualization.AlgorithmVisualization;
import com.project.visualization.GraphOperation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        System.out.println("Dataset with 585 points");

        Graph graph = getNodesFromDataset();
        graph.connectAllNodes();
        long startTime = System.currentTimeMillis();
        List<Edge> mst = Christofides.findMST(graph);
        long endTime = System.currentTimeMillis();
        double mstCost = 0.0;
        for (Edge e : mst) {
            mstCost += e.getDistance();
        }

        System.out.println("Kruskal MST cost : " + String.format("%.2f", mstCost) + " meters");
        System.out.println("Time taken for mst=" + (endTime - startTime) + " milliseconds");

        visualization(graph.getNodes(), Christofides.kruskalgos, null, 5, "Kruskal MST",
                "Kruskal MST cost : " + String.format("%.2f", mstCost) + " meters" + ", " + "Time taken for mst="
                        + (endTime - startTime) + " milliseconds",
                false);

        System.out.println();

        long startTime1 = System.currentTimeMillis();
        List<Node> oddDegrNodes = Christofides.findOddDegreeVertices(graph, mst);

        List<Edge> perfectMatchingEdges = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes);

        List<Node> eulerTour = Christofides.eulerTour(graph, mst, perfectMatchingEdges);

        List<Node> hamiltonCycle = Christofides.generateTSPTour(eulerTour);
        System.out.println("Hamiltonian cycle cost :"
                + Math.round(Christofides.calculateTourLength(hamiltonCycle) * 100.0) / 100.0 + " meters");
        long endTime1 = System.currentTimeMillis();
        System.out.println("Time taken for Christofides=" + ((endTime1 - startTime1)) + " milliseconds");
        System.out.println();
        visualization(graph.getNodes(), Christofides.calcGraphOperation(hamiltonCycle), Christofides.kruskalgos, 5,
                "Christofides Tour using Kruskal MST",
                "Hamiltonian cycle cost :" + Math.round(Christofides.calculateTourLength(hamiltonCycle) * 100.0) / 100.0
                        + " meters" + ", " + "Time taken for Christofides=" + ((endTime1 - startTime1))
                        + " milliseconds",
                false);

        // generate TSP using Random Swap optimization
        long startTime2 = System.currentTimeMillis();
        List<Node> randomTour = Christofides.randomSwapOptimise(hamiltonCycle, 30000);
        System.out.println("Random tour cost :"
                + Math.round(Christofides.calculateTourLength(randomTour) * 100.0) / 100.0 + " meters");
        long endTime2 = System.currentTimeMillis();
        System.out.println("Time taken for RandomTour=" + (endTime2 - startTime2) / 1000 + " seconds");
        visualization(graph.getNodes(), Christofides.calcGraphOperation(randomTour),
                Christofides.calcGraphOperation(hamiltonCycle), 5,
                "RandomSwapping using Christofides Tour with 30000 swaps",
                "Random tour cost :" + Math.round(Christofides.calculateTourLength(randomTour) * 100.0) / 100.0
                        + " meters" + ", " + "Time taken for RandomTour=" + (endTime2 - startTime2) / 1000 + " seconds",
                false);
        System.out.println();

        // generate TSP using 2 Opt Optimization
        long startTime3 = System.currentTimeMillis();
        List<Node> twoOptTour = Christofides.twoOpt(hamiltonCycle);
        System.out.println("Two Opt tour cost :"
                + Math.round(Christofides.calculateTourLength(twoOptTour) * 100.0) / 100.0 + " meters");
        long endTime3 = System.currentTimeMillis();
        System.out.println("Time taken for 2OPT=" + (endTime3 - startTime3) / 1000 + " seconds");
        visualization(graph.getNodes(), Christofides.calcGraphOperation(twoOptTour),
                Christofides.calcGraphOperation(hamiltonCycle), 5, "2Opt Optimization using Christofides Tour",
                "Two Opt tour cost :" + Math.round(Christofides.calculateTourLength(twoOptTour) * 100.0) / 100.0
                        + " meters" + ", " + "Time taken for 2OPT=" + (endTime3 - startTime3) / 1000 + " seconds",
                false);
        System.out.println();

        // simulated annealing
        long startTime8 = System.currentTimeMillis();
        List<Node> simulatedAnneTour = Christofides.simulatedAnnealingOptimizeTour(hamiltonCycle);
        System.out.println("Simu Anne tour cost :"
                + Math.round(Christofides.calculateTourLength(simulatedAnneTour) * 100.0) / 100.0 + " meters");
        long endTime8 = System.currentTimeMillis();
        System.out.println("Time taken for Simulated Annealing=" + (endTime8 - startTime8) / 1000 + " seconds");
        visualization(graph.getNodes(), Christofides.calcGraphOperation(simulatedAnneTour),
                Christofides.calcGraphOperation(hamiltonCycle), 5,
                "SimulatedAnnealing Optimization using Christofides Tour",
                "Simu Anne tour cost :"
                        + Math.round(Christofides.calculateTourLength(simulatedAnneTour) * 100.0) / 100.0 + " meters"
                        + ", " + "Time taken for Simulated Annealing=" + (endTime8 - startTime8) / 1000 + " seconds",
                false);
        System.out.println();

        // simulated annealing with 2Opt
        long startTime4 = System.currentTimeMillis();
        List<Node> simulatedAnneTourwith2Opt = Christofides.simulatedAnnealingOptimizeTour(twoOptTour);
        System.out.println("Simu Anne with 2Opt tour cost :"
                + Math.round(Christofides.calculateTourLength(simulatedAnneTourwith2Opt) * 100.0) / 100.0 + " meters");
        long endTime4 = System.currentTimeMillis();
        System.out.println(
                "Time taken for Simulated Annealing with 2OptTour=" + (endTime4 - startTime4) / 1000 + " seconds");

        visualization(graph.getNodes(), Christofides.calcGraphOperation(simulatedAnneTourwith2Opt),
                Christofides.calcGraphOperation(twoOptTour), 5, "SimulatedAnnealing Optimization using 2Opt Tour",
                "Simu Anne with 2Opt tour cost :"
                        + Math.round(Christofides.calculateTourLength(simulatedAnneTourwith2Opt) * 100.0) / 100.0
                        + " meters" + ", " + "Time taken for Simulated Annealing with 2OptTour="
                        + (endTime4 - startTime4) / 1000 + " seconds",
                false);

        System.out.println();

        // Genetic algo Optimization with Christofides Tour
        long startTime5 = System.currentTimeMillis();
        List<Node> genetictsp = TSPGenetic.TSPGenAlgo(hamiltonCycle);
        System.out.println("Genetic TSP with Chritofides tour cost :"
                + Math.round(Christofides.calculateTourLength(genetictsp) * 100.0) / 100.0 + " meters");
        long endTime5 = System.currentTimeMillis();
        System.out.println(
                "Time taken for Genetic Algo with Christofides tour=" + (endTime5 - startTime5) / 1000 + " seconds");

        visualization(graph.getNodes(), Christofides.calcGraphOperation(genetictsp),
                Christofides.calcGraphOperation(hamiltonCycle), 5, "Genetic Algo Optimization using Christofides Tour",
                "Genetic TSP with Christofides tour cost :"
                        + Math.round(Christofides.calculateTourLength(genetictsp) * 100.0) / 100.0 + " meters" + ", "
                        + "Time taken for Genetic Algo with Christofides tour=" + (endTime5 - startTime5) / 1000
                        + " seconds",
                true);

        System.out.println();

        // Genetic algo Optimization with 2OPT Tour
        long startTime6 = System.currentTimeMillis();
        List<Node> genetictspwith2Opt = TSPGenetic.TSPGenAlgo(twoOptTour);
        System.out.println("Genetic TSP with 2Opt tour cost :"
                + Math.round(Christofides.calculateTourLength(genetictspwith2Opt) * 100.0) / 100.0 + " meters");
        long endTime6 = System.currentTimeMillis();
        System.out.println("Time taken for Genetic Algo with 2OPT tour=" + (endTime6 - startTime6) / 1000 + " seconds");

        visualization(graph.getNodes(), Christofides.calcGraphOperation(genetictspwith2Opt),
                Christofides.calcGraphOperation(twoOptTour), 5, "Genetic Algo Optimization using 2OPT Tour",
                "Genetic TSP with 2OPT tour cost :"
                        + Math.round(Christofides.calculateTourLength(genetictspwith2Opt) * 100.0) / 100.0 + " meters"
                        + ", " + "Time taken for Genetic Algo with 2OPT tour=" + (endTime6 - startTime6) / 1000
                        + " seconds",
                true);

        System.out.println();

        // Genetic algo Optimization with SimulatedAnnealing Tour
        long startTime7 = System.currentTimeMillis();
        List<Node> genetictspwithSA = TSPGenetic.TSPGenAlgo(simulatedAnneTour);
        System.out.println("Genetic TSP with SimulatedAnnealing tour cost :"
                + Math.round(Christofides.calculateTourLength(genetictspwithSA) * 100.0) / 100.0 + " meters");
        long endTime7 = System.currentTimeMillis();
        System.out.println("Time taken for Genetic Algo with SimulatedAnnealing tour=" + (endTime7 - startTime7) / 1000
                + " seconds");

        visualization(graph.getNodes(), Christofides.calcGraphOperation(genetictspwithSA),
                Christofides.calcGraphOperation(simulatedAnneTour), 5,
                "Genetic Algo Optimization using SimualatedAnnealing Tour",
                "Genetic TSP with SimulatedAnnealing tour cost :"
                        + Math.round(Christofides.calculateTourLength(genetictspwithSA) * 100.0) / 100.0 + " meters"
                        + ", " + "Time taken for Genetic Algo with SimulatedAnnealing tour="
                        + (endTime7 - startTime7) / 1000 + " seconds",
                true);

        System.out.println();

        // Genetic algo Optimization with SimulatedAnnealing Tour
        long startTime9 = System.currentTimeMillis();
        List<Node> genetictspwithSA2Opt = TSPGenetic.TSPGenAlgo(simulatedAnneTourwith2Opt);
        System.out.println("Genetic TSP with SimulatedAnnealing tour(done on 2OPT tour) cost :"
                + Math.round(Christofides.calculateTourLength(genetictspwithSA) * 100.0) / 100.0 + " meters");
        long endTime9 = System.currentTimeMillis();
        System.out.println("Time taken for Genetic Algo with SimulatedAnnealing tour(done on 2Opt tour)="
                + (endTime9 - startTime9) / 1000 + " seconds");

        visualization(graph.getNodes(), Christofides.calcGraphOperation(genetictspwithSA2Opt),
                Christofides.calcGraphOperation(simulatedAnneTourwith2Opt), 5,
                "Genetic Algo Optimization using SimualatedAnnealing Tour(done on 2OPT tour)",
                "Genetic TSP with SimulatedAnnealing tour(done on 2OPT tour) cost :"
                        + Math.round(Christofides.calculateTourLength(genetictspwithSA) * 100.0) / 100.0 + " meters"
                        + ", " + "Time taken for Genetic Algo with SimulatedAnnealing tour(done on 2OPT tour)="
                        + (endTime9 - startTime9) / 1000 + " seconds",
                true);

        System.out.println();

        // 3OPT
//		List<Node> threeOptTour = Christofides.ThreeOptChristofides(hamiltonCycle);
//		System.out.println("TSP using three Opt :" + threeOptTour.size());
//		System.out.println("three Opt tour cost :" + Christofides.calculateTourLength(threeOptTour));
//		List<Node> threeOptTour = Christofides.threeopt(hamiltonCycle);
//		System.out.println("TSP using three Opt :" + threeOptTour.size());
//		System.out.println("three Opt tour cost :" + Christofides.calculateTourLength(threeOptTour));

        // K-Opt
//        long startTime10 = System.currentTimeMillis();
//        List<Node> KOptTour = Christofides.kOpt(hamiltonCycle, 5);
//        System.out.println("K-Opt tour cost :" + Math.round(Christofides.calculateTourLength(KOptTour) * 100.0) / 100.0 + " meters");
//        long endTime10 = System.currentTimeMillis();
//        System.out.println("Time taken for K-Opt =" + (endTime10 - startTime10) / 1000 + " seconds");
//        visualization(graph.getNodes(), Christofides.calcGraphOperation(KOptTour),
//                Christofides.calcGraphOperation(hamiltonCycle), 5, "K-Opt using Christofides Tour",
//                "K-Opt tour cost :" + Math.round(Christofides.calculateTourLength(KOptTour) * 100.0) / 100.0 + " meters"
//                + ", " + "Time taken for K-Opt=" + (endTime10 - startTime10) / 1000 + " seconds",false);
//        System.out.println();

        // AntColony
//        long startTime11 = System.currentTimeMillis();
//        List<Node> ACOTour = Christofides.aCOpt(hamiltonCycle);
//        System.out.println("Ant Colony tour cost :" + Math.round(Christofides.calculateTourLength(ACOTour) * 100.0) / 100.0 + " meters");
//        long endTime11 = System.currentTimeMillis();
//        System.out.println("Time taken for Ant Colony=" + (endTime11 - startTime11) / 1000 + " seconds");
//        visualization(graph.getNodes(), Christofides.calcGraphOperation(ACOTour),
//                Christofides.calcGraphOperation(hamiltonCycle), 5, "AntColony Optimization using Christofides Tour",
//                "Ant Colony tour cost :" + Math.round(Christofides.calculateTourLength(ACOTour) * 100.0) / 100.0 + " meters"
//                + ", " + "Time taken for Ant Colony=" + (endTime11 - startTime11) / 1000 + " seconds",false);
//        System.out.println();
    }

    public static Graph getNodesFromDataset() {
        Graph graph = new Graph();
        String line = "";
        String splitBy = ",";
        // Set<String> uniqueNodes = new HashSet<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("info6205.spring2023.teamproject.csv"));
            reader.readLine();
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                String[] node = line.split(splitBy);
                if (node.length > 0 && !(node[0].isEmpty() || node[1].isEmpty() || node[2].isEmpty())) {
                    String nodeKey = node[1] + "," + node[2];
//					if (!uniqueNodes.contains(nodeKey)) {
//						uniqueNodes.add(nodeKey);
                    Node n = new Node(node[0].substring(node[0].length() - 6), Double.parseDouble(node[1]),
                            Double.parseDouble(node[2]));
                    graph.addNode(n);
                    // }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void visualization(List<Node> nodes, List<GraphOperation> gos, List<GraphOperation> oldGos,
                                     int interval, String GraphTitle, String GraphDes, boolean flag) {
        AlgorithmVisualization av;
        // Build av
        if (oldGos != null) {
            av = new AlgorithmVisualization(nodes, gos, oldGos);
        } else {
            av = new AlgorithmVisualization(nodes, gos);
        }
        // You can set sleep time
        // default: 500
        av.setSleepTime(interval);
        av.showResult(GraphTitle, GraphDes,flag);
    }

    protected static void sleep() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

    public static void printGraph(List<Node> nodes) {

        for (Node n : nodes) {
            System.out.print(n.getLatitude() + "," + n.getLongitude() + " -> ");
        }
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6378; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return Math.sqrt(distance);
    }
}
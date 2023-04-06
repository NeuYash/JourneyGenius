package com.project;

import com.project.model.Christofides;
import com.project.model.Edge;
import com.project.model.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.model.Node;

public class App {

    public static Graph getNodesFromDataset() {
        Graph graph = new Graph();
        String line = "";
        String splitBy = ",";
        //Set<String> uniqueNodes = new HashSet<String>();
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
                    Node n = new Node(node[0], Double.parseDouble(node[1]), Double.parseDouble(node[2]));
                    graph.addNode(n);
                    //}
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void main(String[] args) {
        System.out.println("Dataset with 585 points");
        Graph graph = getNodesFromDataset();
        graph.connectAllNodes();
        List<Edge> mst = Christofides.findMST(graph);

        List<Node> oddDegrNodes = Christofides.findOddDegreeVertices(graph, mst);

        List<Edge> perfectMatchingEdges = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes);

        List<Node> eulerTour = Christofides.eulerTour(graph, mst, perfectMatchingEdges);

        List<Node> hamiltonCycle = Christofides.generateTSPTour(eulerTour);
        System.out.println("Hamiltonian cycle cost :" + Christofides.calculateTourLength(hamiltonCycle));

        // generate TSP using Random Swap optimization
        List<Node> randomTour = Christofides.randomSwapOptimise(hamiltonCycle, 20000);
        System.out.println("Random tour cost :" + Christofides.calculateTourLength(randomTour));

        //generate TSP using 2 Opt Optimization
        List<Node> twoOptTour = Christofides.twoOpt(hamiltonCycle);
        System.out.println("Two Opt tour cost :" + Christofides.calculateTourLength(twoOptTour));

        //3 OPT
//		List<Node> threeOptTour = Christofides.threeOpt(hamiltonCycle);
//		System.out.println("TSP using three Opt :" + threeOptTour.size());
//		System.out.println("three Opt tour cost :" + Christofides.calculateTourLength(threeOptTour));
//
//        List<Node> threeOptChristofides = Christofides.threeOptChristofides(hamiltonCycle);
//        System.out.println("TSP using three Opt :" + threeOptChristofides.size());
//        System.out.println("three Opt Christofides tour cost :" + Christofides.calculateTourLength(threeOptChristofides));
    }
}

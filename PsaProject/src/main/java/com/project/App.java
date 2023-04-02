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

    public static Graph getNodesFromcrimeDataset() {
        Graph graph = new Graph();
        String line = "";
        String splitBy = ",";
        Set<String> uniqueNodes = new HashSet<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("crimeSample.csv"));
            reader.readLine();
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                String[] node = line.split(splitBy);
                if (node.length > 0 && !(node[0].isEmpty() || node[1].isEmpty() || node[2].isEmpty())) {
                    String nodeKey = node[1] + "," + node[2];
                    if (!uniqueNodes.contains(nodeKey)) {
                        uniqueNodes.add(nodeKey);
                        Node n = new Node(node[0], Double.parseDouble(node[1]), Double.parseDouble(node[2]));
                        graph.addNode(n);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void main(String[] args) {
        System.out.println("//CrimeSampleDataset//");
        Graph graph1 = getNodesFromcrimeDataset();
        graph1.connectAllNodes();
        List<Edge> mst1 = Christofides.findMST(graph1);
        System.out.println("List of Edges MST :" + mst1.size());
        List<Node> oddDegrNodes1 = Christofides.findOddDegreeVertices(graph1, mst1);
        List<Edge> perfectMatchingEdges1 = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes1);
        System.out.println("PFM :" + perfectMatchingEdges1.size());
        List<Node> eulerTour1 = Christofides.eulerTour(graph1, mst1, perfectMatchingEdges1);
        System.out.println("EUL :" + eulerTour1.size());
        // printGraph(eulerTour1);

        // generate TSP tour using euler cycle
        List<Node> hamiltonCycle1 = Christofides.generateTSPTour(eulerTour1);
        System.out.println("TSP - Hamiltonian cycle :" + hamiltonCycle1.size());
        System.out.println("Hamiltonian cycle cost :" + Christofides.calculateTourLength(hamiltonCycle1));
    }


}

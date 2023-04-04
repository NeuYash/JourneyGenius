package com.project.model;

import com.project.utility.DeepCopyUtils;

import java.util.*;

public class Christofides {

    public static List<Edge> findMST(Graph graph) {
        return graph.kruskalMST();
    }

    public static List<Node> findOddDegreeVertices(Graph graph, List<Edge> mst) {
        Map<Node, Integer> degrees = new HashMap<>();
        for (Node node : graph.nodes) {
            degrees.put(node, 0);
        }
        for (Edge edge : mst) {
            degrees.put(edge.source, degrees.get(edge.source) + 1);
            degrees.put(edge.destination, degrees.get(edge.destination) + 1);
        }
        List<Node> oddDegreeNodes = new ArrayList<>();
        for (Map.Entry<Node, Integer> entry : degrees.entrySet()) {
            if (entry.getValue() % 2 != 0) {
                oddDegreeNodes.add(entry.getKey());
            }
        }
        return oddDegreeNodes;
    }

    public static List<Edge> getMinimumWeightPerfectMatching(List<Node> oddDegreeNodes) {
        Graph subgraph = new Graph(oddDegreeNodes);
        subgraph.connectAllNodes();
        return subgraph.getMinimumWeightPerfectMatching();
    }

    private static void dfs(Node node, List<Node> eulerTour, List<Edge> edges, Set<Node> visited,
                            Map<Node, List<Node>> adjacenyMatrix) {

        visited.add(node);
        for (Node v : adjacenyMatrix.get(node)) {
            if (!visited.contains(v)) {
                eulerTour.add(node);
                eulerTour.add(v);

                Edge matchedEdge = edges.get(0);
                for (Edge edge : edges) {
                    if (edge.source.crimeId == node.crimeId && edge.destination.crimeId == v.crimeId) {
                        matchedEdge = edge;
                        break;
                    }
                }
                edges.remove(matchedEdge);
                dfs(v, eulerTour, edges, visited, adjacenyMatrix);
            }
        }
    }

    // Step 5: Find an Eulerian circuit in the Eulerian graph
    public static List<Node> eulerTour(Graph graph, List<Edge> mst, List<Edge> perfEdges) {
        List<Node> eulerTour = new ArrayList<>();
        List<Edge> combineEdges = new ArrayList<>(mst);
        combineEdges.addAll(perfEdges);
        Map<Node, List<Node>> adjacencyMatrix = graph.adjacencyMatrix();
        Set<Node> visited = new HashSet<>();
        Node startNode = combineEdges.get(0).source;
        dfs(startNode, eulerTour, combineEdges, visited, adjacencyMatrix);
        return eulerTour;
    }

    // Step 6: Remove duplicates to obtain the TSP path
    public static List<Node> generateTSPTour(List<Node> eulerTour) {
        List<Node> hamiltonList = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        for (Node node : eulerTour) {
            if (!visited.contains(node)) {
                visited.add(node);
                hamiltonList.add(node);
            }
        }
        return hamiltonList;
    }

    public static List<Node> randomSwapOptimise(List<Node> tspTour, Integer iterations) {
        List<Node> randomSwapTour = tspTour;
        double currMaxTourLength = calculateTourLength(randomSwapTour);
        for (int i = 0; i < iterations; i++) {
            int randomIndexOne = (int) (Math.random() * tspTour.size());
            int randomIndexTwo = (int) (Math.random() * tspTour.size());
            List<Node> swappedTour = DeepCopyUtils.deepCopy(randomSwapTour);
            swap(swappedTour, randomIndexOne, randomIndexTwo);
            double swappedTourLength = calculateTourLength(swappedTour);
            if (swappedTourLength < currMaxTourLength) {
                currMaxTourLength = swappedTourLength;
                randomSwapTour = DeepCopyUtils.deepCopy(swappedTour);
            }
        }
        return randomSwapTour;
    }

    public static List<Node> twoOpt(List<Node> tour) {
        List<Node> twoOptTour = new ArrayList<>(tour);
        double length = calculateTourLength(twoOptTour);
        int n = twoOptTour.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                Node u = twoOptTour.get(i);
                Node v = twoOptTour.get((i + 1) % n);
                Node x = twoOptTour.get(j);
                Node y = twoOptTour.get((j + 1) % n);

                // Compute the lengths of the two possible new twoOptTours.
                double newLength1 = length - Graph.calculateDistance(u, v) - Graph.calculateDistance(x, y)
                        + Graph.calculateDistance(u, x) + Graph.calculateDistance(v, y);
                double newLength2 = length - Graph.calculateDistance(u, v) - Graph.calculateDistance(x, y)
                        + Graph.calculateDistance(u, y) + Graph.calculateDistance(v, x);

                // If either of the new twoOptTours is shorter, accept it as the new TSP
                // twoOptTour.
                if (newLength1 < length || newLength2 < length) {
                    List<Node> newTour = new ArrayList<>();

                    for (int k = 0; k <= i; k++) {
                        newTour.add(twoOptTour.get(k));
                    }

                    for (int k = j; k >= i + 1; k--) {
                        newTour.add(twoOptTour.get(k));
                    }

                    for (int k = j + 1; k < n; k++) {
                        newTour.add(twoOptTour.get(k));
                    }

                    // Update the length of the new twoOptTour.
                    length = (newLength1 < newLength2) ? newLength1 : newLength2;
                    tour = new ArrayList<>(newTour);
                }
            }
        }

        return tour;
    }

    private static void swap(List<Node> nodes, Integer i, Integer j) {
        Node nodeI = nodes.get(i);
        Node nodeJ = nodes.get(j);
        nodes.set(i, nodeJ);
        nodes.set(j, nodeI);
    }

    public static double calculateTourLength(List<Node> tour) {
        double length = 0;
        for (int i = 0; i < tour.size() - 2; i++) {
            Node source = tour.get(i);
            Node destination = tour.get(i + 1);
            double l = Graph.calculateDistance(source, destination);
            length += l;
        }
        /*
         * Adding the distance between first and last node
         */
        Node source = tour.get(0);
        Node destination = tour.get(tour.size() - 1);
        length += Graph.calculateDistance(source, destination);
        return length;
    }
}
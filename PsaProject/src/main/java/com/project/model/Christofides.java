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

    // 3 OPT
    public static List<Node> threeOpt(List<Node> tour) {
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 0; i < tour.size() - 3; i++) {
                for (int j = i + 2; j < tour.size() - 1; j++) {
                    for (int k = j + 2; k < tour.size(); k++) {
                        double distA = Graph.calculateDistance(tour.get(i), tour.get(i + 1))
                                + Graph.calculateDistance(tour.get(j), tour.get(j + 1))
                                + Graph.calculateDistance(tour.get(k), tour.get(k - 1));
                        double distB = Graph.calculateDistance(tour.get(i), tour.get(j))
                                + Graph.calculateDistance(tour.get(i + 1), tour.get(j + 1))
                                + Graph.calculateDistance(tour.get(k), tour.get(k - 1));
                        double distC = Graph.calculateDistance(tour.get(i), tour.get(j + 1))
                                + Graph.calculateDistance(tour.get(i + 1), tour.get(j))
                                + Graph.calculateDistance(tour.get(k), tour.get(k - 1));
                        double distD = Graph.calculateDistance(tour.get(i), tour.get(j + 1))
                                + Graph.calculateDistance(tour.get(i + 1), tour.get(k))
                                + Graph.calculateDistance(tour.get(j), tour.get(k - 1));
                        double distE = Graph.calculateDistance(tour.get(i), tour.get(k))
                                + Graph.calculateDistance(tour.get(j + 1), tour.get(i + 1))
                                + Graph.calculateDistance(tour.get(j), tour.get(k - 1));
                        if (distB < distA) {
                            reverse(tour, i + 1, j);
                            reverse(tour, j + 1, k);
                            improvement = true;
                        } else if (distC < distA) {
                            swap1(tour, i + 1, j);
                            swap1(tour, j + 1, k);
                            improvement = true;
                        } else if (distD < distA) {
                            swap1(tour, i + 1, k);
                            reverse(tour, j + 1, k);
                            improvement = true;
                        } else if (distE < distA) {
                            swap1(tour, i + 1, j);
                            swap1(tour, k, j + 1);
                            improvement = true;
                        }
                    }
                }
            }
        }
        return tour;
    }

    public static void reverse(List<Node> tour, int start, int end) {
        while (start < end) {
            Node tmp = tour.get(start);
            tour.set(start, tour.get(end));
            tour.set(end, tmp);
            start++;
            end--;
        }
    }

    public static void swap1(List<Node> tour, int i, int j) {
        Node tmp = tour.get(i);
        tour.set(i, tour.get(j));
        tour.set(j, tmp);
    }
    // 3OPT finish

    // 3 OPT Second Method
    public static List<Node> threeOptChristofides(List<Node> nodes) {
        int improvementCount = 0;
        do {
            improvementCount = 0;
            for (int i = 0; i < nodes.size() - 2; i++)
                for (int j = i + 1; j < nodes.size() - 1; j++)
                    for (int k = j + 1; k < nodes.size(); k++) {

                        List<Node> newNodes = Swap(nodes, i, j, k);
                        if (Christofides.calculateTourLength(newNodes) < Christofides.calculateTourLength(nodes)) {
                            nodes = newNodes;
                            improvementCount++;
                        }
                    }
        } while (improvementCount > 0);
        return nodes;
    }

    public static List<Node> Swap(List<Node> nodes, int i, int j, int k) {
        List<Node> newCities = new ArrayList<>();
        for (int x = 0; x <= i; x++)
            newCities.add(nodes.get(x));

        for (int x = j + 1; x <= k; x++)
            newCities.add(nodes.get(x));

        for (int x = i + 1; x <= j; x++)
            newCities.add(nodes.get(x));

        for (int x = k + 1; x < nodes.size(); x++)
            newCities.add(nodes.get(x));

        return newCities;
    }
    // 3 OPT second Method finish

    // K Opt tour
    public static void kOpt(List<Node> tour, int k) {
        int n = tour.size();
        List<Node> segment = new ArrayList<>(k + 1);
        List<Node> newTour = new ArrayList<>(n);
        List<Node> flipped = new ArrayList<>(k + 1);

        for (Node node : tour) {
            newTour.add(node);
        }

        for (int i = 0; i < n - k; i++) {
            for (int j = i + k; j < n; j++) {
                segment.clear();
                flipped.clear();
                for (int x = 0; x <= k; x++) {
                    segment.add(newTour.get((i + x) % n));
                }

                int count = 0;
                for (int x = k; x >= 0; x--) {
                    flipped.add(segment.get(x));
                    count++;
                }

                count = 0;
                for (int x = i + 1; x < i + k; x++) {
                    newTour.set(x, flipped.get(count));
                    count++;
                }

                count = 0;
                for (int x = i + k; x <= j; x++) {
                    newTour.set(x, segment.get(count));
                    count++;
                }

                count = 0;
                for (int x = j + 1; x < n; x++) {
                    newTour.set(x, tour.get((i + k + 1 + count) % n));
                    count++;
                }

                if (calculateTourLength(newTour) < calculateTourLength(tour)) {
                    tour.clear();
                    for (Node node : newTour) {
                        tour.add(node);
                    }
                }
            }
        }
    }

    // K Opt tour finish

    // SIMULATED ANNEALING
    public static List<Node> simulatedAnnealingOptimizeTour(List<Node> tour) {
        Random rand = new Random();
        double temperature = 10000;
        //double coolingRate = 0.003;
        double coolingRate = 0.01;
        List<Node> currentSolution = new ArrayList<>(tour);
        List<Node> bestSolution = new ArrayList<>(tour);

        while (temperature > 1) {
            List<Node> newSolution = new ArrayList<>(currentSolution);

            // Generate a new neighboring solution by randomly swapping two nodes
            int i = rand.nextInt(newSolution.size() - 1) + 1;
            int j = rand.nextInt(newSolution.size() - 1) + 1;
            Node tmp = newSolution.get(i);
            newSolution.set(i, newSolution.get(j));
            newSolution.set(j, tmp);

            // Compute the cost of the new and current solutions
//            double currentCost = computeTourCost(currentSolution);
//            double newCost = computeTourCost(newSolution);
            double currentCost = calculateTourLength(currentSolution);
            double newCost = calculateTourLength(newSolution);

            // Decide whether to accept the new solution or not
            if (newCost < currentCost) {
                currentSolution = new ArrayList<>(newSolution);
            } else if (Math.exp((currentCost - newCost) / temperature) > rand.nextDouble()) {
                currentSolution = new ArrayList<>(newSolution);
            }

            // Update the best solution found so far
            if (calculateTourLength(currentSolution) < calculateTourLength(bestSolution)) {
                bestSolution = new ArrayList<>(currentSolution);
            }

            // Decrease the temperature
            temperature *= 1 - coolingRate;
        }

        return currentSolution;
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
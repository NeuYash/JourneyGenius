package com.project;

import com.project.model.Christofides;
import com.project.model.Edge;
import com.project.model.Graph;
import com.project.model.Node;
import junit.framework.TestCase;

import java.util.List;

public class ChristofidesTSPTest extends TestCase {

	public static List<Node> ChrisTSP() {
		Graph graph = App.getNodesFromDataset();
		graph.connectAllNodes();
		List<Edge> mst = Christofides.findMST(graph);
		List<Node> oddDegrNodes = Christofides.findOddDegreeVertices(graph, mst);
		List<Edge> perfectMatchingEdges = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes);
		List<Node> eulerTour = Christofides.eulerTour(graph, mst, perfectMatchingEdges);
		return Christofides.generateTSPTour(eulerTour);
	}

	public static List<Node> ChrisTSPNew(Graph graph) {
		graph.connectAllNodes();
		List<Edge> mst = Christofides.findMST(graph);
		List<Node> oddDegrNodes = Christofides.findOddDegreeVertices(graph, mst);
		List<Edge> perfectMatchingEdges = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes);
		List<Node> eulerTour = Christofides.eulerTour(graph, mst, perfectMatchingEdges);
		return Christofides.generateTSPTour(eulerTour);
	}

	

	// @Test
	public void testTwoNodeGraph() {
		Graph graph = new Graph();

		Node node1 = new Node("CrimeID1", -0.009691, 51.483548);
		Node node2 = new Node("CrimeID2", -0.118888, 51.513075);
		graph.addNode(node1);
		graph.addNode(node2);
		graph.connectAllNodes();
		List<Node> tsp = ChrisTSPNew(graph);
		assertEquals(Graph.calculateDistance(node1, node2), Christofides.calculateTourLength(tsp));
		assertEquals(node1, tsp.get(0));
		assertEquals(node2, tsp.get(1));
	}

	// @Test
	public void testThreeNodeGraph() {
		Graph graph = new Graph();

		Node node1 = new Node("CrimeID1", -0.009691, 51.483548);
		Node node2 = new Node("CrimeID2", -0.118888, 51.513075);
		Node node3 = new Node("CrimeID3", 0.076327, 51.540042);
		graph.addNode(node1);
		graph.addNode(node2);
		graph.addNode(node3);

		graph.connectAllNodes();
		List<Node> tsp = ChrisTSPNew(graph);
		assertEquals(3, tsp.size());
		assertEquals(node1, tsp.get(0));
		assertEquals(node2, tsp.get(1));
		assertEquals(node3, tsp.get(2));
		assertEquals(16895.438153961317, Christofides.calculateTourLength(tsp), 0.001);
	}

	// @Test
	public void testFiveNodeGraph() {
		Graph graph = new Graph();

		Node node1 = new Node("CrimeID1", -0.009691, 51.483548);
		Node node2 = new Node("CrimeID2", -0.118888, 51.513075);
		Node node3 = new Node("CrimeID3", 0.076327, 51.540042);
		Node node4 = new Node("CrimeID4", -0.418139, 51.500839);
		Node node5 = new Node("CrimeID5", -0.134987, 51.46327);
		graph.addNode(node1);
		graph.addNode(node2);
		graph.addNode(node3);
		graph.addNode(node4);
		graph.addNode(node5);
		graph.connectAllNodes();
		List<Node> tour = ChrisTSPNew(graph);
		assertEquals(5, tour.size());
		assertTrue(tour.contains(node1));
		assertTrue(tour.contains(node2));
		assertTrue(tour.contains(node3));
		assertTrue(tour.contains(node4));
		assertTrue(tour.contains(node5));
		assertEquals(44025.37576885142, Christofides.calculateTourLength(tour), 0.001);
	}
}

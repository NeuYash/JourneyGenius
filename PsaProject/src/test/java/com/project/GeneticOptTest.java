package com.project;

import com.project.model.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeneticOptTest {

	public static List<Node> ChrisTSP() {
		Graph graph = App.getNodesFromDataset();
		graph.connectAllNodes();
		List<Edge> mst = Christofides.findMST(graph);
		List<Node> oddDegrNodes = Christofides.findOddDegreeVertices(graph, mst);
		List<Edge> perfectMatchingEdges = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes);
		List<Node> eulerTour = Christofides.eulerTour(graph, mst, perfectMatchingEdges);
		return Christofides.generateTSPTour(eulerTour);
	}

//	@Test
//	public void testTwoNodeGraph() {
//		Graph graph = new Graph();
//
//		Node node1 = new Node("CrimeID1", -0.009691, 51.483548);
//		Node node2 = new Node("CrimeID2", -0.118888, 51.513075);
//		graph.addNode(node1);
//		graph.addNode(node2);
//		graph.connectAllNodes();
//		List<Node> tsp = ChrisTSPNew(graph);
//		List<Node> GeneticOptTsp=TSPGenetic.TSPGenAlgo(tsp);
//		assertEquals(Graph.calculateDistance(node1, node2), Christofides.calculateTourLength(GeneticOptTsp), 0.01);
//		assertEquals(node2.getCrimeId(), GeneticOptTsp.get(0).getCrimeId());
//		assertEquals(node1.getCrimeId(), GeneticOptTsp.get(1).getCrimeId());
//	}
//	
//	@Test
//	public void testThreeNodeGraph() {
//		Graph graph = new Graph();
//
//		Node node1 = new Node("CrimeID1", -0.009691, 51.483548);
//		Node node2 = new Node("CrimeID2", -0.118888, 51.513075);
//		Node node3 = new Node("CrimeID3", 0.076327, 51.540042);
//		graph.addNode(node1);
//		graph.addNode(node2);
//		graph.addNode(node3);
//		graph.connectAllNodes();
//		List<Node> tsp = ChrisTSPNew(graph);
//		List<Node> GeneticOptTsp=TSPGenetic.TSPGenAlgo(tsp);
//		//assertEquals(Christofides.calculateTourLength(tsp), Christofides.calculateTourLength(GeneticOptTsp), 0.01);
//		assertEquals(node2.getCrimeId(), GeneticOptTsp.get(0).getCrimeId());
//		assertEquals(node1.getCrimeId(), GeneticOptTsp.get(1).getCrimeId());
//	}

	@Test
	public void GeneticOPT() {
		List<Node> tsp = ChrisTSP();
		List<Node> genetic = TSPGenetic.TSPGenAlgo(tsp);
		assertEquals(tsp.size(), genetic.size(), 0.01);
		assertTrue(Christofides.calculateTourLength(tsp) < Christofides.calculateTourLength(genetic));
	}

	@Test
	public void GeneticOPT1() {
		List<Node> tsp = ChrisTSP();
		List<Node> genetic = TSPGenetic.TSPGenAlgo(tsp);
		assertEquals(tsp.size(), genetic.size(), 0.01);
	}
}

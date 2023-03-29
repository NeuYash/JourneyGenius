package com.project;

import com.project.model.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import com.project.model.Node;

public class App {

	public static void main(String[] args) {

	}

	public static Graph getNodesFromcrimeDataset() {
		Graph graph = new Graph();
		String line = "";
		String splitBy = ",";
		Set<String> uniqueNodes = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("crimeSample.csv"));
			reader.readLine();
			int count=0;
			while ((line = reader.readLine()) != null ) {
				count++;
				String[] node = line.split(splitBy);
				if(node.length>0 && !(node[0].isEmpty() || node[1].isEmpty() || node[2].isEmpty())){
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
}

package com.project.model;

import java.util.List;

public class Christofides {

    public static List<Edge> findMST(Graph graph) {
        return graph.kruskalMST();
    }
}
package com.project.visualization;

import com.project.model.Edge;
import com.project.model.Graph;
import com.project.model.Node;

public class GraphOperation {

    public enum Action {
        ADD,
        REMOVE,
        SETLAYOUT
    }

    public enum Layout {
        HIGHLIGHT,
        UNHIGHLIGHT
    }

    Action action;
    Layout layout = Layout.UNHIGHLIGHT;
    Node node1;
    Node node2;
    Edge edge;

    public GraphOperation setLayout(Layout layout) {
        this.layout = layout;
        return this;
    }

    public Edge getEdge() {
        return edge;
    }

    public Layout getLayout() {
        return layout;
    }

    public Action getAction() {
        return action;
    }

    private GraphOperation(Node node1, Node node2, Action act) {
        this.node1 = node1;
        this.node2 = node2;
        this.action = act;
        this.edge = new Edge(node1, node2, Graph.calculateDistance(node1, node2));
    }

    static public GraphOperation addEdge(Node node1, Node node2) {
        return new GraphOperation(node1, node2, Action.ADD);
    }

    static public GraphOperation removeEdge(Node node1, Node node2) {
        return new GraphOperation(node1, node2, Action.REMOVE);
    }

    static public GraphOperation highlightEdge(Node node1, Node node2) {
        return new GraphOperation(node1, node2, Action.SETLAYOUT).setLayout(Layout.HIGHLIGHT);
    }

    static public GraphOperation unhighlightEdge(Node node1, Node node2) {
        return new GraphOperation(node1, node2, Action.SETLAYOUT).setLayout(Layout.UNHIGHLIGHT);
    }

    static public GraphOperation addEdge(Edge edge) {
        return GraphOperation.addEdge(edge.getSource(), edge.getDestination());
    }

    static public GraphOperation removeEdge(Edge edge) {
        return new GraphOperation(edge.getSource(), edge.getDestination(), Action.REMOVE);
    }

    static public GraphOperation highlightEdge(Edge edge) {
        return new GraphOperation(edge.getSource(), edge.getDestination(), Action.SETLAYOUT).setLayout(Layout.HIGHLIGHT);
    }

    static public GraphOperation unhighlightEdge(Edge edge) {
        return new GraphOperation(edge.getSource(), edge.getDestination(), Action.SETLAYOUT).setLayout(Layout.UNHIGHLIGHT);
    }

    @Override
    public String toString() {
        return "GraphOperation{" +
                "action=" + action +
                ", layout=" + layout +
                ", node1=" + node1.getCrimeId() +
                ", node2=" + node2.getCrimeId() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof GraphOperation)) {
            return false;
        }
        GraphOperation go = (GraphOperation) obj;
        boolean res = true;
        if (!go.getEdge().equals(this.getEdge())) {
            return false;
        }
        if (this.getAction() != go.getAction()) {
            return false;
        }
        if (this.getLayout() != go.getLayout()) {
            return false;
        }
        return true;
    }
}
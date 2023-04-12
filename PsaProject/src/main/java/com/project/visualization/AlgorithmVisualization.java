package com.project.visualization;

import com.project.model.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class AlgorithmVisualization {

    public AlgorithmVisualization(List<Node> nodes, List<GraphOperation> gos, List<GraphOperation> oldGos) {
        this.nodes = nodes;
        this.oldGos = oldGos;
        this.gos = gos;
    }

    private List<Node> nodes;
    private List<GraphOperation> gos;
    private List<GraphOperation> oldGos;
    private int sleepTime = 500;

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public AlgorithmVisualization(List<Node> nodes, List<GraphOperation> gos) {
        this.nodes = nodes;
        this.gos = gos;
    }

    //public static int i=0;
    public void showResult(String graphTitle, String graphDes, boolean flag) {

        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("Tutorial 1");
        graph.setAttribute("ui.stylesheet", "node{\n"
                + "    size: 5px, 5px;\n"
                + "    fill-color: black;\n"
                + "    text-mode: normal; \n"
                + "}"
                + "edge.highlight{\n"
                + "   fill-color: red;\n"
                + "   size: 2.5px, 2.5px;"
                + "}"
        );
        // Add all the nodes
        for (Node node : nodes) {
            graph.addNode(node.getCrimeId()).setAttribute("ui.label", node.getCrimeId());
            graph.getNode((node.getCrimeId())).setAttribute("layout.frozen");
            graph.getNode((node.getCrimeId())).setAttribute("xy", node.getLatitude(), node.getLongitude());
        }
        if (oldGos != null) {
            for (GraphOperation go : oldGos) {
                changeEdge(graph, go);
            }
        }
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        ViewPanel view = viewer.addDefaultView(false);
        view.setPreferredSize(new java.awt.Dimension(800, 600));

        JLabel label = new JLabel(graphDes);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel(new java.awt.BorderLayout());
        panel.add(view, java.awt.BorderLayout.CENTER);
        panel.add(label, java.awt.BorderLayout.NORTH);

        JFrame frame = new JFrame(graphTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        //graph.display();
        if (flag) {
            //for genetic
            if (oldGos != null) {
                for (GraphOperation ng : gos) {
                    for (GraphOperation og : oldGos) {
                        if (og.node1 == ng.node1) {
                            if (og.node2 != ng.node2) {
                                Edge e = graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()) == null ? graph.getEdge(og.node2.getCrimeId() + og.node1.getCrimeId()) : graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId());
                                if (graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()) != null) {
                                    graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()).setAttribute("ui.class", "highlight");
                                } else if (graph.getEdge(og.node2.getCrimeId() + og.node1.getCrimeId()) != null) {
                                    graph.getEdge(og.node2.getCrimeId() + og.node1.getCrimeId()).setAttribute("ui.class", "highlight");
                                } else {
                                    graph.addEdge(og.node1.getCrimeId() + og.node2.getCrimeId(), og.node1.getCrimeId(), og.node2.getCrimeId());
                                    graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()).setAttribute("ui.class", "highlight");
                                }
                                e = graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()) == null ? graph.getEdge(og.node2.getCrimeId() + og.node1.getCrimeId()) : graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId());
                                sleep();
                                sleep();
                                graph.removeEdge(e);
                                sleep();
                                Edge e2 = graph.getEdge(ng.node1.getCrimeId() + ng.node2.getCrimeId());
                                if (graph.getEdge(ng.node1.getCrimeId() + ng.node2.getCrimeId()) == null && graph.getEdge(ng.node2.getCrimeId() + ng.node1.getCrimeId()) == null) {
                                    graph.addEdge(ng.node1.getCrimeId() + ng.node2.getCrimeId(), ng.node1.getCrimeId(), ng.node2.getCrimeId());
                                    sleep();
                                }
                            }
                        }
                    }
                }
            }
        }//flag if
        else {
            if (oldGos != null) {
                for (GraphOperation ng : gos) {
                    for (GraphOperation og : oldGos) {
                        if (og.node1 == ng.node1) {
                            if (og.node2 != ng.node2) {
                                Edge e = graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()) == null ? graph.getEdge(og.node2.getCrimeId() + og.node1.getCrimeId()) : graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId());
                                if (graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()) != null) {
                                    graph.getEdge(og.node1.getCrimeId() + og.node2.getCrimeId()).setAttribute("ui.class", "highlight");
                                } else {
                                    graph.getEdge(og.node2.getCrimeId() + og.node1.getCrimeId()).setAttribute("ui.class", "highlight");
                                }
                                sleep();
                                sleep();
                                graph.removeEdge(e);
                                sleep();
                                Edge e2 = graph.getEdge(ng.node1.getCrimeId() + ng.node2.getCrimeId());
                                if (graph.getEdge(ng.node1.getCrimeId() + ng.node2.getCrimeId()) == null && graph.getEdge(ng.node2.getCrimeId() + ng.node1.getCrimeId()) == null) {
                                    graph.addEdge(ng.node1.getCrimeId() + ng.node2.getCrimeId(), ng.node1.getCrimeId(), ng.node2.getCrimeId());
                                    sleep();
                                }
                            }
                        }
                    }
                }
            }
        }//flag else
        if (gos != null) {
            for (GraphOperation go : gos) {
                changeEdge(graph, go);
                sleep();
            }
        }

    }

    private void changeEdge(Graph graph, GraphOperation go) {
        switch (go.getAction()) {
            case ADD:
                Edge e = graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId()) == null ? graph.getEdge(go.node2.getCrimeId() + go.node1.getCrimeId()) : graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId());
                if (e == null) {
                    graph.addEdge(go.node1.getCrimeId() + go.node2.getCrimeId(), go.node1.getCrimeId(), go.node2.getCrimeId());
                    if (go.getLayout() == GraphOperation.Layout.HIGHLIGHT) {
                        graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId()).setAttribute("ui.class", "highlight");
                    }
                } else {
                    //System.out.println("Edge dulplicate");
                }

                break;
            case REMOVE:
                Edge edge = graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId()) == null ? graph.getEdge(go.node2.getCrimeId() + go.node1.getCrimeId()) : graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId());
                if (edge != null) {
                    graph.removeEdge((org.graphstream.graph.Node) graph.getNode(go.node1.getCrimeId()), graph.getNode(go.node2.getCrimeId()));
                } else {
                    System.out.println("Edge not exist");
                }
//                if (graph.getEdge(go.node1.getId() + go.node2.getId()) != null) {
//                    edge = graph.getEdge(go.node1.getId() + go.node2.getId());
//                } else if (graph.getEdge(go.node2.getId() + go.node1.getId()) != null) {
//                    edge = graph.getEdge(go.node2.getId() + go.node1.getId());
//                } else {
//                    break;
//                }
//                graph.removeEdge(edge);
                break;
            case SETLAYOUT:
                Edge edge1;
                if (graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId()) != null) {
                    edge1 = graph.getEdge(go.node1.getCrimeId() + go.node2.getCrimeId());
                } else if (graph.getEdge(go.node2.getCrimeId() + go.node1.getCrimeId()) != null) {
                    edge1 = graph.getEdge(go.node2.getCrimeId() + go.node1.getCrimeId());
                } else {
                    break;
                }
                if (go.getLayout() == GraphOperation.Layout.UNHIGHLIGHT) {
                    edge1.removeAttribute("ui.class");
                } else {
                    edge1.addAttribute("ui.class", "highlight");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + go.getAction());
        }
    }

    protected void sleep() {
        try {
            Thread.sleep(sleepTime);
        } catch (Exception e) {
        }
    }
}
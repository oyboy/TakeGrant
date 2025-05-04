package org.example;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Map;
import java.util.Set;

public class GraphVisualizer {
    private final AccessGraph accessGraph = new AccessGraph();

    public void display() {
        Map<String, Map<String, Set<String>>> data = accessGraph.read();

        Graph graph = new SingleGraph("Access Graph");

        graph.setAttribute("ui.stylesheet",
                "node { fill-color: lightblue; size: 25px; text-size: 16px; } " +
                        "edge { text-size: 14px; fill-color: gray; }");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        for (String from : data.keySet()) {
            if (graph.getNode(from) == null)
                graph.addNode(from).setAttribute("ui.label", from);

            for (String to : data.get(from).keySet()) {
                if (graph.getNode(to) == null)
                    graph.addNode(to).setAttribute("ui.label", to);

                String edgeId = from + "_" + to;

                if (graph.getEdge(edgeId) == null) {
                    Edge edge = graph.addEdge(edgeId, from, to, true);
                    String rights = String.join(",", data.get(from).get(to));
                    edge.setAttribute("ui.label", rights);
                }
            }
        }
        graph.display();
    }
}
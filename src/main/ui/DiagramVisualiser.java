package main.ui;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import main.model.StylingType;

import java.util.ArrayList;
import java.util.List;

import static main.Main.graphList;
import static main.Main.graphView;

public class DiagramVisualiser {

    private Vertex<String> startVertex;
    private final List<Vertex<String>> finalVertices;
    private final List<Vertex<String>> vertices;
    private final List<Edge<String, String>> edges;

    public DiagramVisualiser() {
        finalVertices = new ArrayList<>();
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

//    public void draw(Automata automaton) {
//        reset();
//
//        // Start state
//        addVertex(String.valueOf(automaton.getStartState()), StylingType.START);
//
//        // final state
//        addVertex(String.valueOf(automaton.getFinalState()), StylingType.FINAL);
//
//        for (Transition t : automaton.getTransitions()) {
//
//            // Add vertices if it doesn't exist
//            addVertex(String.valueOf(t.from), StylingType.NORMAL);
//            addVertex(String.valueOf(t.to), StylingType.NORMAL);
//
//            // Add edge between vertices
//            addEdge(
//                    String.valueOf(t.from),
//                    String.valueOf(t.to),
//                    formatSymbol(t.symbol)
//            );
//        }
//        build();
//    }

    public void addVertex(String label, StylingType type) {
        if (doesVertexExist(label)) return;

        Vertex<String> v = graphList.insertVertex(label);

        switch (type) {
            case START:
                startVertex = v;
                break;
            case FINAL:
                finalVertices.add(v);
                break;
            default:
                // do nothing
        }

        vertices.add(v);
    }

    private boolean doesVertexExist(String label) {
        for (Vertex<String> v : vertices) {
            if (v.element().equals(label)) return true;
        }
        return false;
    }

    public void addEdge(String from, String to, String label) {
        if (!doesEdgeExist(from, to)) {
            edges.add(graphList.insertEdge(from, to, label));
        }
    }

    private boolean doesEdgeExist(String from, String to) {
        for (Edge<String, String> e : edges) {
            String beginState = e.vertices()[0].element();
            String endState = e.vertices()[1].element();

            if (beginState.equals(from) && endState.equals(to)) {
                return true;
            }
        }
        return false;
    }

    private String formatSymbol(String symbol) {
        String label = String.valueOf(symbol);

        do {
            label += " ";
        } while (doesLabelAlreadyExist(label));

        return label;
    }

    private boolean doesLabelAlreadyExist(String label) {
        for (Edge<String, String> e : edges) {
            String l = e.element();

            if (l.equals(label)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        if (!graphList.vertices().isEmpty()) {
            // Needs to be in this order to prevent errors
            for (Edge<String, String> e : edges) graphList.removeEdge(e);
            for (Vertex<String> v : vertices) graphList.removeVertex(v);

            edges.clear();
            vertices.clear();

            build();
        }

        if (startVertex != null) startVertex = null;
        if (!finalVertices.isEmpty()) finalVertices.clear();
    }

    public void build() {
        graphView.updateAndWait();
        updateStyling();
    }

    private void updateStyling() {
        try {
            if (startVertex != null) setStartStyle(startVertex);
            for (Vertex<String> v : finalVertices) setFinalStyle(v);
        } catch (Exception e) {
            // Do nothing
        }
    }

    private void setStartStyle(Vertex<String> v) {
        graphView.getStylableVertex(v).setStyle("-fx-fill: white; -fx-stroke: black;");
    }

    private void setFinalStyle(Vertex<String> v) {
        graphView.getStylableVertex(v).setStyle("-fx-fill: #ddf0d1; -fx-stroke: #8dcd65;");
    }
}

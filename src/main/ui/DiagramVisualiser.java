package main.ui;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import main.model.automata.FA;
import main.model.visual.StylingType;
import main.model.automata.Transition;

import java.util.ArrayList;
import java.util.List;

import static main.Main.graphList;
import static main.Main.graphView;

public class DiagramVisualiser {

    private final List<Vertex<String>> startVertices;
    private final List<Vertex<String>> finalVertices;
    private final List<Vertex<String>> startFinalVertices;
    private final List<Vertex<String>> vertices;
    private final List<Edge<String, String>> edges;

    public DiagramVisualiser() {
        startFinalVertices = new ArrayList<>();
        startVertices = new ArrayList<>();
        finalVertices = new ArrayList<>();
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void draw(FA automata) {
        reset();

        /* Needs to be seperate to prevent styling issues */
        for (Transition t : automata.getTransitions()) {
            if (automata.getStartStates().contains(t.getOrigin()) && automata.getEndStates().contains(t.getOrigin())) {
                addVertex(t.getOrigin(), StylingType.START_FINAL);
            } else if (automata.getStartStates().contains(t.getOrigin())) {
                addVertex(t.getOrigin(), StylingType.START);
            } else if (automata.getEndStates().contains(t.getOrigin())) {
                addVertex(String.valueOf(t.getOrigin()), StylingType.FINAL);
            }
        }

        for (Transition t : automata.getTransitions()) {
            // Add vertices if it doesn't exist
            addVertex(String.valueOf(t.getOrigin()), StylingType.NORMAL);
            addVertex(String.valueOf(t.getDestination()), StylingType.NORMAL);

            // Add edge between vertices
            addEdge(
                    String.valueOf(t.getOrigin()),
                    String.valueOf(t.getDestination()),
                    formatLabel(t.getSymbol())
            );

        }
        build();
    }

    public void addVertex(String label, StylingType type) {
        if (doesVertexExist(label)) return;

        Vertex<String> v = graphList.insertVertex(label);

        switch (type) {
            case START:
                startVertices.add(v);
                break;
            case FINAL:
                finalVertices.add(v);
                break;
            case START_FINAL:
                startFinalVertices.add(v);
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
        } else {
            // could be the case that the label is different, if so merge them
            if (!doesEdgeExistWithSameLabel(from, to, label)) {
                for (Edge<String, String> e : graphList.edges()) {
                    String beginState = e.vertices()[0].element();
                    String endState = e.vertices()[1].element();
                    String stateLabel = e.element();

                    if (beginState.equals(from) && endState.equals(to)) {
                        String newLabel = label + "," + stateLabel;
                        edges.add(graphList.insertEdge(from, to, formatLabel(newLabel)));
                        graphList.removeEdge(e);
                        return;
                    }
                }
            }
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

    private boolean doesEdgeExistWithSameLabel(String from, String to, String label) {
        for (Edge<String, String> e : edges) {
            String beginState = e.vertices()[0].element();
            String endState = e.vertices()[1].element();
            String stateLabel = e.element();

            if (beginState.equals(from) && endState.equals(to) && stateLabel.equals(label)) {
                return true;
            }
        }
        return false;
    }

    private String formatLabel(String symbol) {
        String label = String.valueOf(symbol);

        while (doesLabelAlreadyExist(label)){
            label += " ";
        }

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

        if (!startVertices.isEmpty()) startVertices.clear();
        if (!finalVertices.isEmpty()) finalVertices.clear();
        if (!startFinalVertices.isEmpty()) startFinalVertices.clear();
    }

    public void build() {
        graphView.updateAndWait();
        updateStyling();
    }

    private void updateStyling() {
        try {
            for (Vertex<String> v : startVertices) setStartStyle(v);
            for (Vertex<String> v : finalVertices) setFinalStyle(v);
            for (Vertex<String> v : startFinalVertices) setStartFinalStyle(v);
        } catch (Exception e) {
            // Do nothing
        }
    }

    private void setStartFinalStyle(Vertex<String> v) {
        graphView.getStylableVertex(v).setStyle("-fx-fill: white; -fx-stroke: #8dcd65;");
    }

    private void setStartStyle(Vertex<String> v) {
        graphView.getStylableVertex(v).setStyle("-fx-fill: white; -fx-stroke: black;");
    }

    private void setFinalStyle(Vertex<String> v) {
        graphView.getStylableVertex(v).setStyle("-fx-fill: #ddf0d1; -fx-stroke: #8dcd65;");
    }
}

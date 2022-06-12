package main.ui;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import main.model.Automata;
import main.model.Transition;

import java.util.ArrayList;
import java.util.List;

import static main.Main.graphList;
import static main.Main.graphView;

public class DiagramVisualiser {

    private final List<Vertex<String>> bufferVertexList;
    private final List<Edge<String, String>> bufferEdgeList;

    public DiagramVisualiser() {
        bufferVertexList = new ArrayList<>();
        bufferEdgeList = new ArrayList<>();
    }

    public void draw(Automata automaton) {
        List<Transition> transitionsResult = automaton.getTransitions();

        clear();

        // Draw nodes
        for (Transition t : transitionsResult) {
            String state = t.getFromState().toString();

            if (bufferVertexList.isEmpty() || !doesVertexExist(state)) {
                bufferVertexList.add(graphList.insertVertex(state));
            }
        }

        // Draw edges
        for (Transition t : transitionsResult) {
            if (bufferEdgeList.isEmpty() || !doesEdgeExist(t)) {
                bufferEdgeList.add(graphList.insertEdge(
                        t.getFromState().toString(),
                        t.getToState().toString(),
                        getSymbol(t.getSymbol())
                ));
            }
        }

        graphView.update();
    }

    private boolean doesVertexExist(String state) {
        for (Vertex<String> v : bufferVertexList) {
            if (v.element().equals(state)) return true;
        }
        return false;
    }

    private boolean doesEdgeExist(Transition state) {
        for (Edge<String, String> e : bufferEdgeList) {
            String beginState = e.vertices()[0].element();
            String endState = e.vertices()[1].element();

            if (beginState.equals(state.getFromState().toString()) && endState.equals(state.getToState().toString())) {
                return true;
            }
        }
        return false;
    }

    private String getSymbol(char symbol) {
        String label = String.valueOf(symbol);

        do {
            label += " ";
        } while (doesLabelAlreadyExist(label));

        return label;
    }

    private boolean doesLabelAlreadyExist(String label) {
        for (Edge<String, String> e : bufferEdgeList) {
            String l = e.element();

            if (l.equals(label)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        if (!graphList.vertices().isEmpty()) {
            // Needs to be in this order to prevent errors
            for (Edge<String, String> e : bufferEdgeList) graphList.removeEdge(e);
            for (Vertex<String> v : bufferVertexList) graphList.removeVertex(v);

            bufferEdgeList.clear();
            bufferVertexList.clear();

            build();
        }
    }

    public void addVertex(String label) {
        bufferVertexList.add(graphList.insertVertex(label));
    }

    public void addEdge(String from, String to, String label) {
        bufferEdgeList.add(graphList.insertEdge(from, to, label));
    }

    public void build() {
        graphView.update();
    }
}

package main.ui;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import main.model.Automata;
import main.model.VertexType;

import java.util.ArrayList;
import java.util.List;

import static main.Main.graphList;
import static main.Main.graphView;

public class DiagramVisualiser {

    private final List<Vertex<String>> bufferVertexList;
    private final List<Vertex<String>> finalVertexList;
    private Vertex<String> startVertex;

    private final List<Edge<String, String>> bufferEdgeList;

    public DiagramVisualiser() {
        bufferVertexList = new ArrayList<>();
        finalVertexList = new ArrayList<>();
        bufferEdgeList = new ArrayList<>();
    }

    public void draw(Automata automaton) {
        reset();

//        /* Start */
//        for (String state : new ArrayList<String> (automaton.getStartStates())) {
//            if (bufferVertexList.isEmpty() || !doesVertexExist(state)) {
//                addVertex(state, VertexType.START);
//            }
//        }
//        /* Final */
//        for (String state : new ArrayList<String> (automaton.getFinalStates())) {
//            if (bufferVertexList.isEmpty() || !doesVertexExist(state)) {
//                addVertex(state, VertexType.FINAL);
//            }
//        }
//
//        /* Others */
//        for (Transition t : (List<Transition>) automaton.getTransitions()) {
//            String state = t.getFromState().toString();
//
//            if (bufferVertexList.isEmpty() || !doesVertexExist(state)) {
//                addVertex(state, VertexType.NORMAL);
//            }
//        }
//
//        /* Edges */
//        for (Transition t : (List<Transition>) automaton.getTransitions()) {
//            if (bufferEdgeList.isEmpty() || !doesEdgeExist(t)) {
//                addEdge(
//                        t.getFromState().toString(),
//                        t.getToState().toString(),
//                        getSymbol(t.getSymbol())
//                );
//            }
//        }

        build();
    }

    private boolean doesVertexExist(String state) {
        for (Vertex<String> v : bufferVertexList) {
            if (v.element().equals(state)) return true;
        }
        return false;
    }

//    private boolean doesEdgeExist(Transition state) {
//        for (Edge<String, String> e : bufferEdgeList) {
//            String beginState = e.vertices()[0].element();
//            String endState = e.vertices()[1].element();
//
//            if (beginState.equals(state.getFromState().toString()) && endState.equals(state.getToState().toString())) {
//                return true;
//            }
//        }
//        return false;
//    }

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

    public void reset() {
        if (!graphList.vertices().isEmpty()) {
            // Needs to be in this order to prevent errors
            for (Edge<String, String> e : bufferEdgeList) graphList.removeEdge(e);
            for (Vertex<String> v : bufferVertexList) graphList.removeVertex(v);

            bufferEdgeList.clear();
            bufferVertexList.clear();

            build();
        }

        if (startVertex != null) startVertex = null;
        if (!finalVertexList.isEmpty()) finalVertexList.clear();
    }

    public void addVertex(String label, VertexType type) {
        Vertex<String> v = graphList.insertVertex(label);

        switch (type) {
            case START:
                startVertex = v;
                break;
            case FINAL:
                finalVertexList.add(v);
                break;
            case NORMAL:
            default:
                // do nothing
        }

        bufferVertexList.add(v);
    }

    public void addEdge(String from, String to, String label) {
        bufferEdgeList.add(graphList.insertEdge(from, to, label));
    }

    public void build() {
        graphView.updateAndWait();
        updateStyling();
    }

    private void updateStyling() {
        try {
            if (startVertex != null) setStartStyle(startVertex);
            for (Vertex<String> v : finalVertexList) setFinalStyle(v);
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

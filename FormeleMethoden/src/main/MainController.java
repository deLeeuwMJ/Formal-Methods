package main;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.example.dfa.Automata;
import main.example.dfa.TestAutomata;
import main.example.dfa.Transition;
import main.model.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static main.Main.graphList;
import static main.Main.graphView;

public class MainController implements Initializable {

    @FXML
    public ListView<Node> logList;
    public TextField regexField;
    public EnumChoiceBox<Operator> operatorChoiceBox;
    public ToggleGroup regexMode;
    public RadioButton inputMode;
    public Button addRegex;

    // Regex
    private List<Vertex<String>> bufferVertexList;
    private List<Edge<String, String>> bufferEdgeList;
    private String regexString;
    private RegExp lastRegex;

    // Automata
    private SortedSet<Character> terminalList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bufferVertexList = new ArrayList<>();
        bufferEdgeList = new ArrayList<>();
        terminalList = new TreeSet<>();
        lastRegex = new RegExp();
        regexString = "";

        initListeners();
    }

    private void initListeners() {
        regexMode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                if (regexMode.getSelectedToggle() != null) {
                    switch (getRegexMode()) {
                        case INPUT:
                            operatorChoiceBox.setVisible(false);
                            addRegex.setVisible(false);
                            break;
                        case BUILDER:
                            operatorChoiceBox.setVisible(true);
                            addRegex.setVisible(true);
                            break;
                    }
                    resetData();
                }
            }
        });
    }

    private RegexMode getRegexMode() {
        return RegexMode.valueOf(regexMode.getSelectedToggle().getUserData().toString());
    }

    public void onExampleButton(ActionEvent actionEvent) {
        resetData();

        displayOutput("RegEx: ab+");

        RegExp a = new RegExp("a");
        RegExp b = new RegExp("b");
        RegExp all = a.dot(b).plus();

        clearGraphList();

        bufferVertexList.add(graphList.insertVertex("q0"));
        bufferVertexList.add(graphList.insertVertex("q1"));

        bufferEdgeList.add(graphList.insertEdge("q0", "q1", "a"));
        bufferEdgeList.add(graphList.insertEdge("q0", "q0", " a"));
        bufferEdgeList.add(graphList.insertEdge("q1", "q1", "b"));
        bufferEdgeList.add(graphList.insertEdge("q1", "q0", " b"));

        graphView.update();

        displayOutput("Taal: " + all.getLanguage(3).toString());
    }

    private void addTerminals(String characters) {
        char[] splitString = characters.toCharArray();

        for (char c : splitString) {
            if (!terminalList.contains(c)) {
                terminalList.add(c);
            }
        }
    }

    public void onResultButton(ActionEvent actionEvent) {
        if (getRegexMode() == RegexMode.BUILDER) {
            displayError(LogErrorType.NO_FUNCTIONALITY);
            return;
        }

        // todo validate input
        String input = regexField.getText();

        // Defining values for automata ab+
        addTerminals("ab");

        Character[] alphabet = terminalList.toArray(new Character[0]);
        Automata<String> m = new Automata<String>(alphabet);

        // Transitions
        m.addTransition(new Transition<String>("q0", 'a', "q1"));
        m.addTransition(new Transition<String>("q1", 'b', "q0"));

        // Loops
        m.addTransition(new Transition<String>("q0", 'a'));
        m.addTransition(new Transition<String>("q1", 'b'));

        m.defineAsStartState("q0");
        m.defineAsFinalState("q1");

        List<Transition> transitionsResult = m.getTransitions();

        drawGraph(transitionsResult);

        // Log output
        displayOutput("Terminals: " + m.getAlphabet().toString());
        Collections.reverse(transitionsResult); // Reverse to correctly output
        for (Transition t : transitionsResult) displayOutput("Transition: " + t.toString());
    }

    private void drawGraph(List<Transition> transitionsResult) {
        clearGraphList();

        // Draw nodes
        for (Transition t : transitionsResult) {
            String state = t.getFromState().toString();

            if (bufferVertexList.isEmpty() || !doesVertexExist(state)) {
                bufferVertexList.add(graphList.insertVertex(state));
            }
        }

        for (Vertex<String> v : bufferVertexList) System.out.println("Node: " + v.element());

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
            label+= " ";
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

    public void addRegex(ActionEvent actionEvent) {
        Operator operator;
        try {
            operator = Operator.valueOf(operatorChoiceBox.getValue().toString());
        } catch (NullPointerException e) {
            displayError(LogErrorType.NO_OPERATOR_SELECTED);
            return;
        }

        String terminals = regexField.getText();

        if (terminals.isEmpty() && operator != Operator.PLUS && operator != Operator.STAR && operator != Operator.ONE) {
            displayError(LogErrorType.EMPTY_FIELD);
            return;
        }

        RegExp newRegex = new RegExp(terminals);
        addTerminals(terminals);
        switch (operator) {
            case PLUS:  // expr: baa+"
                if (!validatePrevRegex()) break;
                lastRegex = lastRegex.plus();
                regexString = "(" + regexString + ")+";
                break;
            case STAR:  // expr: "(a|b)*"
                if (!validatePrevRegex()) break;
                lastRegex = lastRegex.star();
                regexString = "(" + regexString + ")*";
                break;
            case OR:    // expr: "baa | bb"
                if (!validatePrevRegex()) break;
                lastRegex = lastRegex.or(newRegex);
                regexString = regexString + "|" + terminals;
                break;
            case DOT:   // expr: "b.a"
                if (!validatePrevRegex()) break;
                lastRegex = lastRegex.dot(newRegex);
                regexString = regexString + terminals;
                break;
            case ONE:   // expr: "baa"
                lastRegex = newRegex;
                regexString = terminals;
                break;
        }

        displayOutput("Taal: " + lastRegex.getLanguage(5).toString());
        displayOutput("Regex: " + regexString);
        regexField.clear();
    }

    private boolean validatePrevRegex() {
        if (lastRegex.getLanguage(3).isEmpty()) {
            displayError(LogErrorType.INVALID_OPERATOR_ACTION);
            return false;
        } else return true;
    }

    public void resetRegex(ActionEvent actionEvent) {
        resetData();
    }

    private void resetData() {
        terminalList.clear();
        regexString = "";
        lastRegex = new RegExp();
        logList.getItems().clear();
        regexField.clear();
        clearGraphList();
        displayOutput("Resetting...");
    }

    private void clearGraphList() {
        if (!graphList.vertices().isEmpty()) {
            // Needs to be in this order to prevent errors
            for (Edge<String, String> e : bufferEdgeList) graphList.removeEdge(e);
            for (Vertex<String> v : bufferVertexList) graphList.removeVertex(v);

            bufferEdgeList.clear();
            bufferVertexList.clear();

            graphView.update();
        }
    }

    private void displayOutput(String message) {
        logInfo(new Text(message), Color.BLACK);
    }

    private void displayError(LogErrorType error) {
        String errorMessage;

        switch (error) {
            case EMPTY_FIELD:
                errorMessage = "Er is geen waarde ingevoerd";
                break;
            case NO_FUNCTIONALITY:
                errorMessage = "Deze interactie heeft nog geen functionaliteit";
                break;
            case NO_OPERATOR_SELECTED:
                errorMessage = "Vergeet geen operator te kiezen";
                break;
            case INVALID_OPERATOR_ACTION:
                errorMessage = "Met de huidge regex is deze actie niet mogelijk";
                break;
            default:
                errorMessage = "Er is een fout opgetreden";
        }

        logInfo(new Text(errorMessage), Color.RED);
    }

    private void logInfo(Text text, Color color) {
        LocalDateTime now = LocalDateTime.now();
        text.setText("[" + now.getHour() + ":" + now.getMinute() + "] " + text.getText());
        text.setFill(color);
        logList.getItems().add(0, text);
    }
}
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
import main.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    private List<Vertex<String>> bufferVertexList;
    private List<Edge<String, String>> bufferEdgeList;
    private String regexString;
    private RegExp lastRegex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bufferVertexList = new ArrayList<>();
        bufferEdgeList = new ArrayList<>();
        lastRegex = new RegExp();
        regexString = "";

        initListeners();
    }

    private void initListeners() {
        regexMode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                if (regexMode.getSelectedToggle() != null) {
                    switch (RegexMode.valueOf(regexMode.getSelectedToggle().getUserData().toString())){
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

    public void onExampleButton(ActionEvent actionEvent) {
        resetData();

        displayOutput("RegEx: ab+");

        RegExp a = new RegExp("a");
        RegExp b = new RegExp("b");
        RegExp all = a.dot(b).plus();

        clearGraphList();

        bufferVertexList.add(graphList.insertVertex("A"));
        bufferVertexList.add(graphList.insertVertex("B"));

        bufferEdgeList.add(graphList.insertEdge("A", "B", "a"));
        bufferEdgeList.add(graphList.insertEdge("A", "A", " a"));
        bufferEdgeList.add(graphList.insertEdge("B", "B", "b"));
        bufferEdgeList.add(graphList.insertEdge("B", "A", " b"));

        graphView.update();

        displayOutput("Taal: " + all.getLanguage(3).toString());
    }

    public void onResultButton(ActionEvent actionEvent) {
        displayError(LogErrorType.NO_FUNCTIONALITY);
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
        regexString = "";
        lastRegex = new RegExp();
        logList.getItems().clear();
        regexField.clear();
        clearGraphList();
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
        text.setFill(color);
        logList.getItems().add(0, text);
    }
}
package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import main.logic.AutomataBuilder;
import main.logic.RegexBuilder;
import main.logic.ThompsonHandler;
import main.model.*;
import main.ui.DiagramVisualiser;
import main.ui.EnumChoiceBox;
import main.ui.LoggerBox;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    public Button runButton, resetRegex, runExample, addRegex;
    public EnumChoiceBox<Operator> operatorChoiceBox;
    public RadioButton inputMode, builderMode;
    public ToggleGroup regexMode;
    public TextField regexField;
    public TextArea sentenceBox;
    public ListView<Node> logList;

    // Helper classes
    private DiagramVisualiser diagramVisualiser;
    private ThompsonHandler thompsonHandler;
    private AutomataBuilder automataBuilder;
    private RegexBuilder regexBuilder;
    private LoggerBox loggerBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diagramVisualiser = new DiagramVisualiser();
        thompsonHandler = new ThompsonHandler();
        automataBuilder = new AutomataBuilder();
        loggerBox = new LoggerBox(logList);
        regexBuilder = new RegexBuilder();

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
                            regexField.setText("ab+");
                            break;
                        case BUILDER:
                            operatorChoiceBox.setVisible(true);
                            addRegex.setVisible(true);
                            regexField.clear();
                            break;
                    }
                    resetData();
                }
            }
        });
    }

    /* ab+ */
    public void onExampleButton(ActionEvent actionEvent) {
        resetData();

        RegExp a = new RegExp("a");
        RegExp b = new RegExp("b");
        RegExp exp = a.dot(b).plus();

        automataBuilder.addTerminals("ab");
        if (automataBuilder.init() == ExecutionResult.FAILED) {
            loggerBox.displayError(LoggerBox.LogErrorType.NO_TERMINALS_GIVEN);
            return;
        }

        automataBuilder.addTransition("q0", 'a', "q1");
        automataBuilder.addTransition("q1", 'b', "q0");
        automataBuilder.addSelfTransition("q0", 'a');
        automataBuilder.addSelfTransition("q1", 'b');
        automataBuilder.defineStart("q0");
        automataBuilder.defineFinal("q1");

        diagramVisualiser.draw(automataBuilder.get());
        loggerBox.displayAutomata(automataBuilder);
        loggerBox.displayLanguage(exp);
    }

    public void onResultButton(ActionEvent actionEvent) {
        resetData();

        if (getRegexMode() == RegexMode.BUILDER) {
            loggerBox.displayError(LoggerBox.LogErrorType.NO_FUNCTIONALITY);
            return;
        }

        NFA result = thompsonHandler.process(regexField.getText());
        loggerBox.displayTransitions(result.transitions);
    }

    public void resetRegex(ActionEvent actionEvent) {
        resetData();
    }

    public void addRegex(ActionEvent actionEvent) {
        Operator operator;
        if (regexBuilder.setOperator(getOperatorChoiceBoxValue()) == ExecutionResult.FAILED){
            loggerBox.displayError(LoggerBox.LogErrorType.NO_OPERATOR_SELECTED);
            return;
        } else operator = regexBuilder.getOperator();

        String terminals = regexField.getText();
        if (terminals.isEmpty() && operator != Operator.PLUS && operator != Operator.STAR && operator != Operator.ONE) {
            loggerBox.displayError(LoggerBox.LogErrorType.EMPTY_FIELD);
            return;
        }

        regexBuilder.init(terminals);
        loggerBox.displayOutput("Taal: " + regexBuilder.get().getLanguage(5).toString());
        loggerBox.displayOutput("Regex: " + regexBuilder.getString());
        regexField.clear();
    }

    private String getOperatorChoiceBoxValue() {
        return operatorChoiceBox.getValue().toString();
    }

    private RegexMode getRegexMode() {
        return RegexMode.valueOf(regexMode.getSelectedToggle().getUserData().toString());
    }

    private void resetData() {
        automataBuilder.reset();
        regexBuilder.reset();
        loggerBox.reset();
        diagramVisualiser.reset();
    }
}
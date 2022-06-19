package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import main.logic.AutomataBuilder;
import main.logic.RegExParser;
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
    public ToggleGroup automataType;
    public ToggleGroup languageMode;
    public TextField inputField;
    public ListView<Node> logList;
    public TextField lengthField;
    public TextField regexField;

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
                            break;
                        case BUILDER:
                            operatorChoiceBox.setVisible(true);
                            addRegex.setVisible(true);
                            break;
                    }
                    inputField.clear();
                    resetData();
                }
            }
        });

        languageMode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (regexMode.getSelectedToggle() != null) {
                    switch (getLanguageMode()) {
                        case START:
                        case CONTAINS:
                        case ENDS:
                    }
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

//        automataBuilder.addTerminals("ab");
//        if (automataBuilder.init() == ExecutionResult.FAILED) {
//            loggerBox.displayError(LoggerBox.LogErrorType.NO_TERMINALS_GIVEN);
//            return;
//        }
//
//        automataBuilder.addTransition("q0", "q1", 'a');
//        automataBuilder.addTransition("q1", "q0", 'b');
//        automataBuilder.addSelfTransition("q0", 'a');
//        automataBuilder.addSelfTransition("q1", 'b');
//        automataBuilder.defineStart("q0");
//        automataBuilder.defineFinal("q1");

//        diagramVisualiser.draw(automataBuilder.get());
//        loggerBox.displayAutomata(automataBuilder);
//        loggerBox.displayLanguage(exp, 5);
    }

    public void onResultButton(ActionEvent actionEvent) {
        if (getRegexMode() == RegexMode.BUILDER) {
            loggerBox.displayError(LoggerBox.LogErrorType.NO_FUNCTIONALITY);
            return;
        } else resetData();

        // Parse string into regex operations stack
        String regexString = regexField.getText();
        List<String> regexOperations = new RegExParser().parse(regexString);
        if (regexOperations.isEmpty()){
            loggerBox.displayError(LoggerBox.LogErrorType.INVALID_REGEX);
            return;
        } else loggerBox.displayOperations(regexOperations);

        // Build regex code based on operations stack
        RegExp result = regexBuilder.build(regexOperations);
//        int stringLength = Integer.parseInt(lengthField.getText());
//        if (regexBuilder.getTerminals().size() >= stringLength) {
//            loggerBox.displayError(LoggerBox.LogErrorType.LENGTH_CANT_BE_SMALLER_THAN_TERMINAL_SIZE);
//            return;
//        } else loggerBox.displayLanguage(result, stringLength);

        // Convert into FSM based on operations stack and terminals
        Automata<String> automata = automataBuilder.build(regexOperations, regexBuilder.getTerminals());
        diagramVisualiser.draw(automata);
        loggerBox.displayAutomata(automataBuilder.getMachine());
    }

    // todo check for invalid cases
    private String[] getTerminalPatterns(String text) {
        return text.split(",");
    }

    public void resetRegex(ActionEvent actionEvent) {
        resetData();
    }

    public void addRegex(ActionEvent actionEvent) {
//        Operator operator;
//        if (regexBuilder.setOperator(getOperatorChoiceBoxValue()) == ExecutionResult.FAILED) {
//            loggerBox.displayError(LoggerBox.LogErrorType.NO_OPERATOR_SELECTED);
//            return;
//        } else operator = regexBuilder.getOperator();
//
//        String terminals = inputField.getText();
//        if (terminals.isEmpty() && operator != Operator.PLUS && operator != Operator.STAR && operator != Operator.ONE) {
//            loggerBox.displayError(LoggerBox.LogErrorType.EMPTY_FIELD);
//            return;
//        }
//
//        regexBuilder.init(terminals);
//        loggerBox.displayOutput("Taal: " + regexBuilder.get().getLanguage(5).toString());
//        loggerBox.displayOutput("Regex: " + regexBuilder.getString());
//        inputField.clear();
    }

    private String getOperatorChoiceBoxValue() {
        return operatorChoiceBox.getValue().toString();
    }

    private RegexMode getRegexMode() {
        return RegexMode.valueOf(regexMode.getSelectedToggle().getUserData().toString());
    }

    private LanguageMode getLanguageMode() {
        return LanguageMode.valueOf(languageMode.getSelectedToggle().getUserData().toString());
    }

    private void resetData() {
        loggerBox.reset();
        diagramVisualiser.reset();
    }
}
package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import main.logic.*;
import main.model.*;
import main.ui.DiagramVisualiser;
import main.ui.EnumChoiceBox;
import main.ui.LoggerBox;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    public Button runButton, resetButton, setExample;
    public TextField lengthField, inputField, regexField;
    public ToggleGroup automataType, languageMode;
    public ListView<Node> logList;

    // Helper classes
    private DiagramVisualiser diagramVisualiser;
    private AutomataBuilder automataBuilder;
    private RegexBuilder regexBuilder;
    private LoggerBox loggerBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diagramVisualiser = new DiagramVisualiser();
        automataBuilder = new AutomataBuilder();
        loggerBox = new LoggerBox(logList);
        regexBuilder = new RegexBuilder();
    }

    public void onExampleButton(ActionEvent actionEvent) {
        int steps = 5;

        clearFields();

        String regexExample, stringExample = regexExample = "";
        AutomataType type = AutomataType.valueOf(automataType.getSelectedToggle().getUserData().toString());
        switch (type) {
            case DFA:
                regexExample = "a#(a+b)*#b";
                stringExample = "abb";
                break;
            case NFA:
                regexExample = "a+";
                stringExample = "aarde";
                break;
        }

        setFields(regexExample, steps, stringExample);
    }

    private void setFields(String regex, int steps, String string) {
        regexField.setText(regex);
        lengthField.setText(String.valueOf(steps));
        inputField.setText(string);
    }

    private void clearFields() {
        regexField.clear();
        lengthField.clear();
        inputField.clear();
    }

    public void onResetButton(ActionEvent actionEvent) {
        clearFields();
        resetData();
    }

    public void onRunButton(ActionEvent actionEvent) {
        resetData();

        // Parse string into regex operations stack
        RegexOperationSequence operationSequence = new RegExParser().parse(regexField.getText());
        if (operationSequence.failed()) {
            loggerBox.displayError(LoggerBox.LogErrorType.INVALID_REGEX);
            return;
        } else loggerBox.displayOperations(operationSequence);

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        Stack<String> postfixResult = postfixParser.parse(operationSequence);
        loggerBox.displayPostfixNotation(postfixResult);

        // Build regex code based on postfix operation result stack
        State startState = regexBuilder.build(postfixResult);
        boolean result = NfaSimulator.simulateNFA(startState, inputField.getText());

        System.out.println(result);
//        System.out.println(result.getLanguage(5));
//        int stringLength = Integer.parseInt(lengthField.getText());
//        if (regexBuilder.getTerminals().size() >= stringLength) {
//            loggerBox.displayError(LoggerBox.LogErrorType.LENGTH_CANT_BE_SMALLER_THAN_TERMINAL_SIZE);
//            return;
//        } else loggerBox.displayLanguage(result, stringLength);

        // Convert into FSM based on operations stack and terminals
//        Automata<String> automata = automataBuilder.build(regexOperations, regexBuilder.getTerminals());
//        diagramVisualiser.draw(automata);
//        loggerBox.displayAutomata(automataBuilder.getMachine());
    }

    private void resetData() {
        loggerBox.reset();
        diagramVisualiser.reset();
    }

    private LanguageMode getLanguageMode() {
        return LanguageMode.valueOf(languageMode.getSelectedToggle().getUserData().toString());
    }
}
package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import main.logic.*;
import main.model.*;
import main.ui.DiagramVisualiser;
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
    private LoggerBox loggerBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diagramVisualiser = new DiagramVisualiser();
        loggerBox = new LoggerBox(logList);
    }

    public void onExampleButton(ActionEvent actionEvent) {
        int steps = 5;

        clearFields();

        String regexExample, stringExample = regexExample = "";
        AutomataType type = AutomataType.valueOf(automataType.getSelectedToggle().getUserData().toString());
        switch (type) {
            case DFA:
                regexExample = "a(a|b)+";
                stringExample = "abb";
                break;
            case NFA:
                regexExample = "(a|b)*";
                stringExample = "a";
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

        // Validate input
        ParsedRegex parsedRegex = new RegExParser().parse(regexField.getText());
        if (parsedRegex == null) {
            loggerBox.displayError(LoggerBox.LogErrorType.INVALID_REGEX);
            return;
        } else loggerBox.displayFormattedRegex(parsedRegex);

        // Parse into postfix notation to remove parenthesis
        PostfixNotationParser postfixParser = new PostfixNotationParser();
        Stack<Character> postfixResult = postfixParser.parse(parsedRegex);
        loggerBox.displayPostfixNotation(postfixResult);

        // Generate (in)valid words with postfix
        WordGenerator wordGenerator = new WordGenerator();
        SortedSet<String> validWords = wordGenerator.generateValidWords(postfixResult, Integer.parseInt(lengthField.getText()));
        SortedSet<String> invalidWords = wordGenerator.generateFaultyWords(validWords, Integer.parseInt(lengthField.getText()));
        loggerBox.displayLanguage(validWords, true);
        loggerBox.displayLanguage(invalidWords, false);

        // Generate NDFA
        NDFA tempNDFA = new NDFA();
        tempNDFA.addTransition(new Transition("q1", "q4", "a"));
        tempNDFA.addTransition(new Transition("q1", "q2", "b"));

        tempNDFA.addTransition(new Transition("q2", "q4", "a"));
        tempNDFA.addTransition(new Transition("q2", "q1", "b"));
        tempNDFA.addTransition(new Transition("q2", "q3", "b"));
        tempNDFA.addTransition(new Transition("q2", "q3", "ε"));

        tempNDFA.addTransition(new Transition("q3", "q5", "a"));
        tempNDFA.addTransition(new Transition("q3", "q5", "b"));

        tempNDFA.addTransition(new Transition("q4", "q2", "ε"));
        tempNDFA.addTransition(new Transition("q4", "q3", "a"));

        tempNDFA.addTransition(new Transition("q5", "q4", "a"));
        tempNDFA.addTransition(new Transition("q5", "q1", "b"));

        tempNDFA.addStartState("q1");
        tempNDFA.addEndState("q4");

        diagramVisualiser.draw(tempNDFA);
    }

    private void resetData() {
        loggerBox.reset();
        diagramVisualiser.reset();
    }

    private AutomataType getAutomataType() {
        return AutomataType.valueOf(automataType.getSelectedToggle().getUserData().toString());
    }

    private LanguageMode getLanguageMode() {
        return LanguageMode.valueOf(languageMode.getSelectedToggle().getUserData().toString());
    }
}
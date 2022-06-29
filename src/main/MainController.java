package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import main.logic.*;
import main.model.*;
import main.model.automata.*;
import main.model.regex.ParsedRegex;
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
            case NDFA:
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

        // Build Automata
        AutomataBuilder automataBuilder = new AutomataBuilder();
        FA fa = automataBuilder.build(AutomataType.DFA, postfixResult);
        loggerBox.displayTransitions(fa.getTransitions());
        diagramVisualiser.draw(fa);

        // Check if valid
        if (fa instanceof DFA) {
            DFA dfa = (DFA) fa;
            loggerBox.displayMatch(dfa.isAccepted("aab"));
        }
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
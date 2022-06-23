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
                regexExample = "a(a+b)*b";
                stringExample = "abb";
                break;
            case NFA:
                regexExample = "ab";
                stringExample = "ab";
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

        // Generate words with postfix
        WordGenerator wordGenerator = new WordGenerator();
        SortedSet<String> words = wordGenerator.generate(postfixResult, Integer.parseInt(lengthField.getText()));
        loggerBox.displayLanguage(words);

        // Build expression tree based on postfix
        ExpressionTreeConstructor treeBuilder = new ExpressionTreeConstructor();
        main.model.Node root = treeBuilder.construct(postfixResult);
        treeBuilder.print(ExpressionTreeConstructor.PrintOrder.INORDER,root);

        // Build automata
        AutomataBuilder automataBuilder = new AutomataBuilder();
        Automata resultFA = automataBuilder.build(getAutomataType(), postfixResult, wordGenerator.getTerminals());
//        loggerBox.displayTransitions(resultFA.getTransitions());

        // Draw FSM
//        diagramVisualiser.draw(resultFA);
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
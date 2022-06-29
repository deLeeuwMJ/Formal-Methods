package main.ui;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.model.regex.ParsedRegex;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.Stack;

public class LoggerBox {

    private final ListView<Node> logList;

    public LoggerBox(ListView<Node> list){
        this.logList = list;
    }

    public enum LogErrorType {
        NO_FUNCTIONALITY, NO_OPERATOR_SELECTED, INVALID_OPERATOR_ACTION, EMPTY_FIELD, LENGTH_CANT_BE_SMALLER_THAN_TERMINAL_SIZE, INVALID_REGEX;
    }

    public void displayOutput(String message) {
        logInfo(new Text(message), Color.BLACK);
    }

    public void displayError(LogErrorType error) {
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
            case LENGTH_CANT_BE_SMALLER_THAN_TERMINAL_SIZE:
                errorMessage = "De lengte moet groter of gelijk zijn aan terminals";
                break;
            case INVALID_REGEX:
                errorMessage = "Er is een incorrecte regex opgegeven";
                break;
            default:
                errorMessage = "Er is een fout opgetreden";
        }

        logInfo(new Text(errorMessage), Color.RED);
    }

    public void displayFormattedRegex(ParsedRegex regexOperations) {
        displayOutput("Given expression: " + regexOperations.getRegexString());
        displayOutput("Formatted: " + regexOperations.getSequence().toString());
    }

    public void displayPostfixNotation(Stack<Character> postfixResult) {
        displayOutput("Postfix: " + postfixResult);
    }

    public void displayLanguage(SortedSet<String> set, boolean isValid) {
        displayOutput( (isValid ? "Valid"  : "Invalid") + " language: " + set);

    }

    public void displayExpressionTree(String result){
        displayOutput("Tree: " + result);
    }

//    public void displayTransitions(List<Transition> result){
//        Collections.reverse(result); // Reverse to correctly output transitions
//        for (Transition t : result) displayOutput("Trans: " + t.toString());
//    }

    public void displayMachine(String machine) {
        displayOutput("Machine: " + machine);
    }

    public void displayMatch(boolean matches) {
        displayOutput("Is valid: " + matches);
    }

    private void logInfo(Text text, Color color) {
        LocalDateTime now = LocalDateTime.now();
        text.setText("[" + now.getHour() + ":" + now.getMinute() + "] " + text.getText());
        text.setFill(color);
        logList.getItems().add(0, text);
    }

    public void reset() {
        logList.getItems().clear();
        displayOutput("Resetting...");
    }
}

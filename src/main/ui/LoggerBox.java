package main.ui;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.logic.AutomataBuilder;
import main.model.RegExp;
import main.model.Transition;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class LoggerBox {

    private final ListView<Node> logList;

    public LoggerBox(ListView<Node> list){
        this.logList = list;
    }

    public enum LogErrorType {
        NO_FUNCTIONALITY, NO_OPERATOR_SELECTED, INVALID_OPERATOR_ACTION, EMPTY_FIELD, NO_TERMINALS_GIVEN
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
            case NO_TERMINALS_GIVEN:
                errorMessage = "Er zijn nog geen terminals opgegeven";
                break;
            default:
                errorMessage = "Er is een fout opgetreden";
        }

        logInfo(new Text(errorMessage), Color.RED);
    }

    public void displayLanguage(RegExp regExp) {
        displayOutput(regExp.getLanguage(5).toString());
    }

    public void displayTransitions(List<Transition> result){
        Collections.reverse(result); // Reverse to correctly output transitions
        for (Transition t : result) displayOutput("S > " + t.toString());
    }

    public void displayAutomata(AutomataBuilder builder){
        displayTransitions(builder.get().getTransitions());
        displayOutput(builder.getMachine());
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

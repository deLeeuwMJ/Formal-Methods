package main.ui.regexp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static main.RegExMain.formatAlphabet;
import static main.RegExMain.isValid;

public class RegExpTabController implements Initializable {

    @FXML
    Button runButton;

    @FXML
    TextField alphabetText;

    @FXML
    TextField startText;

    @FXML
    Button addStartField;

    @FXML
    TextField patternText;

    @FXML
    Button addPatternField;

    @FXML
    TextField endText;

    @FXML
    Button addEndField;

    @FXML
    TextArea outputText;

    @FXML
    VBox addStartContainer;

    @FXML
    VBox addPatternContainer;

    @FXML
    VBox addEndContainer;

    private static final int MAX_FIELDS = 3;
    private List<TextField> startInputBoxes;
    private List<TextField> patternInputBoxes;
    private List<TextField> endInputBoxes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.startInputBoxes = new ArrayList<>();
        this.patternInputBoxes = new ArrayList<>();
        this.endInputBoxes = new ArrayList<>();
    }

    @FXML
    private void onResultButton(ActionEvent event) {
        String alphabet = alphabetText.getText();
        String start = startText.getText();
        String pattern = patternText.getText();
        String end = endText.getText();

        if (!isValid(alphabet, start) || !isValid(alphabet, pattern) || !isValid(alphabet, end)) return;
        displayOutput("r = " + start + "(" + formatAlphabet(alphabet) + ")*" + pattern + "(" + formatAlphabet(alphabet) + ")*" + end);
    }

    private void displayOutput(String message) {
        outputText.setStyle("-fx-text-fill: black");
        outputText.setText(message);
    }

    private enum ErrorType {
        LIMIT_REACHED
    }

    private void displayError(ErrorType error) {
        outputText.setStyle("-fx-text-fill: red");
        String errorMessage;

        switch (error) {
            case LIMIT_REACHED:
                errorMessage = "Het maximaal aantal velden van " + MAX_FIELDS + " is bereikt";
                break;
            default:
                errorMessage = "Er is een fout opgetreden";
        }
        outputText.setText(errorMessage);
    }

    @FXML
    private void addStartField(ActionEvent event) {
        addTextField(addStartContainer, InputType.START);
    }

    @FXML
    private void addPatternField(ActionEvent event) {
        addTextField(addPatternContainer, InputType.PATTERN);
    }

    @FXML
    private void addEndField(ActionEvent event) {
        addTextField(addEndContainer, InputType.END);
    }

    private enum InputType {
        START, PATTERN, END
    }

    private void addTextField(VBox box, InputType type) {
        TextField field = new TextField();

        switch (type) {
            case START:
                if (doesItExceedFieldLimit(startInputBoxes.size())) {
                    displayError(ErrorType.LIMIT_REACHED);
                    return;
                }
                startInputBoxes.add(field);
                break;
            case PATTERN:
                if (doesItExceedFieldLimit(patternInputBoxes.size())) {
                    displayError(ErrorType.LIMIT_REACHED);
                    return;
                }
                patternInputBoxes.add(field);
                break;
            case END:
                if (doesItExceedFieldLimit(endInputBoxes.size())) {
                    displayError(ErrorType.LIMIT_REACHED);
                    return;
                }
                endInputBoxes.add(field);
                break;
            default:
                return;
        }
        box.getChildren().add(field);
    }

    private boolean doesItExceedFieldLimit(int size) {
        return size >= (MAX_FIELDS - 1); // Working with size so -1
    }
}
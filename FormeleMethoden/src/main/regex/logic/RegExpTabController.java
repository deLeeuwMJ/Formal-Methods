package main.regex.logic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

import static main.regex.RegExMain.*;

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

    @FXML
    TextField extraStepsText;

    private static final int MAX_FIELDS = 3;
    private List<TextField> startInputBoxes;
    private List<TextField> patternInputBoxes;
    private List<TextField> endInputBoxes;

    private RegExpHelper regExpHelper = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.startInputBoxes = new ArrayList<>(Collections.singletonList(startText));
        this.patternInputBoxes = new ArrayList<>(Collections.singletonList(patternText));
        this.endInputBoxes = new ArrayList<>(Collections.singletonList(endText));
    }

    @FXML
    private void onResultButton(ActionEvent event) {
        // Return false on error
        if (!extractInputInformation()) {
            displayError(ErrorType.INVALID_ALPHABET);
            return;
        }

        displayOutput("r = " + regExpHelper.buildResult());
    }

    // Check if inputs are inside alphabet
    private boolean extractInputInformation() {
        List<String> alphabetList = extractIntoCharList(alphabetText.getText());
        List<String> startList = extractText(startInputBoxes);
        List<String> patternList = extractText(patternInputBoxes);
        List<String> endList = extractText(endInputBoxes);

        this.regExpHelper = new RegExpHelper(
            alphabetList, startList, patternList, endList
        );

        return !alphabetList.isEmpty() && !startList.isEmpty() && !patternList.isEmpty() && !endList.isEmpty();
    }

    // Return empty list on non valid input string
    private List<String> extractText(List<TextField> items) {
        List<String> list = new ArrayList<>();

        for (TextField t : items) {
            String text = t.getText();
            if (!isValid(alphabetText.getText(), text)) return Collections.emptyList();
            list.add(text);
        }

        return list;
    }

    @FXML
    private void onGenerateButton(ActionEvent event) {
        // Return false on error
        if (!extractInputInformation()) {
            displayError(ErrorType.INVALID_ALPHABET);
            return;
        } else if (!isValidNumberInput()) {
            displayError(ErrorType.INVALID_NUMBER);
            return;
        }

        displayOutput("Words " + regExpHelper.generateWords(Integer.parseInt(extraStepsText.getText())));
    }

    private boolean isValidNumberInput() {
        try {
            Integer.parseInt(extraStepsText.getText());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void displayOutput(String message) {
        outputText.setStyle("-fx-text-fill: black");
        outputText.setText(message);
    }

    private enum ErrorType {
        LIMIT_REACHED, INVALID_ALPHABET, INVALID_NUMBER
    }

    private void displayError(ErrorType error) {
        outputText.setStyle("-fx-text-fill: red");
        String errorMessage;

        switch (error) {
            case LIMIT_REACHED:
                errorMessage = "Het maximaal aantal velden van " + MAX_FIELDS + " is bereikt";
                break;
            case INVALID_ALPHABET:
                errorMessage = "Het bevindt zich niet in het alfabet of is dubbel";
                break;
            case INVALID_NUMBER:
                errorMessage = "Er is geen geldig getal ingevoerd";
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
        return size >= MAX_FIELDS;
    }
}
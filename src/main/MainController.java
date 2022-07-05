package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import main.logic.AutomataBuilder;
import main.logic.PostfixNotationParser;
import main.logic.RegExParser;
import main.logic.WordGenerator;
import main.model.LanguageMode;
import main.model.automata.AutomataType;
import main.model.automata.DFA;
import main.model.automata.FA;
import main.model.example.DfaExampleId;
import main.model.example.ExampleId;
import main.model.example.NdfaExampleId;
import main.model.regex.ParsedRegex;
import main.model.visual.InputType;
import main.ui.DiagramVisualiser;
import main.ui.EnumChoiceBox;
import main.ui.LoggerBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.SortedSet;

public class MainController implements Initializable, ChangeListener<Toggle> {

    public static final String DEFAULT_VALIDATE_STRING = "abab";

    @FXML
    public RadioButton noneRadioButton, startRadioButton, containRadioButton, endRadioButton, mdfaRadiobutton;
    public Button runButton, resetButton;
    public TextField lengthField, inputField, regexField;
    public ToggleGroup inputType, automataType, languageMode;
    public ListView<Node> logList;
    public EnumChoiceBox exampleChoiceBox;
    public HBox userInput;

    // Helper classes
    private DiagramVisualiser diagramVisualiser;
    private LoggerBox loggerBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diagramVisualiser = new DiagramVisualiser();
        loggerBox = new LoggerBox(logList);

        inputType.selectedToggleProperty().addListener(this);
        automataType.selectedToggleProperty().addListener(this);
        exampleChoiceBox.getSelectionModel().selectFirst();
    }

    public void onResetButton(ActionEvent actionEvent) {
        clearFields();
        resetData();
    }

    public void onRunButton(ActionEvent actionEvent) {
        resetData();

        String input = regexField.getText();

        if (getInputType() == InputType.USER) {
            switch (getLanguageMode()){
                case START:
                    DFA testDfaStartsWith = new DFA();
                    testDfaStartsWith = testDfaStartsWith.startsWith("aab");
                    diagramVisualiser.draw(testDfaStartsWith);
                    testDfaStartsWith.isAccepted("aab");
                    return;
                case CONTAINS:
                    DFA testDfaShouldContain = new DFA();
                    testDfaShouldContain = testDfaShouldContain.contains("aab");
                    diagramVisualiser.draw(testDfaShouldContain);
                    testDfaShouldContain.isAccepted("aab");
                    return;
                case ENDS:
                    DFA testDfaEndsWith = new DFA();
                    testDfaEndsWith = testDfaEndsWith.endsWith("aab");
                    diagramVisualiser.draw(testDfaEndsWith);
                    testDfaEndsWith.isAccepted("aab");
                   return;
                case NONE:
                default:
                    break;
            }

            // Validate input
            ParsedRegex parsedRegex = new RegExParser().parse(regexField.getText());
            if (parsedRegex == null) {
                loggerBox.displayError(LoggerBox.LogErrorType.INVALID_REGEX);
                return;
            } else loggerBox.displayFormattedRegex(parsedRegex);

            // Parse into postfix notation to remove parenthesis
            PostfixNotationParser postfixParser = new PostfixNotationParser();
            parsedRegex.setPostfixSequence(postfixParser.parse(parsedRegex));
            loggerBox.displayPostfixNotation(parsedRegex.getPostfixSequence());

            // Generate (in)valid words with postfix
            WordGenerator wordGenerator = new WordGenerator();
            SortedSet<String> validWords = wordGenerator.generateValidWords(parsedRegex, Integer.parseInt(lengthField.getText()));
            SortedSet<String> invalidWords = wordGenerator.generateFaultyWords(validWords, Integer.parseInt(lengthField.getText()));
            loggerBox.displayLanguage(validWords, true);
            loggerBox.displayLanguage(invalidWords, false);

            // Build Automata
            AutomataBuilder automataBuilder = new AutomataBuilder();
            FA fa = automataBuilder.build(getAutomataType(), parsedRegex);
            loggerBox.displayTransitions(fa.getTransitions());
            diagramVisualiser.draw(fa);

            // Check if valid
            if (fa instanceof DFA) {
                DFA dfa = (DFA) fa;
                loggerBox.displayMatch(dfa.isAccepted(inputField.getText()));
            }
        } else if (getInputType() == InputType.EXAMPLE) {
            // Build Automata
            AutomataBuilder automataBuilder = new AutomataBuilder();
            FA fa = automataBuilder.build(getAutomataType(), (ExampleId) exampleChoiceBox.getSelectionModel().getSelectedItem());

            if (fa == null){
                loggerBox.displayError(LoggerBox.LogErrorType.EXAMPLE_NOT_MEANT_FOR_THIS_AUTOMATA);
                return;
            }

            loggerBox.displayTransitions(fa.getTransitions());
            diagramVisualiser.draw(fa);

            // Check if valid
            if (fa instanceof DFA) {
                DFA dfa = (DFA) fa;
                loggerBox.displayMatch(dfa.isAccepted(inputField.getText()));
            }
        }
    }

    @Override
    public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        switch (newValue.getUserData().toString()) {
            case "EXAMPLE":
                updateFieldVisibility(true);
                break;
            case "USER":
                updateFieldVisibility(false);
                break;
            case "NDFA":
                updateChoiceBoxVisibility(NdfaExampleId.values(), true);
                break;
            case "MDFA":
            case "DFA":
                updateChoiceBoxVisibility(DfaExampleId.values(), false);
                break;
        }
    }

    private void updateChoiceBoxVisibility(ExampleId[] values, boolean val) {
        exampleChoiceBox.setItems(FXCollections.observableArrayList(values));
        exampleChoiceBox.getSelectionModel().selectFirst();
        updateValidateField(val);
    }

    private void updateValidateField(boolean val){
        if (val) {
            inputField.clear();
        } else inputField.setText(DEFAULT_VALIDATE_STRING);
        inputField.setDisable(val);
    }

    private void updateFieldVisibility(boolean val) {
        noneRadioButton.setSelected(true); // always reset to first choice
        exampleChoiceBox.setVisible(val);
        userInput.setVisible(!val);
        startRadioButton.setDisable(val);
        containRadioButton.setDisable(val);
        endRadioButton.setDisable(val);
        mdfaRadiobutton.setDisable(!val);
        updateValidateField(val);
        resetData();
    }

    private InputType getInputType() {
        return InputType.valueOf(inputType.getSelectedToggle().getUserData().toString());
    }

    private AutomataType getAutomataType() {
        return AutomataType.valueOf(automataType.getSelectedToggle().getUserData().toString());
    }

    private LanguageMode getLanguageMode() {
        return LanguageMode.valueOf(languageMode.getSelectedToggle().getUserData().toString());
    }

    private void clearFields() {
        regexField.clear();
        inputField.clear();
    }

    private void resetData() {
        loggerBox.reset();
        diagramVisualiser.reset();
    }
}
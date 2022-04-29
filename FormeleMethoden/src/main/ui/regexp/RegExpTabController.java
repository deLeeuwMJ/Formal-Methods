package main.ui.regexp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import main.common.views.EnumChoiceBox;
import main.common.RegExpHandler;
import main.operator.regex.RegExpOperatorType;
import java.net.URL;
import java.util.ResourceBundle;

import static main.RegExMain.formatAlphabet;
import static main.RegExMain.isValid;

public class RegExpTabController implements Initializable
{

    @FXML
    TextField alphabetText;

    @FXML
    TextField startText;

    @FXML
    TextField patternText;

    @FXML
    TextField endText;

    @FXML
    TextArea outputText;

    @FXML
    Button runButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    @FXML
    private void onResultButton(ActionEvent event)
    {
        String alphabet = alphabetText.getText();
        String start = startText.getText();
        String pattern = patternText.getText();
        String end = endText.getText();

        if (!isValid(alphabet, start) || !isValid(alphabet, pattern) || !isValid(alphabet, end)) return;
        outputText.setText("r = " + start + "(" + formatAlphabet(alphabet) + ")*" + pattern + "(" + formatAlphabet(alphabet) + ")*" + end);
    }
}
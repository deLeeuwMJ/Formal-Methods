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

public class RegExpTabController implements Initializable
{
    private final RegExpHandler regExpHandler = new RegExpHandler();

    @FXML
    TextField inputText;

    @FXML
    TextField matchText;

    @FXML
    TextArea outputText;

    @FXML
    Button runButton;

    @FXML
    EnumChoiceBox<RegExpOperatorType> operatorCb;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        operatorCb.getSelectionModel().select(0);
    }

    @FXML
    private void onResultButton(ActionEvent event)
    {
        RegExpOperatorType operator = RegExpOperatorType.valueOf(operatorCb.getValue().toString());
        outputText.setText(regExpHandler.handle(operator, inputText.getText(), matchText.getText()).toString());
    }
}
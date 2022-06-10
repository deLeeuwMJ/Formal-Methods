package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public ListView logList;
    public Button runButton;
    public TextField alphabetText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onResultButton(ActionEvent actionEvent) {

    }

    public void addRegex(ActionEvent actionEvent) {
        Main.graphList.insertVertex("R" + Math.random());
        Main.graphView.update();
    }
}
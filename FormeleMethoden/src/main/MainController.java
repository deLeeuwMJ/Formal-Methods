package main;

import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final String REG_TAB_SRC = "./regex/ui/tab.fxml";
    private static final String NDFA_TAB_SRC = "./ndfa/ui/tab.fxml";
    private static final String DFA_TAB_SRC = "./dfa/ui/tab.fxml";

    @FXML
    Tab regExpTab;

    @FXML
    Tab ndfaTab;

    @FXML
    Tab dfaTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initializeTabContent(regExpTab, REG_TAB_SRC);
            initializeTabContent(ndfaTab, NDFA_TAB_SRC);
            initializeTabContent(dfaTab, DFA_TAB_SRC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTabContent(Tab tab, String source) throws IOException {
        GridPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(source)));
        tab.setContent(pane);
    }
}
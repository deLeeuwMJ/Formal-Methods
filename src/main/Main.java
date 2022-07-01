package main;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static final String APP_TITLE = "Formele methoden";
    private static final String CSS_PATH = "main/main.css";
    private static final Integer APP_WIDTH = 1280;
    private static final Integer APP_HEIGHT = 720;

    public static SmartGraphPanel<String, String> graphView;
    public static Digraph<String, String> graphList;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));

        // Graph
        graphList = new DigraphEdgeList<>();
        graphView = new SmartGraphPanel<>(graphList, new SmartCircularSortedPlacementStrategy());
        graphView.setAutomaticLayout(true);

        // Scene
        GridPane mainPane = buildGridPane();
        mainPane.add(graphView, 0, 0);
        mainPane.add(root, 1, 0);

        Scene scene = new Scene(mainPane, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().add(CSS_PATH);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.show();

        // Needs to be after show()
        graphView.init();
    }

    // Workaround since visualisation library doesn't support FXML
    private GridPane buildGridPane() {
        GridPane pane = new GridPane();

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHalignment(HPos.CENTER);
        cc1.setPercentWidth(65);
        pane.getColumnConstraints().add(cc1);

        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHalignment(HPos.CENTER);
        cc2.setPercentWidth(35);
        pane.getColumnConstraints().add(cc2);

        RowConstraints rc = new RowConstraints();
        rc.setValignment(VPos.CENTER);
        rc.setPercentHeight(100);
        pane.getRowConstraints().add(rc);

        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

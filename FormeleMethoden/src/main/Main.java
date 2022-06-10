package main;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    private static final String APP_TITLE = "Formele methoden";
    private static final String CSS_PATH = "main/main.css";
    private static final Integer APP_WIDTH = 450;
    private static final Integer APP_HEIGHT = 450;

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
//        primaryStage.setTitle(APP_TITLE);
//        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
//        scene.getStylesheets().add(CSS_PATH);
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Digraph<String, String> g = new DigraphEdgeList<>();

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
        Scene scene = new Scene(graphView, 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Visualization");
        stage.setScene(scene);
        stage.show();

        graphView.init();
        graphView.setAutomaticLayout(true);

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");
        graphView.update();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package main.dfa.logic;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DfaTabController implements Initializable
{
    @FXML
    HBox centerBox;

    SmartGraphPanel<String, String> graphView;
    Graph<String, String> g = new GraphEdgeList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
//        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
//        graphView = new SmartGraphPanel<>(g, strategy);
//        centerBox.getChildren().add(graphView);
    }

    @Override
    protected void finalize() throws Throwable {
//        super.finalize();
//        graphView.init();
//        graphView.setAutomaticLayout(true);
//        g.insertVertex("A");
//        g.insertVertex("B");
//        g.insertEdge("A", "B", "1");
//        graphView.update();
    }
}
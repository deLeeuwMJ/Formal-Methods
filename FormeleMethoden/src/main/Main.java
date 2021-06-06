package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    private static final String APP_TITLE = "Formele methoden";
    private static final String CSS_PATH = "main/main.css";
    private static final Integer APP_WIDTH = 516;
    private static final Integer APP_HEIGHT = 387;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        primaryStage.setTitle(APP_TITLE);
        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().add(CSS_PATH);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

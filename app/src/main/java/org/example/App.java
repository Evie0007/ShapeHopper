package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Label title = new Label("ShapeHopper");
        StackPane root = new StackPane(title); // Create a StackPane and add the title label to it
        Scene scene = new Scene(root); // Create a scene with the StackPane as the root node
        stage.setTitle("ShapeHopper"); // Set the title of the stage
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
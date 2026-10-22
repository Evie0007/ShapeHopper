package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Pane()); 

        
        scene.getStylesheets().add(getClass().getResource("/org/example/style.css").toExternalForm());

        stage.setTitle("ShapeHopper");
        stage.setMaximized(true);
        stage.setScene(scene);
        Navigator.showMenu(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
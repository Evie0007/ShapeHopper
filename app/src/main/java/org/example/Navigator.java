package org.example;

import javafx.scene.Parent;
import javafx.stage.Stage;

/** Switches between screens in the same window. */
public class Navigator {

    public static void show(Stage stage, Parent screen) {
        screen.setStyle("-fx-font-family: '" + Theme.handwrittenFont() + "';");
        stage.getScene().setRoot(screen);
    }

    public static void showMenu(Stage stage) {
        show(stage, new LandingScreen(stage).build());
    }
}
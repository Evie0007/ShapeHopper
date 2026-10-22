package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Shape Gallery: every shape in the game with its name. */
public class GalleryScreen {

    private final Stage stage;

    public GalleryScreen(Stage stage) {
        this.stage = stage;
    }

    public Parent build() {
        Label title = new Label("Shape Gallery");
        title.getStyleClass().add("screen-title");

        // 7 columns, like the sketch
        TilePane grid = new TilePane(30, 30);
        grid.setPrefColumns(7);
        grid.setMaxWidth(Region.USE_PREF_SIZE);
        grid.setAlignment(Pos.CENTER);
        for (String name : ShapeLibrary.NAMES) {
            grid.getChildren().add(createCard(name));
        }

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> Navigator.showMenu(stage));

        BorderPane root = new BorderPane();
        root.setTop(title);
        root.setCenter(grid);
        root.setBottom(backButton);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setPadding(new Insets(30, 50, 30, 50));
        return root;
    }

    /** One shape with its name underneath. */
    private VBox createCard(String name) {
        StackPane picture = new StackPane(ShapeLibrary.draw(name));
        picture.setPrefSize(110, 110); // same space for every shape so the names line up

        Label label = new Label(name);
        label.getStyleClass().add("shape-name");

        VBox card = new VBox(8, picture, label);
        card.setAlignment(Pos.CENTER);
        return card;
    }
}
package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/** Gameplay History. Shows placeholders until the game saves real results. */
public class HistoryScreen {

    private final Stage stage;

    public HistoryScreen(Stage stage) {
        this.stage = stage;
    }

    public Parent build() {
        Label title = new Label("Gameplay History");
        title.getStyleClass().add("screen-title");

        VBox stats = new VBox(14,
                styled(new Label("Highest score:  —"), "stat-label"),
                styled(new Label("Shapes discovered:  —"), "stat-label"),
                styled(new Label("Challenges:  —"), "stat-label"),
                styled(new Label("Record:"), "stat-label"),
                styled(new Label("No games played yet"), "record-label"));
        stats.setAlignment(Pos.CENTER_LEFT);

        HBox content = new HBox(120, stats, createMascot());
        content.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> Navigator.showMenu(stage));

        BorderPane root = new BorderPane();
        root.setTop(title);
        root.setCenter(content);
        root.setBottom(backButton);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setPadding(new Insets(30, 50, 30, 50));
        return root;
    }

    private Label styled(Label label, String styleClass) {
        label.getStyleClass().add(styleClass);
        return label;
    }

    /** The cheering square from the sketch. */
    private Group createMascot() {
        Arc mouth = new Arc(0, 12, 18, 14, 180, 180);
        mouth.setType(ArcType.CHORD);

        Label yay = new Label("Yay!");
        yay.getStyleClass().add("mascot-text");
        yay.setLayoutX(45);
        yay.setLayoutY(-115);

        return new Group(
                Theme.sketch(new Rectangle(-50, -50, 100, 100)),          // body
                Theme.sketch(new Circle(-18, -15, 10)),                   // left eye
                Theme.sketch(new Circle(18, -15, 10)),                    // right eye
                new Circle(-16, -13, 4),                                  // pupils (solid)
                new Circle(20, -13, 4),
                Theme.sketch(mouth),
                Theme.sketch(new QuadCurve(-50, 0, -85, -5, -75, -50)),  // left arm
                Theme.sketch(new QuadCurve(50, 0, 85, -5, 75, -50)),     // right arm
                Theme.sketch(new Line(-20, 50, -32, 115)),               // left leg
                Theme.sketch(new Line(20, 50, 30, 115)),                 // right leg
                yay);
    }
}
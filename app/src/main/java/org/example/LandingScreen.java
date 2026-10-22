package org.example;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/** The main menu: title and buttons in front, floating shapes behind. */
public class LandingScreen {

    private final Stage stage;

    public LandingScreen(Stage stage) {
        this.stage = stage;
    }

    /** Builds the screen. The first layer in the StackPane sits at the back. */
    public Parent build() {
        StackPane root = new StackPane(createBackground(), createMenu());
        root.setStyle("-fx-font-family: '" + Theme.handwrittenFont() + "';");
        return root;
    }

    // --- Menu (front layer) ---

    private VBox createMenu() {
        Label title = new Label("ShapeHopper");
        title.setId("title");

        Button startButton = menuButton("Start");
        Button galleryButton = menuButton("Shapes Gallery");
        Button historyButton = menuButton("History");
        Button aboutButton = menuButton("About");
        Button exitButton = menuButton("Exit");

        startButton.setOnAction(e -> Navigator.show(stage, new GameScreen(stage).build()));
        galleryButton.setOnAction(e -> Navigator.show(stage, new GalleryScreen(stage).build()));
        historyButton.setOnAction(e -> Navigator.show(stage, new HistoryScreen(stage).build()));
        aboutButton.setOnAction(e -> System.out.println("About: coming soon"));
        exitButton.setOnAction(e -> stage.close());

        VBox menu = new VBox(14, title, startButton, galleryButton, historyButton, aboutButton, exitButton);
        menu.setAlignment(Pos.CENTER);
        return menu;
    }

    private Button menuButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button"); // the look lives in style.css
        return button;
    }

    // --- Background (back layer) ---

    private Pane createBackground() {
        Pane pane = new Pane();
        pane.setMinSize(0, 0); // let the window shrink freely

        Polyline ground = new Polyline();
        sketch(ground);
        pane.getChildren().add(ground);

        // Redraw the ground whenever the window changes size
        pane.widthProperty().addListener(e -> drawGround(ground, pane));
        pane.heightProperty().addListener(e -> drawGround(ground, pane));

        // x and y are fractions of the window (0.1 = 10% from the left/top),
        // so the shapes stay in place on any screen size
        //             shape                                         x     y    tilt  seconds
        addShape(pane, cylinder(),                                   0.14, 0.18,  0,  3.4);
        addShape(pane, sketch(new Circle(45)),                       0.86, 0.20,  0,  4.0);
        addShape(pane, sketch(new Rectangle(-38, -38, 76, 76)),      0.11, 0.46, -8,  3.0);
        addShape(pane, sketch(new Rectangle(-55, -32, 110, 64)),     0.83, 0.47, 22,  3.7);
        addShape(pane, sketch(new Polygon(0, -45, 45, 38, -45, 38)), 0.10, 0.74,  0,  3.2);
        addShape(pane, star(),                                       0.78, 0.72, 12,  4.4);
        return pane;
    }

    /** Gives a shape the black-outline look from style.css. */
    private Shape sketch(Shape shape) {
        shape.getStyleClass().add("sketch");
        return shape;
    }

    /** Tilts a shape, places it on the screen, and makes it float. */
    private void addShape(Pane pane, Node shape, double x, double y, double tilt, double seconds) {
        shape.setRotate(tilt);
        shape.layoutXProperty().bind(pane.widthProperty().multiply(x));
        shape.layoutYProperty().bind(pane.heightProperty().multiply(y));
        pane.getChildren().add(shape);
        startFloating(shape, seconds);
    }

    // Shape gently floate up and down
    private void startFloating(Node shape, double seconds) {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(seconds), shape);
        floating.setByY(-18);                             // drift up 18 pixels
        floating.setAutoReverse(true);                    // then drift back down
        floating.setCycleCount(Animation.INDEFINITE);     // keep going
        floating.setInterpolator(Interpolator.EASE_BOTH); // slow down at the top and bottom
        floating.play();
    }

    // --- Shape builders ---

    // Cylinder
    private Group cylinder() {
        Arc bottom = new Arc(0, 35, 26, 9, 180, 180); // bottom half of an oval
        bottom.setType(ArcType.OPEN);

        return new Group(
                sketch(new Ellipse(0, -35, 26, 9)),
                sketch(new Line(-26, -35, -26, 35)),
                sketch(new Line(26, -35, 26, 35)),
                sketch(bottom));
    }

    // Star 
    private Shape star() {
        Polygon star = new Polygon();
        for (int i = 0; i < 10; i++) {
            double radius = (i % 2 == 0) ? 42 : 18;
            double angle = Math.toRadians(i * 36 - 90); // -90 starts at the top point
            star.getPoints().addAll(radius * Math.cos(angle), radius * Math.sin(angle));
        }
        return sketch(star);
    }

    // Wave line 
    private void drawGround(Polyline ground, Pane pane) {
        double baseY = pane.getHeight() * 0.88;
        ground.getPoints().clear();

        for (double x = 0; x <= pane.getWidth(); x += 8) {
            // Two waves mixed together look more hand-drawn than one
            double y = baseY + Math.sin(x / 90) * 6 + Math.sin(x / 35) * 2;
            ground.getPoints().addAll(x, y);
        }
    }

}
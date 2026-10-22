package org.example;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;

/** The playing screen: info bar on top, the track in the middle, the answer box at the bottom. */
public class GameScreen {

    private final Stage stage;

    // Fields so the game rules can update them later
    private Label roundLabel;
    private Label progressLabel;
    private TextField answerInput;
    private TranslateTransition hoverAnimation;
    private VBox pauseMenu;

    public GameScreen(Stage stage) {
        this.stage = stage;
    }

    public Parent build() {
        BorderPane layout = new BorderPane();
        layout.setTop(createTopBar());
        layout.setCenter(createPlayArea());
        layout.setBottom(createBottomBar());

        pauseMenu = createPauseMenu();
        Platform.runLater(() -> answerInput.requestFocus()); // ready to type right away
        return new StackPane(layout, pauseMenu);            // pause menu sits on top
    }

    // --- Top: round, title, progress ---

    private BorderPane createTopBar() {
        roundLabel = new Label("Round 1");
        Label titleLabel = new Label("ShapeHopper");
        progressLabel = new Label("1/5");

        roundLabel.getStyleClass().add("hud-label");
        progressLabel.getStyleClass().add("hud-label");
        titleLabel.setId("game-title");

        BorderPane topBar = new BorderPane();
        topBar.setLeft(roundLabel);
        topBar.setCenter(titleLabel);
        topBar.setRight(progressLabel);
        topBar.setPadding(new Insets(20, 40, 20, 40));
        return topBar;
    }

    // --- Center: track, character, shape ---

    private Pane createPlayArea() {
        Pane playArea = new Pane();
        playArea.setMinSize(0, 0);

        Line track = new Line();
        Theme.sketch(track);
        track.startXProperty().bind(playArea.widthProperty().multiply(0.08));
        track.endXProperty().bind(playArea.widthProperty().multiply(0.92));
        track.startYProperty().bind(playArea.heightProperty().multiply(0.7));
        track.endYProperty().bind(playArea.heightProperty().multiply(0.7));

        Polyline divider = new Polyline();
        Theme.sketch(divider);
        playArea.widthProperty().addListener(e -> drawWave(divider, playArea));
        playArea.heightProperty().addListener(e -> drawWave(divider, playArea));

        playArea.getChildren().addAll(track, divider);
        placeOnTrack(playArea, createCharacter(), 0.25);

        Node shape = ShapeLibrary.draw("triangle"); // the rules will choose shapes later
        placeOnTrack(playArea, shape, 0.55);
        startHovering(shape);
        return playArea;
    }

    /** x is a fraction of the width (0.5 = middle). */
    private void placeOnTrack(Pane playArea, Node node, double x) {
        node.layoutXProperty().bind(playArea.widthProperty().multiply(x));
        node.layoutYProperty().bind(playArea.heightProperty().multiply(0.7));
        playArea.getChildren().add(node);
    }

    /** Shapes are drawn around their center, so lift them about 50px to clear the track. */
    private void startHovering(Node shape) {
        hoverAnimation = new TranslateTransition(Duration.seconds(1.5), shape);
        hoverAnimation.setFromY(-50);
        hoverAnimation.setToY(-65);
        hoverAnimation.setAutoReverse(true);
        hoverAnimation.setCycleCount(Animation.INDEFINITE);
        hoverAnimation.setInterpolator(Interpolator.EASE_BOTH);
        hoverAnimation.play();
    }

    /** Stick figure with its feet at (0, 0). */
    private Group createCharacter() {
        return new Group(
                Theme.sketch(new Circle(0, -70, 12)),     // head
                Theme.sketch(new Line(0, -58, 0, -25)),   // body
                Theme.sketch(new Line(0, -48, -16, -35)), // left arm
                Theme.sketch(new Line(0, -48, 16, -38)),  // right arm
                Theme.sketch(new Line(0, -25, -12, 0)),   // left leg
                Theme.sketch(new Line(0, -25, 12, 0)));   // right leg
    }

    /** Wavy line along the bottom of the play area. */
    private void drawWave(Polyline wave, Pane pane) {
        double baseY = pane.getHeight() - 15;
        wave.getPoints().clear();
        for (double x = 0; x <= pane.getWidth(); x += 8) {
            double y = baseY + Math.sin(x / 90) * 7 + Math.sin(x / 35) * 2;
            wave.getPoints().addAll(x, y);
        }
    }


    private BorderPane createBottomBar() {
        answerInput = new TextField();
        answerInput.setPromptText("Type the shape's name...");
        answerInput.getStyleClass().add("answer-input");
        HBox.setHgrow(answerInput, Priority.ALWAYS); // box takes the extra width

        Button submitButton = new Button();
        submitButton.setGraphic(checkMark());
        submitButton.getStyleClass().add("submit-button");
        submitButton.setMaxHeight(Double.MAX_VALUE); // same height as the box

        Button hintButton = new Button("hint?");
        hintButton.getStyleClass().add("hint-button");

        Button pauseButton = new Button("II");
        pauseButton.getStyleClass().add("pause-button");

        answerInput.setOnAction(e -> submitAnswer());  // Enter key
        submitButton.setOnAction(e -> submitAnswer()); // ✓ button
        hintButton.setOnAction(e -> System.out.println("Hint clicked"));
        pauseButton.setOnAction(e -> pause());

        HBox inputRow = new HBox(answerInput, submitButton);
        VBox inputArea = new VBox(6, inputRow, hintButton);
        inputArea.setAlignment(Pos.CENTER_RIGHT);
        inputArea.setMaxWidth(460);

        BorderPane bottomBar = new BorderPane();
        bottomBar.setCenter(inputArea);
        bottomBar.setLeft(pauseButton);
        BorderPane.setAlignment(pauseButton, Pos.BOTTOM_LEFT);
        bottomBar.setPadding(new Insets(20, 40, 30, 40));
        return bottomBar;
    }

    private Node checkMark() {
        Polyline check = new Polyline(0, 8, 6, 14, 16, 0);
        check.getStyleClass().add("check-icon");
        return check;
    }

    /** Placeholder until the rules exist: print the answer and clear the box. */
    private void submitAnswer() {
        System.out.println("Answer: " + answerInput.getText());
        answerInput.clear();
    }

    // --- Pause menu ---

    private VBox createPauseMenu() {
        Label pausedLabel = new Label("Paused");
        pausedLabel.setId("title");

        Button resumeButton = new Button("Resume");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit to Menu");
        resumeButton.getStyleClass().add("menu-button");
        settingsButton.getStyleClass().add("menu-button");
        exitButton.getStyleClass().add("menu-button");

        resumeButton.setOnAction(e -> resume());
        settingsButton.setOnAction(e -> System.out.println("Settings: coming soon"));
        exitButton.setOnAction(e -> {
            hoverAnimation.stop(); // don't leave the animation running in the background
            Navigator.showMenu(stage);
        });

        VBox menu = new VBox(14, pausedLabel, resumeButton, settingsButton, exitButton);
        menu.setAlignment(Pos.CENTER);
        menu.getStyleClass().add("pause-overlay");
        menu.setVisible(false);
        return menu;
    }

    private void pause() {
        pauseMenu.setVisible(true);
        hoverAnimation.pause();
    }

    private void resume() {
        pauseMenu.setVisible(false);
        hoverAnimation.play();
        answerInput.requestFocus();
    }
}
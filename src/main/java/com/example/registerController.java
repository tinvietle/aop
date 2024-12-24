package com.example;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterController {

    @FXML
    private Spinner<Integer> playerCountSpinner;

    @FXML
    private VBox playerNamesContainer;

    @FXML
    private Button backButton;

    @FXML
    private Button startGameButton;

    @FXML
    private StackPane registerRootPane;

    @FXML
    public void initialize() {
        // Set background image
        String imagePath = Paths.get("assets\\register.jpg").toUri().toString();
        Image backgroundImage = new Image(imagePath);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
        registerRootPane.setBackground(new Background(background));

        // Initialize the Spinner with value factory (updated max value to 6)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2);
        playerCountSpinner.setValueFactory(valueFactory);

        // Add listener to spinner to generate player name fields
        playerCountSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePlayerNameFields(newVal);
            }
        });

        // Initialize with default number of players (2)
        updatePlayerNameFields(2);

        // Set up button event handlers
        backButton.setOnAction(event -> handleBack());
        startGameButton.setOnAction(event -> handleStartGame());
    }

    private void updatePlayerNameFields(int count) {
        playerNamesContainer.getChildren().clear();
        for (int i = 1; i <= count; i++) {
            TextField nameField = new TextField();
            nameField.setPromptText("Player " + i + " Name");
            playerNamesContainer.getChildren().add(nameField);
        }
    }

    private void handleBack() {
        try {
            // Load the menu screen first
            Parent menuRoot = App.loadFXML("menu");
            Scene currentScene = registerRootPane.getScene();
            double sceneWidth = currentScene.getWidth();
            
            // Create a container for both screens
            StackPane container = new StackPane();
            menuRoot.setTranslateX(-sceneWidth);
            container.getChildren().addAll(registerRootPane, menuRoot);
            
            // Set the container as the new root
            currentScene.setRoot(container);
            
            // Create parallel transitions for both screens
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), registerRootPane);
            slideOut.setToX(sceneWidth);
            
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), menuRoot);
            slideIn.setToX(0);
            
            // Play both transitions
            slideOut.play();
            slideIn.play();
            
            // When animation finishes, clean up
            slideIn.setOnFinished(event -> {
                container.getChildren().remove(registerRootPane);
            });
            
        } catch (Exception e) {
            System.err.println("Error during transition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleStartGame() {
        // Retrieve player names
        int playerCount = playerCountSpinner.getValue();
        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            TextField nameField = (TextField) playerNamesContainer.getChildren().get(i);
            playerNames[i] = nameField.getText().trim();
            if (playerNames[i].isEmpty()) {
                System.err.println("Player " + (i + 1) + " name is empty.");
                return;
            }
        }

        System.out.println("Starting game with players:");
        for (String name : playerNames) {
            System.out.println(name);
        }

        // Start the game
        try {
            // Load the FXML file and create the scene
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("game.fxml"));
            Parent gameRoot = fxmlLoader.load();
            Scene gameScene = new Scene(gameRoot);

            // Retrieve the controller after the FXML is loaded
            GameController controller = fxmlLoader.getController();

            Stage stage = (Stage) startGameButton.getScene().getWindow();

            stage.setTitle("Age of Pokemon");
            stage.setScene(gameScene);
            stage.setMaximized(true); // Maximize the window
            stage.setResizable(true); 
            stage.centerOnScreen();
            stage.show();

            controller.registerPlayer(playerNames);
            controller.setCurPlayer(playerNames[0]);

            // Stop the background video before starting the game
            System.out.println("Stopping background video before starting the game");
            VideoPlayer.stopBackgroundVideo();

            Parent startGameRoot = App.loadFXML("game"); // Load the primary.fxml file
            Scene scene = startGameButton.getScene(); // Get the current scene
            scene.setRoot(startGameRoot); // Set the new root
        } catch (IOException e) {
            System.out.println("Error loading .fxml file: " + e.getMessage());
        }
    }
}


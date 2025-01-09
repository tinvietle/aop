package com.example.menu;


import com.example.App;
import com.example.misc.SoundManager;
import com.example.misc.Utils;
import com.example.settings.SettingsController;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuController {

    @FXML
    private MediaView backgroundVideo;

    @FXML
    private Button startButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button exitButton;

    @FXML
    private StackPane rootPane;  // Add this FXML injection for the root pane

    @FXML
    private Label titleText;

    @FXML
    public void initialize() {
        // Load Pocket Monk font
        Font.loadFont(getClass().getResourceAsStream("/com/example/assets/font/PocketMonk.otf"), 14);

        // Stop any currently playing background music
        SoundManager.getInstance().stopBGM();
        
        // Play a random BGM track
        SoundManager.getInstance().playRandomBGM();

        // Initialize the background video player
        VideoPlayer.playBackgroundVideo(backgroundVideo);

        backgroundVideo.setPreserveRatio(false);

        // Bind background video size to the root pane size
        backgroundVideo.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundVideo.fitHeightProperty().bind(rootPane.heightProperty());

        // Bind buttons width to the root pane width
        startButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.6));
        settingsButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.6));
        exitButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.6));

        // Bind buttons height to the root pane height
        startButton.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.1));
        settingsButton.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.1));
        exitButton.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.1));

        // Bind font size to scene width, and add other attributes: bold, black
        titleText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.1), ";",
                "-fx-font-weight: bold; ",
                "-fx-text-fill: black;",
                "-fx-font-family: 'Pocket Monk';"));

        // Bind button font size to scene width, and add other attributes: bold, black
        startButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.02), ";",
                "-fx-text-fill: black;"));
        settingsButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.02), ";",
                "-fx-text-fill: black;"));
        exitButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.02), ";",
                "-fx-text-fill: black;"));

        // Set up button event handlers
        startButton.setOnAction(event -> handleStartGame());
        settingsButton.setOnAction(event -> handleSettings());
        exitButton.setOnAction(event -> handleExit());
    }

    private void handleStartGame() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            // Load the next screen first
            Parent registerRoot = App.loadFXML("register/register");
            Scene currentScene = rootPane.getScene();
            double sceneWidth = currentScene.getWidth();
            
            // Create a container for both screens
            StackPane container = new StackPane();
            registerRoot.setTranslateX(sceneWidth);
            container.getChildren().addAll(rootPane, registerRoot);
            
            // Set the container as the new root
            currentScene.setRoot(container);
            
            // Create parallel transitions for both screens
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(250), rootPane);
            slideOut.setToX(-sceneWidth);
            
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), registerRoot);
            slideIn.setToX(0);
            
            // Play both transitions
            slideOut.play();
            slideIn.play();
            
            // When animation finishes, clean up
            slideIn.setOnFinished(event -> {
                VideoPlayer.stopBackgroundVideo(backgroundVideo);
                container.getChildren().remove(rootPane);
            });
            
        } catch (Exception e) {
            System.err.println("Error during transition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSettings() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/settings/settings.fxml"));
            Parent settingsRoot = loader.load();
            Scene settingsScene = new Scene(settingsRoot);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            
            // Get the controller and set the previous scene
            SettingsController settingsController = loader.getController();
            settingsController.setPreviousScene(stage, stage.getScene());
            
            // Switch to settings scene
            stage.setScene(settingsScene);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleExit() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        System.out.println("Exiting application");
        Utils.closeProgram();
    }
}
package com.example.menu;


import com.example.App;
import com.example.misc.Utils;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.MediaView; 

import javafx.scene.layout.StackPane;
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
    public void initialize() {
        // Initialize the background video player
        VideoPlayer.playBackgroundVideo(backgroundVideo);

        backgroundVideo.setPreserveRatio(false);

        // Set up button event handlers
        startButton.setOnAction(event -> handleStartGame());
        settingsButton.setOnAction(event -> handleSettings());
        exitButton.setOnAction(event -> handleExit());
    }

    private void handleStartGame() {
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
        try {
            // Update the path to match your project structure
            Parent settingsRoot = App.loadFXML("settings/settings");
            Scene currentScene = rootPane.getScene();
            double sceneWidth = currentScene.getWidth();
            
            // Create a container for both screens
            StackPane container = new StackPane();
            settingsRoot.setTranslateX(sceneWidth);
            container.getChildren().addAll(rootPane, settingsRoot);
            
            // Set the container as the new root
            currentScene.setRoot(container);
            
            // Create transitions for both screens
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), rootPane);
            slideOut.setToX(-sceneWidth);
            
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), settingsRoot);
            slideIn.setToX(0);
            
            // Play both transitions
            slideOut.play();
            slideIn.play();
            
            // When animation finishes, clean up
            slideIn.setOnFinished(event -> {
                container.getChildren().remove(rootPane);
            });
            
        } catch (Exception e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleExit() {
        System.out.println("Exiting application");
        Utils.closeProgram();
    }
}
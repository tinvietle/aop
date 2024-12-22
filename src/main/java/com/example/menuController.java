package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.MediaView; 


public class menuController {

    // @FXML
    // private StackPane rootPane; 

    @FXML
    private MediaView backgroundVideo;

    @FXML
    private Button startButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        // Initialize the background video player
        VideoPlayer.playBackgroundVideo(backgroundVideo);

        // Bind MediaView size to StackPane size
        // backgroundVideo.fitWidthProperty().bind(rootPane.widthProperty());
        // backgroundVideo.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundVideo.setPreserveRatio(false);

        // Set up button event handlers
        startButton.setOnAction(event -> handleStartGame());
        settingsButton.setOnAction(event -> handleSettings());
        exitButton.setOnAction(event -> handleExit());
    }

    private void handleStartGame() {
        try {
            Parent startGameRoot = App.loadFXML("register"); // Load the StartGame.fxml file
            Scene scene = startButton.getScene(); // Get the current scene
            scene.setRoot(startGameRoot); // Set the new root
            
        } catch (IOException e) {
            System.err.println("Error loading .fxml file: " + e.getMessage());
        }
    }

    private void handleSettings() {
        
    }

    private void handleExit() {
        Utils.closeProgram();
    }
}
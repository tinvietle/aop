package com.example.capture;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class OnlyMedia {

    @FXML
    private Pane Pane;

    @FXML
    private MediaView mediaView;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Stage primaryStage;
    private Scene previousScene;

    public void initializeMedia(String mediaPath, double scale) {
        // Load the media file
        media = new Media(mediaPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Calculate width and height based on scale and 16:9 ratio
        double targetWidth = screenWidth * scale;
        double targetHeight = targetWidth / (16.0 / 9.0);

        // Ensure it fits within the screen height
        if (targetHeight > screenHeight * scale) {
            targetHeight = screenHeight * scale;
            targetWidth = targetHeight * (16.0 / 9.0);
        }

        // Set the calculated dimensions to the MediaView
        mediaView.setFitWidth(targetWidth);
        mediaView.setFitHeight(targetHeight);
        mediaView.setPreserveRatio(true);

        // Center the MediaView (optional, if you use a larger Pane)
        mediaView.setLayoutX((Pane.getPrefWidth() - targetWidth) / 2);
        mediaView.setLayoutY((Pane.getPrefHeight() - targetHeight) / 2);

        // Create the message label
        Label messageLabel = new Label("Click anywhere to return.");
        messageLabel.setStyle("-fx-font-size: 24px; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: rgba(0, 0, 0, 0.7); " +
            "-fx-padding: 15px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px;");
        messageLabel.setVisible(false);

        // Wrap the MediaView and Label in a StackPane to overlay the label
        StackPane mediaContainer = new StackPane(mediaView, messageLabel);
        BorderPane.setAlignment(mediaContainer, javafx.geometry.Pos.CENTER);
        Pane.getChildren().add(mediaContainer);

        // Set action to show the text and blur the video when playback ends
        mediaPlayer.setOnEndOfMedia(() -> {
            // Apply blur effect to the video
            GaussianBlur blur = new GaussianBlur(15);
            mediaView.setEffect(blur);

            // Show the label
            messageLabel.setVisible(true);

            // Allow clicking anywhere to return to the previous scene
            Pane.setOnMouseClicked(event -> {
                if (primaryStage != null && previousScene != null) {
                    primaryStage.setScene(previousScene);
                    primaryStage.centerOnScreen();
                }
            });
        });

        // Set the media player to auto play the video
        mediaPlayer.setAutoPlay(true);
    }

    // Method to set reference to the stage and the previous scene
    public void setPreviousScene(@SuppressWarnings("exports") Stage primaryStage, @SuppressWarnings("exports") Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }
}

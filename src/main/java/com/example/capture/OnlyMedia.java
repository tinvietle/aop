package com.example.capture;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class OnlyMedia {

    @FXML
    private StackPane Pane;

    @FXML
    private MediaView mediaView;
    
    @FXML
    private Label messageLabel;

    @FXML
    private Button skipButton;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Stage primaryStage;
    private Scene previousScene;
    private boolean isMediaLoading = false;
    private Runnable onVideoFinished;

    public void setOnVideoFinished(Runnable callback) {
        this.onVideoFinished = callback;
    }

    public void initializeMedia(String mediaPath, double width, double height) {
        try {
            if (isMediaLoading) {
                System.out.println("Media is currently loading, please wait...");
                return;
            }

            stopMedia();
            initializeMediaPlayer(mediaPath, width, height);

        } catch (Exception e) {
            System.err.println("Error initializing media: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeMediaPlayer(String mediaPath, double width, double height) {
        try {
            isMediaLoading = true;
            
            // Create new Media instance
            media = new Media(mediaPath);
            mediaPlayer = new MediaPlayer(media);
            
            // Set up media player event handlers
            setupMediaPlayerEvents(mediaPath, width, height);
            
            // Apply media view settings
            configureMediaView(width, height);
            
            // Apply media player settings
            configureMediaPlayer();
            
            // Play media when initialized
            playMedia();

        } catch (Exception e) {
            System.err.println("Error initializing media player: " + e.getMessage());
            e.printStackTrace();
            isMediaLoading = false;
        }
    }

    private void setupMediaPlayerEvents(String mediaPath, double width, double height) {
        mediaPlayer.setOnReady(() -> {
            System.out.println("Media is ready");
            isMediaLoading = false;
        });

        mediaPlayer.setOnError(() -> {
            System.err.println("Media Player Error: " + mediaPlayer.getError().getMessage());
            isMediaLoading = false;
            cleanup();
            // Attempt to reinitialize after error
            initializeMediaPlayer(mediaPath, width, height);
        });

        messageLabel.setVisible(false);

        mediaPlayer.setOnEndOfMedia(() -> {
            GaussianBlur blur = new GaussianBlur(15);
            mediaView.setEffect(blur);

            messageLabel.setVisible(true);
            Pane.setOnMouseClicked(event -> {
                handleVideoEnd();
            });
        });

        mediaPlayer.statusProperty().addListener((observable, oldStatus, newStatus) -> {
            System.out.println("MediaPlayer Status: " + newStatus);
        });
    }

    private void configureMediaView(double width, double height) {
        StackPane.setAlignment(mediaView, Pos.CENTER);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(false);
        // mediaView.setFitWidth(width);
        // mediaView.setFitHeight(height);
        // Bind the media view size to the parent pane
        mediaView.fitWidthProperty().bind(Pane.widthProperty());
        mediaView.fitHeightProperty().bind(Pane.heightProperty());
        messageLabel.prefWidthProperty().bind(Pane.widthProperty().multiply(335.0 / 800.0));
        messageLabel.prefHeightProperty().bind(Pane.heightProperty().multiply(72.0 / 500.0));
        messageLabel.styleProperty().bind(Bindings.concat(
            "-fx-font-size:", Pane.heightProperty().multiply(28.0/500.0), ";",
            "-fx-background-radius:", Pane.heightProperty().multiply(10.0/500.0), "px;",
            "fx-border-radius:", Pane.heightProperty().multiply(1.0/500.0), "px;",
            "fx-padding:", Pane.heightProperty().multiply(15.0/500.0), "px;"
        ));
    }

    private void configureMediaPlayer() {
        mediaPlayer.setAutoPlay(false); // Remove this line
        
        // Add error recovery
        mediaPlayer.setOnStalled(() -> {
            System.out.println("Media playback stalled, attempting to resume...");
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
        });
    }

    public void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopMedia() {
        cleanup();
    }

    private void cleanup() {
        if (mediaView != null) {
            mediaView.setMediaPlayer(null);
        }

        if (mediaPlayer != null) {
            System.out.println("Cleaning up media player resources");
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        if (media != null) {
            media = null;
        }

        isMediaLoading = false;
    }

    private void handleVideoEnd() {
        if (onVideoFinished != null) {
            onVideoFinished.run();
        }
        returnToPreviousScene();
    }

    private void returnToPreviousScene() {
        if (primaryStage != null && previousScene != null) {
            primaryStage.setScene(previousScene);
            primaryStage.centerOnScreen();
        }
    }

    // Method to set reference to the stage and the previous scene
    public void setPreviousScene(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @FXML
    public void onSkip(ActionEvent event) {
        stopMedia();
        handleVideoEnd();
    }
}

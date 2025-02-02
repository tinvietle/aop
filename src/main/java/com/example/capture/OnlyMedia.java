/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.capture;

import com.example.misc.SoundManager;

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

/*
 * Handles media playback and display in a JavaFX application.
 * This class manages video content with features like:
 * - Media loading and playback control
 * - Error handling and recovery
 * - Scene transitions
 * - Responsive media view sizing
 */
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
        /*
         * Sets the callback to be executed when video playback finishes.
         * 
         * Parameters:
         * - callback: Runnable to be executed at video completion
         */
        this.onVideoFinished = callback;
    }

    @FXML
    public void onSkip(ActionEvent event) {
        /*
         * Handles the skip button action by stopping media and transitioning.
         * 
         * Parameters:
         * - event: The triggering action event
         */
        stopMedia();
        handleVideoEnd();
    }

    public void initializeMedia(String mediaPath, double width, double height) {
        /*
         * Initializes and configures media playback for the specified source.
         * 
         * Parameters:
         * - mediaPath: Path to the media file
         * - width: Desired width of the media view
         * - height: Desired height of the media view
         * 
         * Notes:
         * - Prevents multiple simultaneous initialization attempts
         * - Handles media loading errors
         */
        try {
            if (isMediaLoading) {
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
        /*
         * Sets up the media player with the specified parameters.
         * 
         * Parameters:
         * - mediaPath: Source path for the media
         * - width: Target width for media view
         * - height: Target height for media view
         * 
         * Implementation:
         * - Creates media player instance
         * - Configures event handlers
         * - Sets up view properties
         * - Initiates playback
         */
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
        /*
         * Configures media player event handlers and callbacks.
         * 
         * Parameters:
         * - mediaPath: Media source path for potential reinitialization
         * - width: View width for reinit
         * - height: View height for reinit
         * 
         * Handles:
         * - Ready state
         * - Error recovery
         * - End of media actions
         * - Status changes
         */
        mediaPlayer.setOnReady(() -> {
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

        mediaPlayer.statusProperty().addListener((observable, oldStatus, newStatus) -> {});
    }

    private void configureMediaView(double width, double height) {
        /*
         * Configures the MediaView component with responsive layout.
         * 
         * Parameters:
         * - width: Initial width
         * - height: Initial height
         * 
         * Features:
         * - Responsive sizing
         * - Center alignment
         * - Dynamic message label sizing
         */
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
        /*
         * Sets up media player properties and error recovery.
         * 
         * Features:
         * - Volume management
         * - Stall recovery
         * - Autoplay configuration
         */
        mediaPlayer.setAutoPlay(false); // Remove this line
        
        // Add error recovery
        mediaPlayer.setOnStalled(() -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
        });

        mediaPlayer.setVolume(SoundManager.getInstance().getVolume() * SoundManager.getInstance().getMasterVolume() * 2);
    }

    public void playMedia() {
        /*
         * Starts or resumes media playback if a player exists.
         */
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopMedia() {
        /*
         * Stops media playback and cleans up resources.
         */
        cleanup();
    }

    private void cleanup() {
        /*
         * Performs resource cleanup and resets player state.
         * 
         * Actions:
         * - Stops playback
         * - Disposes media player
         * - Clears references
         * - Resets loading state
         */
        if (mediaView != null) {
            mediaView.setMediaPlayer(null);
        }

        if (mediaPlayer != null) {
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
        /*
         * Handles video completion actions.
         * 
         * Actions:
         * - Executes completion callback if set
         * - Returns to previous scene
         */
        if (onVideoFinished != null) {
            onVideoFinished.run();
        }
        returnToPreviousScene();
    }

    private void returnToPreviousScene() {
        /*
         * Transitions back to the previous scene.
         * 
         * Requirements:
         * - Valid primary stage reference
         * - Valid previous scene reference
         */
        if (primaryStage != null && previousScene != null) {
            primaryStage.setScene(previousScene);
            primaryStage.centerOnScreen();
        }
    }

    // Method to set reference to the stage and the previous scene
    public void setPreviousScene(Stage primaryStage, Scene previousScene) {
        /*
         * Stores references for scene management.
         * 
         * Parameters:
         * - primaryStage: Main application stage
         * - previousScene: Scene to return to after playback
         */
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }
}

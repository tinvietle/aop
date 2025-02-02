/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.menu;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/*
 * Manages video playback functionality for the menu background.
 * 
 * Features:
 * - Video loading and playback
 * - Media player configuration
 * - Error handling and recovery
 * - Resource cleanup
 */
public class VideoPlayer {
    private static MediaPlayer mediaPlayer;
    private static Media currentMedia;
    private static boolean isMediaLoading = false;

    public static void playBackgroundVideo(MediaView mediaView) {
        /*
         * Starts playing the background video in the provided MediaView.
         * 
         * Parameters:
         * - mediaView: MediaView component to display the video
         * 
         * Features:
         * - Prevents multiple loading attempts
         * - Handles cleanup of existing media
         * - Initializes new media player
         */
        try {
            if (isMediaLoading) {
                return;
            }

            stopBackgroundVideo(mediaView);
            initializeMediaPlayer(mediaView);

        } catch (Exception e) {
            System.err.println("Error playing video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeMediaPlayer(MediaView mediaView) {
        /*
         * Sets up the media player with the video file.
         * 
         * Parameters:
         * - mediaView: MediaView to attach the media player to
         * 
         * Features:
         * - Loads video file
         * - Creates media player instance
         * - Configures event handlers
         * - Sets up media view properties
         */
        try {
            isMediaLoading = true;
            String videoPath = VideoPlayer.class.getResource("/com/example/assets/intro_pokemon_2.mp4").toExternalForm();
            
            // Create new Media instance
            currentMedia = new Media(videoPath);
            mediaPlayer = new MediaPlayer(currentMedia);
            
            // Set up media player event handlers
            setupMediaPlayerEvents(mediaPlayer, mediaView);
            
            // Apply media view settings
            configureMediaView(mediaView);
            
            // Apply media player settings
            configureMediaPlayer();

        } catch (Exception e) {
            System.err.println("Error initializing media player: " + e.getMessage());
            e.printStackTrace();
            isMediaLoading = false;
        }
    }

    private static void setupMediaPlayerEvents(MediaPlayer player, MediaView mediaView) {
        /*
         * Configures event handlers for the media player.
         * 
         * Parameters:
         * - player: MediaPlayer instance to configure
         * - mediaView: Associated MediaView for error handling
         * 
         * Features:
         * - Sets up ready state handler
         * - Configures error recovery
         * - Handles end of media
         * - Monitors player status
         */
        player.setOnReady(() -> {
            isMediaLoading = false;
        });

        player.setOnError(() -> {
            System.err.println("Media Player Error: " + player.getError().getMessage());
            isMediaLoading = false;
            cleanup(mediaView);
            // Attempt to reinitialize after error
            initializeMediaPlayer(mediaView);
        });

        player.setOnEndOfMedia(() -> {
            player.seek(javafx.util.Duration.ZERO);
        });

        player.statusProperty().addListener((observable, oldStatus, newStatus) -> {});
    }

    private static void configureMediaView(MediaView mediaView) {
        /*
         * Sets up the MediaView display properties.
         * 
         * Parameters:
         * - mediaView: MediaView to configure
         * 
         * Features:
         * - Applies blur effect
         * - Sets media player
         * - Configures aspect ratio
         * - Binds size to scene
         */
        GaussianBlur blur = new GaussianBlur(10);
        mediaView.setEffect(blur);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(false);
        
        // Add size binding when scene is available
        mediaView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                mediaView.fitWidthProperty().bind(newScene.widthProperty());
                mediaView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });
    }

    private static void configureMediaPlayer() {
        /*
         * Configures media player playback settings.
         * 
         * Features:
         * - Sets autoplay
         * - Configures looping
         * - Mutes audio
         * - Sets up stall recovery
         */
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);
        
        // Add error recovery
        mediaPlayer.setOnStalled(() -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
        });
    }

    public static void stopBackgroundVideo(MediaView mediaView) {
        /*
         * Stops video playback and cleans up resources.
         * 
         * Parameters:
         * - mediaView: MediaView to clean up
         */
        cleanup(mediaView);
    }

    private static void cleanup(MediaView mediaView) {
        /*
         * Performs resource cleanup for media components.
         * 
         * Parameters:
         * - mediaView: MediaView to clean up
         * 
         * Actions:
         * - Clears media view
         * - Stops media player
         * - Releases resources
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

        if (currentMedia != null) {
            currentMedia = null;
        }

        isMediaLoading = false;
    }
}

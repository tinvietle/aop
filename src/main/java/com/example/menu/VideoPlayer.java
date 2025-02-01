package com.example.menu;

import java.nio.file.Paths;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoPlayer {
    private static MediaPlayer mediaPlayer;
    private static Media currentMedia;
    private static boolean isMediaLoading = false;

    public static void playBackgroundVideo(MediaView mediaView) {
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
        try {
            isMediaLoading = true;
            String videoPath = Paths.get("src\\main\\resources\\com\\example\\assets\\intro_pokemon_2.mp4").toUri().toString();
            
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
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);
        
        // Add error recovery
        mediaPlayer.setOnStalled(() -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
        });
    }

    public static void stopBackgroundVideo(MediaView mediaView) {
        cleanup(mediaView);
    }

    private static void cleanup(MediaView mediaView) {
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

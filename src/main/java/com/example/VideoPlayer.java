package com.example;

import java.nio.file.Paths;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class VideoPlayer {

    private static MediaPlayer mediaPlayer;

    public static void playBackgroundVideo(MediaView mediaView) {
        try {
            // Check if media player is already initialized
            if (mediaPlayer != null) {
                System.out.println("Stopping and disposing existing media player");
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;

                // Add a spiral transform animation before reinitializing the media player
                applySpiralTransform(mediaView, () -> initializeMediaPlayer(mediaView));
            } else {
                initializeMediaPlayer(mediaView);
            }

        } catch (Exception e) {
            System.err.println("Error playing video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeMediaPlayer(MediaView mediaView) {
        try {
            // Get video resource URL from the classpath
            String videoPath = Paths.get("assets\\intro_pokemon_2.mp4").toUri().toString();
            Media media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);
            
            // Add error handling for media player
            mediaPlayer.setOnError(() -> {
                System.err.println("Media Player Error: " + mediaPlayer.getError().getMessage());
            });

            // Apply effects and settings
            GaussianBlur blur = new GaussianBlur(10);
            mediaView.setEffect(blur);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(false);
            mediaView.setPreserveRatio(false);
            
            // Add size binding when scene is available
            mediaView.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    mediaView.fitWidthProperty().bind(newScene.widthProperty());
                    mediaView.fitHeightProperty().bind(newScene.heightProperty());
                }
            });

            System.out.println("Video started successfully");

        } catch (Exception e) {
            System.err.println("Error initializing media player: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopBackgroundVideo() {
        if (mediaPlayer != null) {
            System.out.println("Stopping and disposing media player");
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private static void applySpiralTransform(MediaView mediaView, Runnable onFinished) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), mediaView);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), mediaView);
        translateTransition.setByX(100);
        translateTransition.setByY(100);
        translateTransition.setCycleCount(1);

        SequentialTransition sequentialTransition = new SequentialTransition(rotateTransition, translateTransition);
        sequentialTransition.setOnFinished(event -> onFinished.run());
        sequentialTransition.play();
    }
}

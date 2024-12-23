package com.example;

import java.nio.file.Paths;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoPlayer {

    public static void playBackgroundVideo(MediaView mediaView) {
        try {

            String videoPath = Paths.get("C:\\Users\\PC\\Desktop\\Tech\\VGU\\3rd_year\\Fra-Uas\\OOP_Java\\aop\\assets\\stocks\\intro_pokemon_2.mp4").toUri().toString(); 
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            GaussianBlur blur = new GaussianBlur(10);
            mediaView.setEffect(blur);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(false); 
            
            // Bind MediaView size to Scene size
            // mediaView.fitWidthProperty().bind(mediaView.getScene().widthProperty());
            // mediaView.fitHeightProperty().bind(mediaView.getScene().heightProperty());
            mediaView.setPreserveRatio(false);

        } catch (Exception e) {
            System.err.println("Error playing video: " + e.getMessage());
        }
    }
}

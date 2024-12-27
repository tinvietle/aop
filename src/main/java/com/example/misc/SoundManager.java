package com.example.misc;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer mediaPlayer;
    private List<String> bgmList;
    private double volume = 0.5;

    private SoundManager() {
        bgmList = new ArrayList<>();
        try {
            for (int i = 1; i <= 10; i++) {
                URL resourceUrl = getClass().getResource("/com/example/assets/bgm/track" + i + ".mp3");
                if (resourceUrl != null) {
                    bgmList.add(resourceUrl.toString());
                    System.out.println("Found BGM track: " + i);
                }
            }
            if (bgmList.isEmpty()) {
                System.err.println("No BGM files found in resources!");
            }
        } catch (Exception e) {
            System.err.println("Error loading BGM files: " + e.getMessage());
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playRandomBGM() {
        try {
            if (bgmList.isEmpty()) {
                System.err.println("No BGM files found!");
                return;
            }

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            
            Random random = new Random();
            String mediaPath = bgmList.get(random.nextInt(bgmList.size()));
            Media media = new Media(mediaPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            
            // Add error handling for media player
            mediaPlayer.setOnError(() -> {
                System.err.println("Media error: " + mediaPlayer.getError().getMessage());
            });
            
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing BGM: " + e.getMessage());
        }
    }

    public void stopBGM() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }
}

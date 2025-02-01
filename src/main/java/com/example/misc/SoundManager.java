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
    private double volume = 0.1;
    private double sfxVolume = 0.5;
    private double masterVolume = 1.0;
    private double voiceVolume = 0.5;
    // New field to hold the currently playing voice player
    private MediaPlayer currentVoicePlayer;

    private SoundManager() {
        bgmList = new ArrayList<>();
        try {
            for (int i = 1; i <= 10; i++) {
                URL resourceUrl = getClass().getResource("/com/example/assets/bgm/track" + i + ".mp3");
                if (resourceUrl != null) {
                    bgmList.add(resourceUrl.toString());
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
            mediaPlayer.setVolume(volume * masterVolume);
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

    public void playBGM(String bgmPath) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            URL resourceUrl = getClass().getResource(bgmPath);
            if (resourceUrl != null) {
                Media media = new Media(resourceUrl.toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(volume * masterVolume);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

                // Add error handling for media player
                mediaPlayer.setOnError(() -> {
                    System.err.println("Media error: " + mediaPlayer.getError().getMessage());
                });

                mediaPlayer.play();
            } else {
                System.err.println("BGM file not found: " + bgmPath);
            }
        } catch (Exception e) {
            System.err.println("Error playing BGM: " + e.getMessage());
        }
    }

    public void playSFX(String sfxPath) {
        try {
            URL resourceUrl = getClass().getResource(sfxPath);
            if (resourceUrl != null) {
                Media sfxMedia = new Media(resourceUrl.toString());
                MediaPlayer sfxPlayer = new MediaPlayer(sfxMedia);
                sfxPlayer.setVolume(sfxVolume * masterVolume);
                sfxPlayer.play();
            } else {
                System.err.println("SFX file not found: " + sfxPath);
            }
        } catch (Exception e) {
            System.err.println("Error playing SFX: " + e.getMessage());
        }
    }

    public void playVoice(String voicePath) {
        try {
            URL resourceUrl = getClass().getResource(voicePath);
            if (resourceUrl != null) {
                Media voiceMedia = new Media(resourceUrl.toString());
                currentVoicePlayer = new MediaPlayer(voiceMedia);
                currentVoicePlayer.setVolume(voiceVolume * masterVolume);
                currentVoicePlayer.setOnError(() -> {
                    System.err.println("Voice Media error: " + currentVoicePlayer.getError().getMessage());
                    currentVoicePlayer = null;
                });
                currentVoicePlayer.setOnEndOfMedia(() -> {
                    currentVoicePlayer.dispose();
                    currentVoicePlayer = null;
                });
                // Start voice playback after media is ready
                currentVoicePlayer.setOnReady(() -> currentVoicePlayer.play());
            } else {
                System.err.println("Voice file not found: " + voicePath);
            }
        } catch (Exception e) {
            System.err.println("Error playing voice: " + e.getMessage());
            currentVoicePlayer = null;
        }
    }

    public void stopBGM() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    // New method to pause BGM instead of stopping it
    public void pauseBGM() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }
    
    // New method to resume BGM if paused, or play a random BGM otherwise
    public void resumeBGM() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
        } else {
            playRandomBGM();
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(masterVolume * this.volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    public void setSFXVolume(double volume) {
        this.sfxVolume = volume;
    }

    public double getSFXVolume() {
        return sfxVolume;
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        this.masterVolume = masterVolume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(masterVolume * volume);
        }
    }

    public double getVoiceVolume() {
        return voiceVolume;
    }

    public void setVoiceVolume(double volume) {
        this.voiceVolume = volume;
    }
}

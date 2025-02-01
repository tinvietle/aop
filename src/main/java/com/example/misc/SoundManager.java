/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
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
        /*
         * Initializes the SoundManager singleton.
         * 
         * Features:
         * - Loads BGM tracks from resources
         * - Creates empty BGM list
         * - Handles missing resources
         */
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
        /*
         * Gets the singleton instance of SoundManager.
         * 
         * Returns:
         * - SoundManager singleton instance
         */
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playRandomBGM() {
        /*
         * Plays a random background music track.
         * 
         * Features:
         * - Stops current BGM if playing
         * - Randomly selects from available tracks
         * - Sets volume based on master and BGM volume
         * - Loops indefinitely
         */
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
        /*
         * Plays a specific background music track.
         * 
         * Parameters:
         * - bgmPath: Resource path to the BGM file
         * 
         * Features:
         * - Stops current BGM if playing
         * - Sets volume and looping
         * - Handles playback errors
         */
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media media = new Media(getClass().getResource(bgmPath).toString());
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

    public void playSFX(String sfxPath) {
        /*
         * Plays a sound effect once.
         * 
         * Parameters:
         * - sfxPath: Resource path to the sound effect file
         * 
         * Features:
         * - Uses SFX volume settings
         * - No looping
         */
        try {
            Media sfxMedia = new Media(getClass().getResource(sfxPath).toString());
            MediaPlayer sfxPlayer = new MediaPlayer(sfxMedia);
            sfxPlayer.setVolume(sfxVolume * masterVolume);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing SFX: " + e.getMessage());
        }
    }

    public void playVoice(String voicePath) {
        /*
         * Plays a voice clip.
         * 
         * Parameters:
         * - voicePath: Resource path to the voice file
         * 
         * Features:
         * - Uses voice volume settings
         * - Auto-disposes after playback
         * - Error handling for media issues
         */
        try {
            // Pause BGM before playing voice (if desired)
    
            Media voiceMedia = new Media(getClass().getResource(voicePath).toString());
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
        /*
         * Sets the background music volume.
         * 
         * Parameters:
         * - volume: Volume level between 0.0 and 1.0
         */
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(masterVolume * this.volume);
        }
    }

    public double getVolume() {
        /*
         * Gets the current background music volume.
         * 
         * Returns:
         * - Current volume level between 0.0 and 1.0
         */
        return volume;
    }

    public void setSFXVolume(double volume) {
        /*
         * Sets the sound effects volume.
         * 
         * Parameters:
         * - volume: Volume level between 0.0 and 1.0
         */
        this.sfxVolume = volume;
    }

    public double getSFXVolume() {
        /*
         * Gets the current sound effects volume.
         * 
         * Returns:
         * - Current volume level between 0.0 and 1.0
         */
        return sfxVolume;
    }

    public double getMasterVolume() {
        /*
         * Gets the current master volume.
         * 
         * Returns:
         * - Current master volume level between 0.0 and 1.0
         */
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        /*
         * Sets the master volume.
         * 
         * Parameters:
         * - masterVolume: Volume level between 0.0 and 1.0
         */
        this.masterVolume = masterVolume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(masterVolume * volume);
        }
    }

    public double getVoiceVolume() {
        /*
         * Gets the current voice playback volume.
         * 
         * Returns:
         * - Current voice volume level between 0.0 and 1.0
         */
        return voiceVolume;
    }

    public void setVoiceVolume(double volume) {
        /*
         * Sets the voice playback volume.
         * 
         * Parameters:
         * - volume: Volume level between 0.0 and 1.0
         */
        this.voiceVolume = volume;
    }
}

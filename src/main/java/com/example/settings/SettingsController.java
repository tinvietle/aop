package com.example.settings;

import java.io.IOException;
import java.net.URL;

import com.example.App;
import com.example.misc.SoundManager;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SettingsController {
    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider sfxVolumeSlider;

    @FXML
    private StackPane settingsRootPane;

    @FXML 
    private ImageView backgroundImage;

    private Stage primaryStage;
    private Scene previousScene;

    @FXML
    public void initialize() {
        try {
            // Bind backgroundIMage size to the root pane size
            backgroundImage.fitWidthProperty().bind(settingsRootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(settingsRootPane.heightProperty());

            // Initialize volume slider
            volumeSlider.setValue(SoundManager.getInstance().getVolume());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
                SoundManager.getInstance().setVolume(newVal.doubleValue())
            );

            // Initialize SFX volume slider
            sfxVolumeSlider.setValue(SoundManager.getInstance().getSFXVolume());
            sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                SoundManager.getInstance().setSFXVolume(newVal.doubleValue())
            );
        } catch (Exception e) {
            System.err.println("Error initializing settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void backToMenu() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        if (primaryStage != null && previousScene != null) {
            primaryStage.setScene(previousScene);
            primaryStage.centerOnScreen();
        }
    }

    public void setPreviousScene(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.previousScene = scene;
    }
}

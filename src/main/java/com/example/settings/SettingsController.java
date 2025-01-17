package com.example.settings;

import com.example.misc.SoundManager;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SettingsController {
    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider sfxVolumeSlider;

    @FXML
    private Slider masterVolumeSlider;

    @FXML
    private Slider voiceVolumeSlider;

    @FXML
    private Label masterVolumeLabel;

    @FXML
    private Label bgmVolumeLabel;

    @FXML
    private Label sfxVolumeLabel;

    @FXML
    private Label voiceVolumeLabel;

    @FXML
    private StackPane settingsRootPane;

    @FXML 
    private ImageView backgroundImage;

    private Stage primaryStage;
    private Scene previousScene;

    @FXML
    public void initialize() {
        try {
            // First check if all required FXML elements are properly injected
            if (backgroundImage != null && settingsRootPane != null) {
                // Bind all FXML components size to root pane size
                backgroundImage.fitWidthProperty().bind(settingsRootPane.widthProperty());
                backgroundImage.fitHeightProperty().bind(settingsRootPane.heightProperty());

                volumeSlider.prefWidthProperty().bind(settingsRootPane.widthProperty().multiply(0.3));
                volumeSlider.prefHeightProperty().bind(settingsRootPane.heightProperty().multiply(0.05));
                sfxVolumeSlider.prefWidthProperty().bind(settingsRootPane.widthProperty().multiply(0.3));
                sfxVolumeSlider.prefHeightProperty().bind(settingsRootPane.heightProperty().multiply(0.05));
                masterVolumeSlider.prefWidthProperty().bind(settingsRootPane.widthProperty().multiply(0.3));
                masterVolumeSlider.prefHeightProperty().bind(settingsRootPane.heightProperty().multiply(0.05));
                voiceVolumeSlider.prefWidthProperty().bind(settingsRootPane.widthProperty().multiply(0.3));
                voiceVolumeSlider.prefHeightProperty().bind(settingsRootPane.heightProperty().multiply(0.05));

            }

            // Initialize master volume controls
            if (masterVolumeSlider != null && masterVolumeLabel != null) {
                masterVolumeSlider.setValue(SoundManager.getInstance().getMasterVolume());
                masterVolumeLabel.textProperty().bind(
                    masterVolumeSlider.valueProperty().multiply(100).asString("%.0f"));
                masterVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    SoundManager.getInstance().setMasterVolume(newVal.doubleValue());
                });
            } else {
                System.err.println("Master volume controls not properly initialized");
            }

            // Initialize BGM volume controls
            if (volumeSlider != null && bgmVolumeLabel != null) {
                volumeSlider.setValue(SoundManager.getInstance().getVolume());
                bgmVolumeLabel.textProperty().bind(
                    volumeSlider.valueProperty().multiply(100).asString("%.0f"));
                volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    SoundManager.getInstance().setVolume(newVal.doubleValue());
                });
            } else {
                System.err.println("BGM volume controls not properly initialized");
            }

            // Initialize SFX volume controls
            if (sfxVolumeSlider != null && sfxVolumeLabel != null) {
                sfxVolumeSlider.setValue(SoundManager.getInstance().getSFXVolume());
                sfxVolumeLabel.textProperty().bind(
                    sfxVolumeSlider.valueProperty().multiply(100).asString("%.0f"));
                sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    SoundManager.getInstance().setSFXVolume(newVal.doubleValue());
                });
            } else {
                System.err.println("SFX volume controls not properly initialized");
            }

            // Initialize voice volume controls
            if (voiceVolumeSlider != null && voiceVolumeLabel != null) {
                voiceVolumeSlider.setValue(SoundManager.getInstance().getVoiceVolume());
                voiceVolumeLabel.textProperty().bind(
                    voiceVolumeSlider.valueProperty().multiply(100).asString("%.0f"));
                voiceVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    SoundManager.getInstance().setVoiceVolume(newVal.doubleValue());
                });
            }
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

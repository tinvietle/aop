package com.example.settings;

import java.io.IOException;
import java.net.URL;

import com.example.App;
import com.example.misc.SoundManager;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SettingsController {
    @FXML
    private Slider volumeSlider;

    @FXML
    private StackPane settingsRootPane;

    @FXML
    public void initialize() {
        try {
            // Set background image with proper scaling
            URL resourceUrl = App.class.getResource("/com/example/assets/settings.jpg");
            if (resourceUrl == null) {
                throw new IOException("Cannot find settings background image");
            }
            Image backgroundImage = new Image(resourceUrl.toString());
            BackgroundImage background = new BackgroundImage(
                backgroundImage, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, 
                new BackgroundSize(
                    100, 
                    100, 
                    true, 
                    true, 
                    true, 
                    true  
                )
            );
            settingsRootPane.setBackground(new Background(background));
            
            // Add size listener to adjust background when window resizes
            settingsRootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    settingsRootPane.prefWidthProperty().bind(newScene.widthProperty());
                    settingsRootPane.prefHeightProperty().bind(newScene.heightProperty());
                }
            });

            // Initialize volume slider
            volumeSlider.setValue(SoundManager.getInstance().getVolume());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
                SoundManager.getInstance().setVolume(newVal.doubleValue())
            );
        } catch (Exception e) {
            System.err.println("Error initializing settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void backToMenu() throws IOException {
        Parent menuRoot = App.loadFXML("menu/menu");
        Scene currentScene = settingsRootPane.getScene();
        double sceneWidth = currentScene.getWidth();
        
        // Create a container for both screens
        StackPane container = new StackPane();
        menuRoot.setTranslateX(-sceneWidth);
        container.getChildren().addAll(settingsRootPane, menuRoot);
        
        // Set the container as the new root
        currentScene.setRoot(container);
        
        // Create transitions for both screens
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), settingsRootPane);
        slideOut.setToX(sceneWidth);
        
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), menuRoot);
        slideIn.setToX(0);
        
        // Play both transitions
        slideOut.play();
        slideIn.play();
        
        // When animation finishes, clean up
        slideIn.setOnFinished(event -> {
            container.getChildren().remove(settingsRootPane);
        });
    }
}

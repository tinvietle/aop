package com.example.menu;


import java.io.IOException;

import com.example.App;
import com.example.capture.OnlyMedia;
import com.example.misc.SoundManager;
import com.example.register.RegisterController;
import com.example.settings.SettingsController;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private MediaView backgroundVideo;

    @FXML
    private Button startButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button creditButton;

    @FXML
    private StackPane rootPane;  // Add this FXML injection for the root pane

    @FXML
    private Label titleText;

    @FXML
    public void initialize() {
        // Load Pocket Monk font
        Font.loadFont(getClass().getResourceAsStream("/com/example/assets/font/PocketMonk.otf"), 14);

        // Stop any currently playing background music
        SoundManager.getInstance().stopBGM();
        
        // Play a random BGM track
        SoundManager.getInstance().playRandomBGM();

        // Initialize the background video player
        VideoPlayer.playBackgroundVideo(backgroundVideo);

        backgroundVideo.setPreserveRatio(false);

        // Bind background video size to the root pane size
        backgroundVideo.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundVideo.fitHeightProperty().bind(rootPane.heightProperty());

        // Bind buttons width to the root pane width
        startButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.6));
        settingsButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.6));
        creditButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.6));

        // Bind buttons height to the root pane height
        startButton.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.1));
        settingsButton.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.1));
        creditButton.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.1));

        // Bind font size to scene width, and add other attributes: bold, black
        titleText.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.1), ";"));

        // Bind button font size to scene width, and add other attributes: bold, black
        startButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.02), ";"));
        settingsButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.02), ";"));
        creditButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rootPane.widthProperty().multiply(0.02), ";"));

        // Set up button event handlers
        startButton.setOnAction(event -> handleStartGame());
        settingsButton.setOnAction(event -> handleSettings());
        creditButton.setOnAction(event -> handlecredit());
    }

    private void handleStartGame() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("register/register.fxml"));
            Parent registerRoot = loader.load();
            RegisterController registerController = loader.getController();
            Scene registerScene = new Scene(registerRoot);
            Stage stage = (Stage) rootPane.getScene().getWindow();

            registerController.setPreviousScene(stage, stage.getScene());
            stage.setScene(registerScene);
            stage.centerOnScreen();

            // Stop the background video
            VideoPlayer.stopBackgroundVideo(backgroundVideo);

        } catch (Exception e) {
            System.err.println("Error loading register scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSettings() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/settings/settings.fxml"));
            Parent settingsRoot = loader.load();
            Scene settingsScene = new Scene(settingsRoot);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            
            // Get the controller and set the previous scene
            SettingsController settingsController = loader.getController();
            settingsController.setPreviousScene(stage, stage.getScene());
            
            // Switch to settings scene
            stage.setScene(settingsScene);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handlecredit() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        // System.out.println("Exiting application");
        // Utils.closeProgram();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/capture/onlymedia.fxml"));
            Scene onlyMediaScene = new Scene(fxmlLoader.load());
            OnlyMedia controller = fxmlLoader.getController();

            String videoPath = App.class.getResource("/com/example/assets/credit.mp4").toExternalForm();
            Stage stage = (Stage) rootPane.getScene().getWindow();

            // Get the size of current stage to pass to the controller
            controller.initializeMedia(videoPath.toString(), stage.getWidth(), stage.getHeight());

            // Pass the stage and the previous scene to the controller
            controller.setPreviousScene(stage, stage.getScene());

            stage.setTitle("JavaFX MediaPlayer!");
            stage.setScene(onlyMediaScene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading media scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
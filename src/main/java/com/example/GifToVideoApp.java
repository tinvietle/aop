package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class GifToVideoApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the GIF ImageView
        Image gifImage = new Image("file:src/main/resources/com/example/3ani__675__xy.gif"); // Replace with the path to your GIF
        ImageView gifView = new ImageView(gifImage);
        gifView.setFitWidth(300);
        gifView.setPreserveRatio(true);

        // Label instruction
        Label label = new Label("Click on the GIF to play the video");

        // Layout for the main window
        StackPane mainLayout = new StackPane(gifView, label);
        StackPane.setAlignment(label, javafx.geometry.Pos.TOP_CENTER);

        Scene mainScene = new Scene(mainLayout, 900, 700);

        primaryStage.setTitle("GIF to Video Example");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        gifView.setOnMouseClicked(event -> playVideo(mainLayout, gifView, label));
    }

    private void playVideo(StackPane mainLayout, ImageView gifView, Label label) {
        // Remove GIF and label
        mainLayout.getChildren().removeAll(gifView, label);

        // Media setup (replace with your video file path)
        String videoPath = "file:src/main/resources/com/example/intro_pokemon_2.mp4"; // Replace with the path to your video
        Media videoMedia = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(videoMedia);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(400);
        mediaView.setPreserveRatio(true);

        // Add MediaView to the layout
        mainLayout.getChildren().add(mediaView);

        // Start the video
        mediaPlayer.play();

        // Event listener for when the video ends
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose(); // Release resources
            mainLayout.getChildren().remove(mediaView); // Remove the video player
            mainLayout.getChildren().addAll(gifView, label); // Restore the GIF and label
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.example.capture;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.HashMap;

public class MediaPlay extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        // Create dictionary to store pokemon and their respective video using hashmap
        HashMap<String, String> pokemonVideo = new HashMap<String, String>();
        pokemonVideo.put("gyarados", "/com/example/assets/stocks/gyarados.mp4");
        pokemonVideo.put("hawlucha", "/com/example/assets/stocks/hawlucha.mp4");
        pokemonVideo.put("jellicent", "/com/example/assets/stocks/jellicent.mp4");
        pokemonVideo.put("klingklang", "/com/example/assets/stocks/klingklang.mp4");
        pokemonVideo.put("machamp", "/com/example/assets/stocks/machamp.mp4");
        pokemonVideo.put("pangoro", "/com/example/assets/stocks/pangoro.mp4");
        pokemonVideo.put("talonflame", "/com/example/assets/stocks/talonflame.mp4");

        // Create the GIF ImageViews
        HashMap<String, ImageView> pokemonGifs = new HashMap<>();
        for (String pokemon : pokemonVideo.keySet()) {
            Image gifImage = new Image("file:src/main/resources/com/example/assets/stocks/" + pokemon + ".gif");
            ImageView gifView = new ImageView(gifImage);
            gifView.setFitWidth(60);
            gifView.setPreserveRatio(true);
            pokemonGifs.put(pokemon, gifView);
        }

        // Get screen dimension
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Scale factor for the GIFs
        double scaleFactor = 0.9;

        double targetWidth = screenWidth * scaleFactor;
        double targetHeight = targetWidth / (16.0 / 9.0);

        // Ensure it fits the screen height
        if (targetHeight > screenHeight) {
            targetHeight = screenHeight * scaleFactor;
            targetWidth = targetHeight * (16.0 / 9.0);
        }

        // Create the layout
        VBox layout = new VBox(5);
        layout.setAlignment(Pos.TOP_CENTER);

        // Label instruction
        Label label = new Label("Click on a GIF to play the video");
        layout.getChildren().add(label);

        // Determine the columns per row
        int columns = 3;
        HBox currentRow = new HBox(5);
        currentRow.setAlignment(Pos.CENTER);

        int count = 0;
        for (ImageView gifView : pokemonGifs.values()) {
            currentRow.getChildren().add(gifView);
            gifView.setOnMouseClicked(event -> {
                try {
                    playVideo(primaryStage, pokemonVideo.get(getPokemonName(gifView)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            count++;

            // If the row is full, add it to the main layout and start a new row
            if (count % columns == 0) {
                layout.getChildren().add(currentRow);
                currentRow = new HBox(10);
                currentRow.setAlignment(Pos.CENTER);
            }
        }

        // Add any remaining GIFs in the last row
        if (!currentRow.getChildren().isEmpty()) {
            layout.getChildren().add(currentRow);
        }

        Scene mainScene = new Scene(layout);

        primaryStage.setTitle("GIF to Video Example");
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private String getPokemonName(ImageView gifView) {
        String url = gifView.getImage().getUrl();
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }

    private void playVideo(Stage stage, String videoPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/capture/onlymedia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        OnlyMedia controller = fxmlLoader.getController();
        String mediaPath = getClass().getResource(videoPath).toString();
        controller.initializeMedia(mediaPath, 1.0);
        controller.setPreviousScene(stage, stage.getScene());

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}
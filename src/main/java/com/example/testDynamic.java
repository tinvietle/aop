package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testDynamic extends Application {

    private static final double ASPECT_RATIO = 16.0 / 10; // Example aspect ratio

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("testDynamic.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Dice Roller");

        // Initial size respecting the aspect ratio
        Scene scene = new Scene(root, 900, 900 / ASPECT_RATIO);
        primaryStage.setScene(scene);

        // Set minimum size for the stage
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(800 / ASPECT_RATIO);

        // Add listeners to maintain aspect ratio during resizing
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue();
            double newHeight = newWidth / ASPECT_RATIO;

            if (newHeight >= primaryStage.getMinHeight()) {
                primaryStage.setHeight(newHeight);
            } else {
                primaryStage.setWidth(primaryStage.getMinHeight());
            }
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue();
            double newWidth = newHeight * ASPECT_RATIO;

            if (newWidth >= primaryStage.getMinWidth()) {
                primaryStage.setWidth(newWidth);
            } else {
                primaryStage.setHeight(primaryStage.getMinWidth());
            }
        });

        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

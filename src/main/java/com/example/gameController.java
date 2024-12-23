package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class gameController {

    @FXML
    private GridPane playerInfo;

    @FXML
    private ImageView talonflame;

    @FXML
    private void newGame() throws IOException {
        System.out.println("New Game");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load()); // Load the onlymedia.fxml file

        Stage stage = (Stage) playerInfo.getScene().getWindow();

        stage.setTitle("Age of Pokemon");
        stage.setScene(menuScene);
        stage.setMaximized(true); // Maximize the window
        stage.setResizable(true); // Enable resizing
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void closeProgram() {
        Utils.closeProgram();
    }

    @FXML
    private void catchPokemon(MouseEvent event) {
        // Get the node that triggered the event
        Node source = (Node) event.getSource();
        String pokemonName = (String) source.getId(); // Retrieve userData

        String path = "assets\\stocks\\%s.mp4";
        String videoPath = Paths.get(String.format(path, pokemonName)).toUri().toString();

        if (confirmCatch()) {
            try {
                playVideo((Stage) talonflame.getScene().getWindow(), videoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private boolean confirmCatch() {
        Alert alert = Utils.confirmBox("Catch a Pokemon", "Are you sure that you want to catch this pokemon?", "Press OK to catch the pokemon.");
        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }

    private void playVideo(Stage stage, String videoPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("onlymedia.fxml"));
        Scene onlyMediaScene = new Scene(fxmlLoader.load()); // Load the onlymedia.fxml file
        OnlyMedia controller = fxmlLoader.getController();

        controller.initializeMedia(videoPath, 1);

        // Pass the stage and the previous scene to the controller
        controller.setPreviousScene(stage, stage.getScene());

        stage.setTitle("JavaFX MediaPlayer!");
        stage.setScene(onlyMediaScene);
        stage.setMaximized(true); // Maximize the window
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public void updatePlayerBoard(String[] names, String[] scores) {
        playerInfo.getChildren().clear();
        Label playerHeader = new Label("Players");
        Label scoreHeader = new Label("Scores");
        playerInfo.add(playerHeader, 0, 0);
        playerInfo.add(scoreHeader, 1, 0);

        for (int i = 0; i < names.length; i++) {
            Label name = new Label(names[i]);
            Label score = new Label(scores[i]);
            playerInfo.add(name, 0, i+1);
            playerInfo.add(score, 1, i+1);
        }
    }

    @FXML
    private void initialize() {
        // Create the Tooltip
        Tooltip tooltip = new Tooltip("This is an image!\nCharizard is a Fire/Flying type Pokemon.");

        // Set the show delay to 0 milliseconds (instant display)
        tooltip.setShowDelay(javafx.util.Duration.ZERO);

        // Set the Tooltip on the ImageView
        Tooltip.install(talonflame, tooltip);
    }
}


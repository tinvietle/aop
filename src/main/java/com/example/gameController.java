package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class gameController {

    @FXML
    private GridPane playerInfo;

    @FXML
    private ImageView charizard;

    @FXML
    private void newGame() throws IOException {
        System.out.println("New Game");
        Parent menuRoot = App.loadFXML("menu");
        Scene scene = playerInfo.getScene(); // Get the current scene
        scene.setRoot(menuRoot); // Set the new root
    }

    @FXML
    private void closeProgram() {
        Utils.closeProgram();
    }

    @FXML
    private void confirmCatch() {
        Alert alert = Utils.confirmBox("Catch a Pokemon", "Are you sure that you want to catch this pokemon?", "Press OK to catch the pokemon.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) System.out.println("Pokemon caught!");
    }

    public void updatePlayerBoard(String[] names) {
        playerInfo.getChildren().clear();
        Label playerHeader = new Label("Players");
        Label scoreHeader = new Label("Scores");
        playerInfo.add(playerHeader, 0, 0);
        playerInfo.add(scoreHeader, 1, 0);

        for (int i = 0; i < names.length; i++) {
            Label name = new Label(names[i]);
            Label score = new Label("0");
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
        Tooltip.install(charizard, tooltip);
    }
}


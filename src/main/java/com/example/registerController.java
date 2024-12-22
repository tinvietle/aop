package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class registerController {

    @FXML
    private Spinner<Integer> playerCountSpinner;

    @FXML
    private VBox playerNamesContainer;

    @FXML
    private Button backButton;

    @FXML
    private Button startGameButton;

    @FXML
    public void initialize() {
        // Add listener to spinner to generate player name fields
        playerCountSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updatePlayerNameFields(newVal));

        // Initialize with default number of players
        updatePlayerNameFields(playerCountSpinner.getValue());

        // Set up button event handlers
        backButton.setOnAction(event -> handleBack());
        startGameButton.setOnAction(event -> handleStartGame());
    }

    private void updatePlayerNameFields(int count) {
        playerNamesContainer.getChildren().clear();
        for (int i = 1; i <= count; i++) {
            TextField nameField = new TextField();
            nameField.setPromptText("Player " + i + " Name");
            playerNamesContainer.getChildren().add(nameField);
        }
    }

    private void handleBack() {
        try {
            Parent menuRoot = App.loadFXML("menu"); // Load the LoadingScreen.fxml file
            Scene scene = startGameButton.getScene(); // Get the current scene
            scene.setRoot(menuRoot); // Set the new root
        } catch (IOException e) {
            System.err.println("Error loading .fxml file: " + e.getMessage());
        }
    }

    private void handleStartGame() {
        // Retrieve player names
        int playerCount = playerCountSpinner.getValue();
        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            TextField nameField = (TextField) playerNamesContainer.getChildren().get(i);
            playerNames[i] = nameField.getText().trim();
            if (playerNames[i].isEmpty()) {
                System.err.println("Player " + (i + 1) + " name is empty.");
                return;
            }
        }

        System.out.println("Starting game with players:");
        for (String name : playerNames) {
            System.out.println(name);
        }

        // Start the game
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("game.fxml"));
            Parent startGameRoot = fxmlLoader.load(); // Load the game.fxml file
            gameController controller = fxmlLoader.getController();

            Scene scene = startGameButton.getScene(); // Get the current scene
            scene.setRoot(startGameRoot); // Set the new root

            controller.updatePlayerBoard(playerNames);
        } catch (IOException e) {
            System.out.println("Error loading .fxml file: " + e.getMessage());
            System.err.println("Error loading StartGame.fxml: " + e.getMessage());
        }
    }
}


package com.example.result;

import java.io.IOException;
import java.util.List;

import com.example.App;
import com.example.misc.Player;
import com.example.misc.SoundManager;
import com.example.misc.Utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultDisplay {
    @FXML
    private ImageView trophyImage;
    @FXML
    private Label winnerName;
    @FXML
    private Label winnerScore;
    @FXML
    private ListView<String> leaderboardList;
    @FXML
    private VBox leaderboardContainer;
    @FXML
    private Button newGameButton;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        try {
            Image trophyImg = new Image(getClass().getResourceAsStream("/com/example/assets/trophy.png"));
            if (trophyImg.isError()) {
                System.err.println("Error loading trophy image: " + trophyImg.getException());
                // Set a default image or handle the error appropriately
            }
            trophyImage.setImage(trophyImg);
        } catch (Exception e) {
            System.err.println("Could not load trophy image: " + e.getMessage());
        }

        // Stop current BGM and play new track
        SoundManager.getInstance().stopBGM();
        SoundManager.getInstance().playBGM("/com/example/assets/bgm/kiseki.mp3");

        // Set button actions
        newGameButton.setOnAction(event -> handleNewGame());
        exitButton.setOnAction(event -> handleExit());
    }

    public void displayResults(List<Player> players) {
        if (players.size() < 2 || players.size() > 6) {
            throw new IllegalArgumentException("Player count must be between 2 and 6");
        }

        // Sort players by score in descending order
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        // Display winner
        Player winner = players.get(0);
        winnerName.setText("Player: " + winner.getName());
        winnerScore.setText("Score: " + winner.getScore());

        // Calculate optimal height per player
        double baseHeight = 100; // base height per player
        double totalHeight = players.size() * baseHeight;
        leaderboardList.setPrefHeight(totalHeight);
        leaderboardList.getStyleClass().add("leaderboard-list");

        // Update leaderboard
        leaderboardList.getItems().clear();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String position;
            switch (i) {
                case 0: position = "🥇"; break;
                case 1: position = "🥈"; break;
                case 2: position = "🥉"; break;
                default: position = (i + 1) + "th";
            }
            leaderboardList.getItems().add(
                String.format("%s %s - %d points", position, player.getName(), player.getScore())
            );
        }
    }

    private void handleNewGame() {
        try {
            System.out.println("New Game");
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/menu/menu.fxml"));
            Scene menuScene = new Scene(fxmlLoader.load()); 

            Stage stage = (Stage) newGameButton.getScene().getWindow();

            stage.setTitle("Age of Pokemon");
            stage.setScene(menuScene);
            stage.setMaximized(true); // Maximize the window
            stage.setResizable(true); // Enable resizing
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading menu scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleExit() {
        Utils.closeProgram();
    }
}

package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
    import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ResultDisplay implements Initializable {

    @FXML
    private ImageView trophyIcon;

    @FXML
    private ImageView winnerAvatar;

    @FXML
    private Label winnerName;

    @FXML
    private Label winnerScore;

    @FXML
    private ListView<String> leaderboardList;

    @FXML
    private Button playAgainButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize method called");
        
        // Verify FXML injection
        System.out.println("winnerName label: " + (winnerName != null));
        System.out.println("leaderboardList: " + (leaderboardList != null));
        System.out.println("playAgainButton: " + (playAgainButton != null));

        // Set custom cell factory for leaderboard using anonymous class
        leaderboardList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setFont(Font.font("System", FontWeight.BOLD, 14));
                    setStyle("-fx-background-color: transparent; -fx-padding: 5 10 5 10;");
                }
            }
        });

        // Example data
        HashMap<String, Integer> leaderboardData = new HashMap<>();
        leaderboardData.put("Alice", 1500);
        leaderboardData.put("Bob", 1200);
        leaderboardData.put("Charlie", 1000);
        leaderboardData.put("Diana", 800);

        try {
            // Sort leaderboard data by score in descending order
            leaderboardData.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(entry -> {
                    leaderboardList.getItems().add(entry.getKey() + ": " + entry.getValue());
                });

            // Set winner details
            Map.Entry<String, Integer> winner = leaderboardData.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Leaderboard is empty"));
            winnerName.setText(winner.getKey());
            winnerScore.setText("Score: " + winner.getValue());

            // Set event handler for Play Again button
            playAgainButton.setOnAction(event -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/example/menu.fxml"));
                    Stage stage = (Stage) playAgainButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
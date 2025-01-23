package com.example.result;

import java.io.IOException;
import java.util.List;

import com.example.App;
import com.example.misc.Player;
import com.example.misc.SoundManager;
import com.example.misc.Utils;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultDisplay {
    @FXML
    private HBox rootBox;
    @FXML
    private ImageView trophyImage;
    @FXML
    private Label winerText;
    @FXML
    private Label winnerName;
    @FXML
    private Label winnerScore;
    @FXML
    private Label leaderboardText;
    @FXML
    private ListView<String> leaderboardList;
    @FXML
    private VBox leaderboardContainer;
    @FXML
    private VBox firstContainer;
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

        // Bind the first container size to the window size
        firstContainer.prefWidthProperty().bind(rootBox.widthProperty().multiply(0.4));
        firstContainer.prefHeightProperty().bind(rootBox.heightProperty().multiply(0.8));

        // Bind the leaderboard container size to the window size
        leaderboardContainer.prefWidthProperty().bind(rootBox.widthProperty().multiply(0.4));
        leaderboardContainer.prefHeightProperty().bind(rootBox.heightProperty().multiply(0.8));

        // Bind the trophy image size to the window size
        trophyImage.fitWidthProperty().bind(firstContainer.widthProperty().multiply(0.4));
        trophyImage.fitHeightProperty().bind(firstContainer.heightProperty().multiply(0.4));

        // Bind the new game button size to the window size
        newGameButton.prefWidthProperty().bind(leaderboardContainer.widthProperty().multiply(0.8));
        newGameButton.prefHeightProperty().bind(leaderboardContainer.heightProperty().multiply(0.05));

        // Bind the exit button size to the window size
        exitButton.prefWidthProperty().bind(leaderboardContainer.widthProperty().multiply(0.8));
        exitButton.prefHeightProperty().bind(leaderboardContainer.heightProperty().multiply(0.05));

        // Bind the text size to the window size
        winerText.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", firstContainer.heightProperty().multiply(0.1), "px;"));
        winnerName.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", firstContainer.heightProperty().multiply(0.03), "px;"));
        winnerScore.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", firstContainer.heightProperty().multiply(0.03), "px;"));

        // Bind the leaderboard text size to the window size
        leaderboardText.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", leaderboardContainer.widthProperty().multiply(0.12), "px;"));
        
        // Bind the leaderboard list text size to the window size
        leaderboardList.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", leaderboardContainer.heightProperty().multiply(0.03), "px;",
            "-fx-padding: ", leaderboardContainer.heightProperty().multiply(0.01), "px;"));

        // Bind the list cell padding to the window size
        // Use a custom cell factory to allow dynamic padding
        leaderboardList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                }
            }
        });

        // Add a listener to dynamically update padding
        // Use a custom cell factory to dynamically adjust padding
        leaderboardList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                    }
                }
            };

            // Add a listener to update padding dynamically
            leaderboardContainer.widthProperty().addListener((observable, oldWidth, newWidth) -> {
                double paddLeftRight = newWidth.doubleValue() * 0.05; // 5% of container width
                cell.setPadding(new Insets(0, paddLeftRight, 0, paddLeftRight));
            });

            leaderboardContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
                double paddTopBottom = newHeight.doubleValue() * 0.012; // 1% of container height
                cell.setPadding(new Insets(paddTopBottom, 0, paddTopBottom, 0));
            });

            return cell;
        });

        // Bind button text size to the window size
        newGameButton.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", leaderboardContainer.heightProperty().multiply(0.03), "px;"));
        exitButton.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", leaderboardContainer.heightProperty().multiply(0.03), "px;"));        
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
        double baseHeight = 58; // base height per player
        double totalHeight = players.size() * baseHeight;
        leaderboardList.prefHeightProperty().bind(leaderboardContainer.heightProperty().multiply(totalHeight / 500));
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

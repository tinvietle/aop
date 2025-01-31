package com.example.result;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultDisplay {
    @FXML
    private HBox rootBox;
    @FXML
    private Pane rootPane;
    @FXML
    private ImageView backgroundImage;
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

        // Bind the root box size to the Pane size
        rootBox.prefWidthProperty().bind(rootPane.widthProperty());
        rootBox.prefHeightProperty().bind(rootPane.heightProperty());

        // Bind the background image size to the window size
        backgroundImage.fitWidthProperty().bind(rootBox.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootBox.heightProperty());

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
        players.sort((p1, p2) -> {
            int scoreComp = Integer.compare(p2.getScore(), p1.getScore());
            if (scoreComp != 0) return scoreComp;
            int capturedComp = Integer.compare(p2.getNumCapturedPokemons(), p1.getNumCapturedPokemons());
            if (capturedComp != 0) return capturedComp;
            return Integer.compare(p2.getNumGroups(), p1.getNumGroups());
        });

        // Identify all top players (same top score, captured pokemons, and groups)
        List<Player> winners = findWinners(players);

        // Show winners
        if (winners.size() > 1) {
            winerText.setText("Tie!");
            StringBuilder tieDisplay = new StringBuilder();
            for (Player w : winners) {
                tieDisplay.append(String.format(
                    "Player: %s - %dPts-%dPkmn - %dGrp\n",
                    w.getName(), w.getScore(), w.getNumCapturedPokemons(), w.getNumGroups()
                ));
            }
            winnerName.setText(tieDisplay.toString().trim());
            winnerScore.setText("");
        } else {
            Player w = winners.get(0);
            winerText.setText("Winner!");
            winnerName.setText(String.format(
                "Player: %s - %dPts - %dPkmn - %dGrp",
                w.getName(), w.getScore(), w.getNumCapturedPokemons(), w.getNumGroups()
            ));
            winnerScore.setText("");
        }

        // Calculate optimal height per player
        double baseHeight = 58; // base height per player
        double totalHeight = players.size() * baseHeight;
        leaderboardList.prefHeightProperty().bind(leaderboardContainer.heightProperty().multiply(totalHeight / 500));
        leaderboardList.getStyleClass().add("leaderboard-list");

        // Build leaderboard with shared ranks if same score/pokemons/groups
        leaderboardList.getItems().clear();
        int rank = 1;
        Player prev = null;
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (i == 0) {
                prev = p;
            } else {
                if (p.getScore() != prev.getScore() ||
                    p.getNumCapturedPokemons() != prev.getNumCapturedPokemons() ||
                    p.getNumGroups() != prev.getNumGroups()) {
                    rank = i + 1;
                }
                prev = p;
            }
            leaderboardList.getItems().add(
                String.format("%d) %s - %d points - %d pokemons - %d groups",
                    rank, p.getName(), p.getScore(), p.getNumCapturedPokemons(), p.getNumGroups()
                )
            );
        }
    }

    private List<Player> findWinners(List<Player> players) {
        List<Player> topPlayers = new ArrayList<>();
        topPlayers.add(players.get(0));
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getScore() == topPlayers.get(0).getScore() &&
                players.get(i).getNumCapturedPokemons() == topPlayers.get(0).getNumCapturedPokemons() &&
                players.get(i).getNumGroups() == topPlayers.get(0).getNumGroups()) {
                topPlayers.add(players.get(i));
            } else {
                break;
            }
        }
        return topPlayers;
    }

    private void handleNewGame() {
        try {
            System.out.println("New Game");
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/menu/menu.fxml"));
            Scene menuScene = new Scene(fxmlLoader.load()); 

            Stage stage = (Stage) newGameButton.getScene().getWindow();

            stage.setTitle("Age of Pokemon");
            stage.setScene(menuScene);
            stage.setMaximized(true); 
            stage.setResizable(true); 
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
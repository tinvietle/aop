package com.example.game;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import com.example.App;
import com.example.capture.OnlyMedia;
import com.example.misc.Player;
import com.example.misc.Pokemon;
import com.example.misc.PokemonReader;
import com.example.misc.Utils;
import com.example.settings.SettingsController;

import java.util.HashMap;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.paint.Color;


public class GameController {

    HashMap<String, Player> players = new HashMap<String, Player>();

    HashMap<String, Pokemon> pokemons = new HashMap<String, Pokemon>();

    HashMap<String, ImageView> pokemonImages = new HashMap<String, ImageView>();

    ArrayList<Pokemon> pokemonLists;
    
    Player curPlayer;

    private ArrayList<String> playerOrder; // Add this field to track player order

    @FXML
    private GridPane playerInfo;

    @FXML
    private ImageView cloyster;
    @FXML
    private ImageView galvantula;
    @FXML
    private ImageView gengar;
    @FXML
    private ImageView gyarados;
    @FXML
    private ImageView hawlucha;
    @FXML
    private ImageView helioptile;
    @FXML
    private ImageView jellicent;
    @FXML
    private ImageView klingklang;
    @FXML
    private ImageView ludicolo;
    @FXML
    private ImageView machamp;
    @FXML
    private ImageView manectric;
    @FXML
    private ImageView pangoro;
    @FXML
    private ImageView talonflame;

    @FXML
    private StackPane mainContainer; // Add this field to your existing FXML fields


    public GameController() {
        PokemonReader reader = new PokemonReader();
        pokemonLists = reader.readPokemons();
        for (Pokemon pokemon : pokemonLists) {
            pokemons.put(pokemon.getName(), pokemon);
        }
    }

    @FXML
    private void newGame() throws IOException {
        System.out.println("New Game");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/menu/menu.fxml"));
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
    private void openSettings() throws IOException {   
        // Load settings scene
        Parent settingsRoot = App.loadFXML("settings/settings");
        Scene currentScene = playerInfo.getScene();
        
        // Direct scene switch without transition
        currentScene.setRoot(settingsRoot);
    }

    @FXML
    private void catchPokemon(MouseEvent event) {
        // Get the node that triggered the event
        Node source = (Node) event.getSource();
        String pokemonName = (String) source.getId(); // Retrieve userData

        String path = "src\\main\\resources\\com\\example\\assets\\stocks\\%s.mp4";
        String videoPath = Paths.get(String.format(path, pokemonName)).toUri().toString();

        if (confirmCatch()) {
            try {
                playVideo((Stage) talonflame.getScene().getWindow(), videoPath);
                curPlayer.addScore(1);
                updatePlayerBoard(players);
                // Remove the showNextPlayerTurn() call from here since it's now handled in playVideo
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/capture/onlymedia.fxml"));
        Scene onlyMediaScene = new Scene(fxmlLoader.load());
        OnlyMedia controller = fxmlLoader.getController();
        
        // Store the game scene
        Scene gameScene = stage.getScene();
        
        // Initialize media player
        controller.initializeMedia(videoPath, 1);
        controller.setPreviousScene(stage, gameScene);
        
        // Switch to video scene
        stage.setScene(onlyMediaScene);
        stage.setMaximized(true);
        stage.setResizable(true);
        
        // Add listener for scene restoration
        gameScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
            if (newWindow != null) {
                javafx.application.Platform.runLater(() -> {
                    // Force layout updates
                    gameScene.getRoot().layout();
                    
                    // Ensure proper window state
                    stage.setMaximized(true);
                    stage.setFullScreen(false);
                    
                    // Refresh UI components
                    updatePlayerBoard(players);
                    
                    // Switch to next player and show turn
                    switchToNextPlayer();
                    showNextPlayerTurn();
                    
                    // Request focus to ensure controls work
                    gameScene.getRoot().requestFocus();
                });
            }
        });
    }

    public void registerPlayer(String[] names) {
        playerOrder = new ArrayList<>();
        for (String name : names) {
            Player player = new Player(name);
            players.put(name, player);
            playerOrder.add(name);
            System.out.println("Player registered: " + player.getName());
        }
        updatePlayerBoard(players);
        
        setCurPlayer(names[0]);
        showNextPlayerTurn();
    }

    private void switchToNextPlayer() {
        int currentIndex = playerOrder.indexOf(curPlayer.getName());
        int nextIndex = (currentIndex + 1) % playerOrder.size();
        setCurPlayer(playerOrder.get(nextIndex));
    }

    public void updatePlayerBoard(HashMap<String, Player> players) {
        playerInfo.getChildren().clear();
        Label playerHeader = new Label("Players");
        Label scoreHeader = new Label("Scores");
        playerInfo.add(playerHeader, 0, 0);
        playerInfo.add(scoreHeader, 1, 0);

        int i = 0;
        for (Player player: players.values()) {
            Label name = new Label(player.getName());
            Label score = new Label(Integer.toString(player.getScore()));
            playerInfo.add(name, 0, i+1);
            playerInfo.add(score, 1, i+1);
            i++;
        }
    }

    @FXML
    private void initialize() {
        pokemonImages.put("cloyster", cloyster);
        pokemonImages.put("galvantula", galvantula);
        pokemonImages.put("gengar", gengar);
        pokemonImages.put("gyarados", gyarados);
        pokemonImages.put("hawlucha", hawlucha);
        pokemonImages.put("helioptile", helioptile);
        pokemonImages.put("jellicent", jellicent);
        pokemonImages.put("klingklang", klingklang);
        pokemonImages.put("ludicolo", ludicolo);
        pokemonImages.put("machamp", machamp);
        pokemonImages.put("manectric", manectric);
        pokemonImages.put("pangoro", pangoro);
        pokemonImages.put("talonflame", talonflame);

        for (String name : pokemonImages.keySet()) {

            ImageView image = pokemonImages.get(name);

            // Create the Tooltip
            Tooltip tooltip = new Tooltip(pokemons.get(name).toString());

            // Set the show delay to 0 milliseconds (instant display)
            tooltip.setShowDelay(javafx.util.Duration.ZERO);

            // Set the Tooltip on the ImageView
            Tooltip.install(image, tooltip);
        }

        // Remove the showNextPlayerTurn() call from here
    }

    private void showNextPlayerTurn() {
        try {
            // Load the turn overlay
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/game/nextTurn.fxml"));
            StackPane turnOverlay = loader.load();
            Text turnText = (Text) turnOverlay.lookup("#turnText");
            turnText.setText(curPlayer.getName() + "'s Turn");
            
            // Add overlay to the main scene
            mainContainer.getChildren().add(turnOverlay);
            
            // Create animations
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), turnOverlay);
            fade.setFromValue(0);
            fade.setToValue(1);
            
            ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), turnText);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.0);
            scale.setToY(1.0);
            
            // Combine animations
            ParallelTransition parallel = new ParallelTransition(fade, scale);
            parallel.setOnFinished(e -> {
                // Remove overlay after delay
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), turnOverlay);
                fadeOut.setDelay(Duration.seconds(1));
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> mainContainer.getChildren().remove(turnOverlay));
                fadeOut.play();
            });
            
            parallel.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurPlayer(String name) {
        curPlayer = players.get(name);
    }
}


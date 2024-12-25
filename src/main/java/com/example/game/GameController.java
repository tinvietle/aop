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


public class GameController {

    HashMap<String, Player> players = new HashMap<String, Player>();

    HashMap<String, Pokemon> pokemons = new HashMap<String, Pokemon>();

    HashMap<String, ImageView> pokemonImages = new HashMap<String, ImageView>();

    ArrayList<Pokemon> pokemonLists;
    
    Player curPlayer;

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

    public void registerPlayer(String[] names) {
        for (String name : names) {
            Player player = new Player(name);
            players.put(name, player);
            System.out.println("Player registered: " + player.getName());
        }

        // String[] scores = new String[players.size()]; // Array of String objects, initially null
        // Arrays.fill(scores, "0"); // Fills the array with 0

        updatePlayerBoard(players);
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
    }

    public void setCurPlayer(String name) {
        curPlayer = players.get(name);
    }
}


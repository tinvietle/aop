package com.example.game;

import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Stack;


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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.util.Duration;
import javafx.stage.Screen;
import javafx.beans.binding.Bindings;


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
    private ImageView pikachu;
    @FXML
    private ImageView talonflame;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane rollIncludedPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ImageView mapView;
    @FXML
    private StackPane stackPane;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu helpMenu;
    @FXML
    private Accordion accordionView;
    

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
                // Get the stage from the current scene and play the video
                Stage stage = (Stage) playerInfo.getScene().getWindow();
                playVideo(stage, videoPath);
                curPlayer.addScore(1);
                Pokemon pokemon = pokemons.get(pokemonName);
                pokemon.updateOwner(curPlayer);
                ImageView image = pokemonImages.get(pokemonName);
                image.setOpacity(0.5);
                updatePlayerBoard(players);
                // Create the Tooltip
                Tooltip tooltip = new Tooltip(pokemon.toString());

                // Set the show delay to 0 milliseconds (instant display)
                tooltip.setShowDelay(javafx.util.Duration.ZERO);

                // Set the Tooltip on the ImageView
                Tooltip.install(image, tooltip);
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

        // Get the size of current stage to pass to the controller
        controller.initializeMedia(videoPath, stage.getWidth(), stage.getHeight());

        // Pass the stage and the previous scene to the controller
        controller.setPreviousScene(stage, stage.getScene());

        // Close the current stage and show the new stage
        stage.close();
        stage.setTitle("JavaFX MediaPlayer!");
        stage.setScene(onlyMediaScene);
        // stage.setMaximized(true); // Maximize the window
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
        pokemonImages.put("pikachu", pikachu);
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

        // Get screen dimensions
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Set the menuBar's dimensions to BorderPane
        menuBar.prefWidthProperty().bind(borderPane.widthProperty());
        menuBar.prefHeightProperty().bind(borderPane.heightProperty().multiply(24.0 / 500.0));

        // Set stackPane size to screen dimensions
        stackPane.setPrefWidth(screenWidth);
        stackPane.setPrefHeight(screenHeight);

        // Bind stackPane's dimensions to the borderPane
        stackPane.prefWidthProperty().bind(borderPane.widthProperty());
        stackPane.prefHeightProperty().bind(borderPane.heightProperty().multiply(400.0 / 500.0));

        // Bind the mapView's dimensions to the stackPane
        mapView.fitWidthProperty().bind(stackPane.widthProperty());
        mapView.fitHeightProperty().bind(stackPane.heightProperty());

        // Bind the rollIncludedPane's dimensions to the BorderPane
        rollIncludedPane.prefWidthProperty().bind(borderPane.widthProperty());
        rollIncludedPane.prefHeightProperty().bind(borderPane.heightProperty().multiply(76.0 / 500.0));

        // Bind the playerInfo's dimensions to the BorderPane
        playerInfo.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.2));
        playerInfo.prefHeightProperty().bind(borderPane.heightProperty().multiply(200 / 500.0));

        // Bind the accordionView's dimensions to the BorderPane
        accordionView.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.2));
        accordionView.prefHeightProperty().bind(borderPane.heightProperty().multiply(200 / 500.0));
        
        // Bind all ImageView sizes to the stackPane size
        bindImageView(talonflame, 645.0, 39.0, 0.09, 0.1);   // Example ratios
        bindImageView(cloyster, 562.0, 323.0, 0.08, 0.07);
        bindImageView(galvantula, 219.0, 158.0, 0.15, 0.09);
        bindImageView(gengar, 105.0, 245.0, 0.07, 0.07);
        bindImageView(gyarados, 654.0, 140.0, 0.08, 0.1);
        bindImageView(hawlucha, 297.0, 25.0, 0.05, 0.05);
        bindImageView(helioptile, 301.0, 245.0, 0.06, 0.05);
        bindImageView(jellicent, 567.0, 205.0, 0.07, 0.08);
        bindImageView(klingklang, 359.0, 64.0, 0.08, 0.05);
        bindImageView(ludicolo, 427.0,313.0, 0.09, 0.1);
        bindImageView(machamp, 458.0, 107.0, 0.08, 0.12);
        bindImageView(manectric, 668.0, 309.0, 0.05, 0.05);
        bindImageView(pangoro, 425.0, 199.0, 0.07, 0.08);

        // Bind text of each Menu to the BorderPane
        fileMenu.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", borderPane.heightProperty().multiply(0.02), ";",
                "-fx-text-fill: black;"));
        editMenu.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", borderPane.heightProperty().multiply(0.02), ";",
                "-fx-text-fill: black;")); 
        helpMenu.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", borderPane.heightProperty().multiply(0.02), ";",
                "-fx-text-fill: black;"));
    }

    private void bindImageView(ImageView imageView, double X, double Y, double width, double height) {
        imageView.layoutXProperty().bind(stackPane.widthProperty().multiply(X / 800.0));
        imageView.layoutYProperty().bind(stackPane.heightProperty().multiply(Y / 400.0));
        imageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(width));
        imageView.fitHeightProperty().bind(stackPane.heightProperty().multiply(height));
    }

    public void setCurPlayer(String name) {
        curPlayer = players.get(name);
    }
}


package com.example.game;

import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.text.Text;

import com.example.App;
import com.example.capture.OnlyMedia;
import com.example.help.HelpController;
import com.example.misc.Player;
import com.example.misc.Pokeball;
import com.example.misc.Pokemon;
import com.example.misc.PokemonReader;
import com.example.misc.Utils;
import com.example.settings.SettingsController;
import com.example.misc.SoundManager;

import java.util.HashMap;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.beans.binding.Bindings;
import javafx.util.Duration;
import javafx.stage.Screen;


public class GameController {

    // HashMap<String, Player> players = new HashMap<String, Player>();

    List<Player> players = new ArrayList<Player>();

    HashMap<String, Pokemon> pokemons = new HashMap<String, Pokemon>();

    HashMap<String, ImageView> pokemonImages = new HashMap<String, ImageView>();

    ArrayList<Pokemon> pokemonLists;
    
    Player curPlayer;
    
    private int currentPlayerIndex = 0;
    private Pokemon chosenPokemon;

    boolean isPokemonHandled = false;

    @FXML
    private GridPane playerInfo;
    
    @FXML
    public DiceController dicePaneController;

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
    private Pane dicePane;
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
    @FXML
    private StackPane turnOverlay;
    @FXML
    private Text turnText;
    @FXML
    private StackPane welcomeOverlay;
    @FXML
    private Text welcomeText;
    

    public GameController() {
        PokemonReader reader = new PokemonReader();
        pokemonLists = reader.readPokemons();
        for (Pokemon pokemon : pokemonLists) {
            pokemons.put(pokemon.getName(), pokemon);
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

        for (Pokemon pokemon : pokemons.values()) {
            ImageView image = pokemonImages.get(pokemon.getName());

            // Create the Tooltip
            Tooltip tooltip = new Tooltip(pokemon.toString());

            // Set the show delay to 0 milliseconds (instant display)
            tooltip.setShowDelay(javafx.util.Duration.ZERO);

            Tooltip.install(image, tooltip);
        }

        disableAllPokemons();

        dicePaneController.setGameController(this);

        dicePaneController.setOnRollComplete(() -> endTurn());

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

        // Bind the dicePane's dimensions to the BorderPane
        dicePane.prefWidthProperty().bind(borderPane.widthProperty());
        dicePane.prefHeightProperty().bind(borderPane.heightProperty().multiply(76.0 / 500.0));

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
        bindImageView(pikachu, 238.0, 350.0, 0.07, 0.08);

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
        
        // Initialize turn overlay as invisible
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/game/nextTurn.fxml"));
            StackPane overlay = loader.load();
            turnOverlay = overlay;
            turnText = (Text) overlay.lookup("#turnText");
            turnOverlay.setOpacity(0);
            stackPane.getChildren().add(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // showDialog(
        //     "Welcome to Age of Pokemon!", 
        //     400,  // fixed width in pixels
        //     100,  // fixed height in pixels
        //     1,    // center horizontally
        //     0,    // center vertically
        //     3.0   // duration in seconds
        // );
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
        try {
            // Load settings scene
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/settings/settings.fxml"));
            Parent settingsRoot = loader.load();
            Scene settingsScene = new Scene(settingsRoot);
            Stage stage = (Stage) borderPane.getScene().getWindow();
            
            // Get the controller and set the previous scene
            SettingsController settingsController = loader.getController();
            settingsController.setPreviousScene(stage, stage.getScene());
            
            // Switch to settings scene
            stage.setScene(settingsScene);
            stage.centerOnScreen();
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openHelpScene() throws IOException {   
        try {
            // Load settings scene
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/help/help.fxml"));
            Parent settingsRoot = loader.load();
            Scene settingsScene = new Scene(settingsRoot);
            Stage stage = (Stage) borderPane.getScene().getWindow();
            
            // Get the controller and set the previous scene
            HelpController settingsController = loader.getController();
            settingsController.setPreviousScene(stage, stage.getScene());
            
            // Switch to settings scene
            stage.setScene(settingsScene);
            stage.centerOnScreen();
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void choosePokemon(MouseEvent event) {
        if (!isPokemonHandled) {
            return;
        }

        Node source = (Node) event.getSource();
        String pokemonName = (String) source.getId();

        if (confirmChoose()) {
            try {
                Pokemon pokemon = pokemons.get(pokemonName);
                chosenPokemon = pokemon;

                System.out.println("Chose " + pokemon.getName());

                disableAllPokemons();

                showDialog("Continue rolling to catch the chosen pokemon.", 400, 100, 1, 0, 3.0);

                // Enable the roll button
                dicePaneController.disableButtons(false, false);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private boolean confirmChoose() {
        Alert alert = Utils.confirmBox("Choose a Pokemon", "Are you sure that you want to choose this pokemon?", "Press OK to choose the pokemon.");
        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }

    @FXML
    public void catchPokemon(Pokeball roll) {
        if (roll.compare(chosenPokemon.getRequirements())) {
            System.out.println("Caught the pokemon");

            if (chosenPokemon.getOwner() != null) {
                chosenPokemon.getOwner().updateScore(-chosenPokemon.getScore());
                chosenPokemon.getOwner().removePokemon(chosenPokemon);
            }
        
            curPlayer.updateScore(chosenPokemon.getScore());
            curPlayer.addPokemon(chosenPokemon);
            chosenPokemon.updateOwner(curPlayer);
            ImageView image = pokemonImages.get(chosenPokemon.getName());
            image.setOpacity(0.5);
            updatePlayerBoard(players);
            // Create the Tooltip
            Tooltip tooltip = new Tooltip(chosenPokemon.toString());

            // Set the show delay to 0 milliseconds (instant display)
            tooltip.setShowDelay(javafx.util.Duration.ZERO);

            Tooltip.install(image, tooltip);

            String path = "src\\main\\resources\\com\\example\\assets\\stocks\\%s.mp4";
            String videoPath = Paths.get(String.format(path, chosenPokemon.getName())).toUri().toString();

            try {
                playVideo((Stage) playerInfo.getScene().getWindow(), videoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Failed to catch the pokemon");
            System.out.println("Requirements: " + chosenPokemon.getRequirements().toString());
            System.out.println("Roll: " + roll.toString());
        }
    }

    private void bindImageView(ImageView imageView, double X, double Y, double width, double height) {
        imageView.layoutXProperty().bind(stackPane.widthProperty().multiply(X / 800.0));
        imageView.layoutYProperty().bind(stackPane.heightProperty().multiply(Y / 400.0));
        imageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(width));
        imageView.fitHeightProperty().bind(stackPane.heightProperty().multiply(height));
    }

    private void playVideo(Stage stage, String videoPath) throws IOException {
        // Stop BGM before playing video
        SoundManager.getInstance().stopBGM();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/capture/onlymedia.fxml"));
        Scene onlyMediaScene = new Scene(fxmlLoader.load());
        OnlyMedia controller = fxmlLoader.getController();

        // Get the size of current stage to pass to the controller
        controller.initializeMedia(videoPath, stage.getWidth(), stage.getHeight());

        // Pass the stage and the previous scene to the controller
        controller.setPreviousScene(stage, stage.getScene());
        
        // Add a callback to handle turn transition after video ends
        controller.setOnVideoFinished(() -> {
            SoundManager.getInstance().playRandomBGM();
        });

        stage.setTitle("JavaFX MediaPlayer!");
        stage.setScene(onlyMediaScene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public void registerPlayer(String[] names) {
        for (String name : names) {
            Player player = new Player(name);
            players.add(player);
            System.out.println("Player registered: " + player.getName());
        }

        updatePlayerBoard(players);
        // Set initial player and show first turn
        setCurPlayer(players.get(0));
        showTurnTransition();
    }

    private void showTurnTransition() {
        // Load the overlay FXML
        try {
            if (turnOverlay == null) {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/game/nextTurn.fxml"));
                StackPane overlay = loader.load();
                turnOverlay = overlay;
                turnText = (Text) overlay.lookup("#turnText");
                stackPane.getChildren().add(overlay);
            }

            // Set the text for current player's turn
            turnText.setText(curPlayer.getName() + "'s Turn");

            // Create fade in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), turnOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Create fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), turnOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setDelay(Duration.seconds(2));

            // Play transitions in sequence
            fadeIn.setOnFinished(e -> fadeOut.play());
            fadeIn.play();

            fadeOut.setOnFinished(e -> {
                showDialog("Roll one time.", 400, 100, 1, 0, 3.0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerBoard(List<Player> players) {
        playerInfo.getChildren().clear();
        Label playerHeader = new Label("Players");
        Label scoreHeader = new Label("Scores");
        playerInfo.add(playerHeader, 0, 0);
        playerInfo.add(scoreHeader, 1, 0);

        int i = 0;
        for (Player player: players) {
            Label name = new Label(player.getName());
            Label score = new Label(Integer.toString(player.getScore()));
            playerInfo.add(name, 0, i+1);
            playerInfo.add(score, 1, i+1);
            i++;
        }
    }

    public void endTurn () {
        Pokeball roll = new Pokeball(dicePaneController.getResult());

        catchPokemon(roll);

        nextTurn();
    }

    public void nextTurn() {

        System.out.println("Next Turn");

        dicePaneController.resetDice();
        
        // Find the current player's index
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        // Update the current player to the next player
        setCurPlayer(players.get(currentPlayerIndex));

        showTurnTransition();
    }

    public void setCurPlayer(Player player) {
        curPlayer = player;
    }

    public void enableAllPokemons() {
        isPokemonHandled = true;

        for (Pokemon pokemon : pokemons.values()) {
            ImageView image = pokemonImages.get(pokemon.getName());

            GameUtils.enablePokemon(image);
        }
    }

    @FXML
    public void disableAllPokemons() {
        isPokemonHandled = false;

        for (Pokemon pokemon : pokemons.values()) {
            ImageView image = pokemonImages.get(pokemon.getName());

            GameUtils.disablePokemon(image);
        }
    }

    public void showDialog(String text, double width, double height, double x, double y, double duration) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/game/Dialog.fxml"));
            StackPane dialog = loader.load();
            DialogController dialogController = loader.getController();

            // Set initial opacity to 0 to prevent flashing
            dialog.setOpacity(0);
            
            // Calculate center position if x and y are 0
            if (x == 0 && y == 0) {
                double screenWidth = Screen.getPrimary().getBounds().getWidth();
                double screenHeight = Screen.getPrimary().getBounds().getHeight();
                x = (screenWidth - width) / 2;
                y = (screenHeight - height) / 2;
            }
            
            dialogController.customizeDialog(text, width, height, x, y);
            stackPane.getChildren().add(dialog);
            
            // Create fade in animation
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), dialog);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            // Automatic fade out after specified duration
            PauseTransition delay = new PauseTransition(Duration.seconds(duration));
            delay.setOnFinished(event -> dialogController.fadeOut());
            delay.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.App;
import com.example.capture.OnlyMedia;
import com.example.help.HelpController;
import com.example.misc.Group;
import com.example.misc.GroupReader;
import com.example.misc.Line;
import com.example.misc.Player;
import com.example.misc.Pokemon;
import com.example.misc.PokemonReader;
import com.example.misc.Requirement;
import com.example.misc.SoundManager;
import com.example.misc.Utils;
import com.example.result.ResultDisplay;
import com.example.settings.SettingsController;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Main controller for the Pokemon catching game.
 * This class manages:
 * - Game state and flow
 * - Player management
 * - Pokemon catching mechanics
 * - UI layout and responsiveness
 * - Scene transitions and animations
 * - Media playback
 */
public class GameController {
    // Constants
    private static final int FADE_DURATION = 1000;

    // Data Structures
    private List<Player> players = new ArrayList<>();
    private HashMap<String, Pokemon> pokemons = new HashMap<>();
    private HashMap<String, ImageView> pokemonImages = new HashMap<>();
    private HashMap<String, Group> groups = new HashMap<>();

    // Game State
    private Player curPlayer;
    private int currentPlayerIndex = 0;
    private Pokemon chosenPokemon;
    private boolean isPokemonActive = false;
    private boolean isInstruction = true;
    private Requirement target;

    private String difficulty;

    // FXML Components
    @FXML private GridPane playerInfo;
    @FXML private DiceController dicePaneController;
    @FXML private BorderPane borderPane;
    @FXML private Pane dicePane;
    @FXML private MenuBar menuBar;
    @FXML private ImageView mapView;
    @FXML private StackPane stackPane;
    @FXML private Menu fileMenu, editMenu, helpMenu;
    @FXML private MenuItem instructionButton;
    @FXML private Accordion accordionView;
    @FXML private StackPane turnOverlay, welcomeOverlay;
    @FXML private Text turnText, welcomeText;
    @FXML private Label turnLabel;

    // Pokemon ImageViews
    @FXML private ImageView cloyster, galvantula, gengar, gyarados, hawlucha, helioptile, 
                         jellicent, klingklang, ludicolo, machamp, manectric, 
                         pangoro, pikachu, talonflame;

    private boolean helpSceneOpened = false;

    // Constructor
    public GameController(String difficulty) {
        /*
         * Initializes the game controller with specified difficulty.
         * 
         * Parameters:
         * - difficulty: Game difficulty level that affects Pokemon requirements
         * 
         * Features:
         * - Loads Pokemon data
         * - Initializes group configurations
         * - Sets up scoring system
         */
        this.difficulty = difficulty;
        if (this.difficulty != null) {
            PokemonReader reader = new PokemonReader(this.difficulty);
            reader.readPokemons().forEach(pokemon -> pokemons.put(pokemon.getName(), pokemon));

            GroupReader groupReader = new GroupReader();
            groupReader.readGroups().forEach(group -> {
                group.mapToPokemons(pokemons);
                groups.put(group.getName(), group);
                for (Pokemon pokemon : group.getPokemons()) {
                    pokemon.setGroupScore(group.getScore());
                }
            });
        }
    }

    @FXML
    private void initialize() throws IOException {
        /*
         * Initializes the game interface and components.
         * 
         * Features:
         * - Sets up Pokemon images
         * - Configures UI bindings
         * - Initializes dice controller
         * - Sets up turn transition overlay
         */
        initializePokemonImages();
        setupUIBindings();
        setupDiceController();
        initializeTurnOverlay();
    }

    private void initializePokemonImages() {
        /*
         * Sets up Pokemon image views and their interactions.
         * 
         * Features:
         * - Maps Pokemon names to image views
         * - Configures CSS styling
         * - Sets up tooltips
         * - Initializes click handlers
         */
        // Map ImageViews to Pokemon names
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

        // Listen for when the BorderPane is added to a Scene
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                String css = this.getClass().getResource("/com/example/game/style.css").toExternalForm();
                newScene.getStylesheets().add(css);
            }
        });

        pokemons.values().forEach(pokemon -> {
            ImageView image = pokemonImages.get(pokemon.getName());
            GameUtils.updateToolTip(pokemon, image, borderPane);
        });

        disableAllPokemons(true);
    }

    private void setupUIBindings() {
        /*
         * Establishes responsive UI layout bindings.
         * 
         * Features:
         * - Configures screen dimensions
         * - Binds component sizes
         * - Sets up menu styling
         * - Positions Pokemon images
         */
        // Get screen dimensions
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Set stackPane size to screen dimensions
        stackPane.setPrefWidth(screenWidth);
        stackPane.setPrefHeight(screenHeight);

        // Set menuBar and UI dimensions based on screen size
        menuBar.prefWidthProperty().bind(borderPane.widthProperty());
        menuBar.prefHeightProperty().bind(borderPane.heightProperty().multiply(24.0 / 500.0));

        stackPane.prefWidthProperty().bind(borderPane.widthProperty());
        stackPane.prefHeightProperty().bind(borderPane.heightProperty().multiply(400.0 / 500.0));

        mapView.fitWidthProperty().bind(stackPane.widthProperty());
        mapView.fitHeightProperty().bind(stackPane.heightProperty());

        dicePane.prefWidthProperty().bind(borderPane.widthProperty());
        dicePane.prefHeightProperty().bind(borderPane.heightProperty().multiply(76.0 / 500.0));

        playerInfo.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.2));
        playerInfo.prefHeightProperty().bind(borderPane.heightProperty().multiply(200 / 500.0));

        accordionView.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.2));
        accordionView.prefHeightProperty().bind(borderPane.heightProperty().multiply(200 / 500.0));

        // Bind turn label
        turnLabel.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", borderPane.heightProperty().multiply(0.05), ";"));

        // Bind all ImageView sizes to the stackPane size
        bindImageView(talonflame, 490.0, 165.0, 0.2, 0.2);   // Example ratios
        bindImageView(cloyster, 249.0, 310.0, 0.09, 0.1);
        bindImageView(galvantula, 300.0, 253.0, 0.09, 0.1);
        bindImageView(gengar, 220.0, 33.0, 0.09, 0.1);
        bindImageView(gyarados, 500, 108.0, 0.15, 0.15);
        bindImageView(hawlucha, 480.0, 300.0, 0.09, 0.1);
        bindImageView(helioptile, 418.0, 253.0, 0.07, 0.07);
        bindImageView(jellicent, 500.0, 29.0, 0.09, 0.09);
        bindImageView(klingklang, 225, 200, 0.09, 0.1);
        bindImageView(ludicolo, 210.0,100.0, 0.09, 0.1);
        bindImageView(machamp, 350.0, 33.0, 0.15, 0.15);
        bindImageView(manectric, 420.0, 335.0, 0.09, 0.1);
        bindImageView(pangoro, 380.0, 130.0, 0.13, 0.13);
        bindImageView(pikachu, 145.0, 253.0, 0.07, 0.07);

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

    private void setupDiceController() {
        /*
         * Configures the dice controller for the game.
         * 
         * Features:
         * - Sets game controller reference
         * - Defines roll completion behavior
         */
        dicePaneController.setGameController(this);
        dicePaneController.setOnRollComplete(this::endTurn);
    }

    private void initializeTurnOverlay() {
        /*
         * Initializes the turn transition overlay.
         * 
         * Features:
         * - Loads FXML for turn overlay
         * - Sets up text and opacity
         * - Adds overlay to stack pane
         */
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
    }

    @FXML
    private void newGame() throws IOException {
        /*
         * Starts a new game by loading the main menu scene.
         * 
         * Features:
         * - Plays button sound effect
         * - Loads main menu FXML
         * - Sets up stage and scene
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        Stage stage = (Stage) borderPane.getScene().getWindow();
        GameUtils.loadScene("/com/example/menu/menu.fxml", "Age of Pokemon", stage, null);
    }

    @FXML
    private void closeProgram() {
        /*
         * Closes the application.
         * 
         * Features:
         * - Plays button sound effect
         * - Calls utility method to close program
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        Utils.closeProgram();
    }

    @FXML
    private void openSettings() throws IOException {   
        /*
         * Opens the settings scene.
         * 
         * Features:
         * - Plays button sound effect
         * - Loads settings FXML
         * - Sets up stage and scene
         * - Handles exceptions
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            GameUtils.loadScene("/com/example/settings/settings.fxml", "Settings", (Stage) borderPane.getScene().getWindow(), loader -> {
                SettingsController settingsController = loader.getController();
                settingsController.setPreviousScene((Stage) borderPane.getScene().getWindow(), borderPane.getScene());
            });
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openHelpScene() throws IOException {   
        /*
         * Opens the help scene with a popup overlay.
         * 
         * Features:
         * - Plays button sound effect
         * - Loads help FXML
         * - Applies Gaussian blur to background
         * - Animates popup appearance
         * - Handles closing of popup
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/help/help.fxml"));
            Parent helpRoot = loader.load();

            HelpController helpController = loader.getController();

            // Get the root StackPane of the current scene
            StackPane rootPane = (StackPane) borderPane.getScene().getRoot();

            // Apply Gaussian blur only to the rootPane (background content)
            StackPane backgrounPane = new StackPane();
            backgrounPane.getChildren().addAll(rootPane.getChildren());
            rootPane.getChildren().clear();

            // Apply the blur effect to the background
            GaussianBlur blur = new GaussianBlur(10);
            backgrounPane.setEffect(blur);

            // Add the background to the root pane
            rootPane.getChildren().add(backgrounPane);

            double prefHeight = borderPane.getHeight() / 2 * 1.3;
            double prefWidth = prefHeight * 1.6;          

            // Make sure helpRoot is scaleable
            if (helpRoot instanceof Region) {
                Region region = (Region) helpRoot;
                region.setMaxWidth(prefWidth);
                region.setMaxHeight(prefHeight);
            }

            // Create an overlay to prevent clicking on the background
            Pane overlay = new Pane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
            overlay.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
            rootPane.getChildren().add(overlay);

            // Add the popup to the root pane and center it
            rootPane.getChildren().add(helpRoot);
            StackPane.setAlignment(helpRoot, Pos.BOTTOM_CENTER); // Center the popup

            // Calculate the starting Y position (below the screen)
            helpRoot.setTranslateY(rootPane.getHeight()); // Start at the bottom of the scene

            // Animate the popup (wipe in from bottom)
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), helpRoot);
            transition.setFromY(rootPane.getHeight()); // Start position at the bottom
            transition.setToY(-rootPane.getHeight()*0.5 + prefHeight/2.2); // Final position centered (300px for the height)
            transition.setInterpolator(Interpolator.EASE_OUT);
            transition.play();

            // Add listener to scale the popup when the scene is resized
            rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
                double newHeight = newVal.doubleValue() / 2 * 1.3;
                double newWidth = newHeight * 1.6;
                if (helpRoot instanceof Region) {
                    Region region = (Region) helpRoot;
                    region.setMaxWidth(newWidth);
                    region.setMaxHeight(newHeight);
                    
                }
                helpRoot.setTranslateY(-rootPane.getHeight() * 0.5 + newHeight / 2.2); // Update position
            });

            // Handle closing the popup (remove blur and popup immediately)
            helpController.setCloseAction((Void v) -> {
                rootPane.getChildren().remove(helpRoot); // Remove the popup immediately
                rootPane.getChildren().remove(overlay); // Remove the overlay
                backgrounPane.setEffect(null); // Remove the blur effect from the background
                if (!helpSceneOpened) {
                    showTurnTransition();
                    helpSceneOpened = true;
                }
                
            });

        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void userInstruction() {
        /*
         * Toggles the display of user instructions.
         * 
         * Features:
         * - Plays button sound effect
         * - Updates instruction visibility state
         * - Changes button text accordingly
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        isInstruction = !isInstruction;
        instructionButton.setText(isInstruction ? "Hide Instruction" : "Show Instruction");
    }

    @FXML
    private void choosePokemon(MouseEvent event) {
        /*
         * Handles Pokemon selection by the player.
         * 
         * Parameters:
         * - event: Mouse event containing source Pokemon
         * 
         * Features:
         * - Validates selection
         * - Plays Pokemon sound
         * - Checks ownership status
         * - Updates game state
         */
        if (!isPokemonActive) return;

        // Get the name of the Pokemon clicked
        String pokemonName = ((Node) event.getSource()).getId();
        Pokemon attemptedPokemon = pokemons.get(pokemonName);
        // Confirm selection
        if (confirmChoose()) {
            SoundManager.getInstance().playVoice("/com/example/assets/voice/" + pokemonName + ".wav");
            // Check if the Pokemon is already owned by the player
            if (attemptedPokemon.getGroupOwner() != null) {
                GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "This Pokemon is part of a group already owned by Player " + attemptedPokemon.getGroupOwner().getName());
                return;
            }
            // Check if the Pokemon is already owned by owner
            if (attemptedPokemon.getOwner() == curPlayer) {
                GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "You already own this Pokemon.");
                return;
            }
            // Update game state
            chosenPokemon = pokemons.get(pokemonName);
            target = chosenPokemon.getRequirementLines();
            disableAllPokemons(true);
            showInstruction("Continue rolling to catch the chosen Pokemon.", 400, 100, 1, 0, 3000);
        }
    }

    private boolean confirmChoose() {
        /*
         * Displays a confirmation dialog for Pokemon selection.
         * 
         * Returns:
         * - true if player confirms selection
         * - false otherwise
         */
        // Show confirmation dialog
        Alert alert = Utils.confirmBox("Choose a Pokemon", "Are you sure?", "Press OK to choose.");
        Optional<ButtonType> result = alert.showAndWait();
        // Return true if OK is pressed
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void catchPokemon() {
        /*
         * Processes Pokemon catching attempt.
         * 
         * Features:
         * - Validates catch conditions
         * - Updates scores
         * - Manages ownership changes
         * - Triggers animations
         * - Handles group completion
         */
        // Check if a Pokemon is chosen
        if (chosenPokemon == null) {
            GameUtils.showAlert(Alert.AlertType.ERROR, "Failed to Catch", "You have not chosen a pokemon. Better luck next time!");
            nextTurn();
            return;
        }
        // Check if the target is met
        if (target != null && target.length() == 0) {

            if (chosenPokemon.getOwner() != null) {
                chosenPokemon.getOwner().updateScore(-chosenPokemon.getScore());
                chosenPokemon.getOwner().removePokemon(chosenPokemon);
            }
        
            curPlayer.updateScore(chosenPokemon.getScore());
            chosenPokemon.setOwner(curPlayer);
            Group group = groups.get(chosenPokemon.getGroup());
            // Check if the group is completed
            if (group != null && group.checkOwned(curPlayer)) {
                curPlayer.updateScore(group.getScore());
                curPlayer.updateNumGroup();
                for (Pokemon pokemon : group.getPokemons()) {
                    pokemon.setGroupOwner(curPlayer);
                    ImageView image = pokemonImages.get(pokemon.getName());
                    image.setOpacity(0.5);
                    GameUtils.updateToolTip(pokemon, image, borderPane);

                    curPlayer.updateScore(-pokemon.getScore());
                }
            } else {
                // Update the Pokemon image
                ImageView image = pokemonImages.get(chosenPokemon.getName());
                GameUtils.updateToolTip(chosenPokemon, image, borderPane);
            }
            // Update player board
            updatePlayerBoard(players);

            String path = "/com/example/assets/stocks/%s.mp4";
            String videoPath = getClass().getResource(String.format(path, chosenPokemon.getName())).toExternalForm();

            try {
                playVideo((Stage) playerInfo.getScene().getWindow(), videoPath);
            } catch (IOException e) {
                e.printStackTrace();
                nextTurn(); // Ensure turn proceeds even if video fails
            }
        } else {
            GameUtils.showAlert(Alert.AlertType.ERROR, "Failed to Catch", "You failed to catch the Pokemon. Better luck next time!");
            nextTurn();
        }
    }

    public Requirement getTarget() {
        /*
         * Retrieves the current target requirement for catching a Pokemon.
         * 
         * Returns:
         * - Requirement object representing the target
         */
        return target;
    }
    
    public void reduceTarget(Line line) {
        /*
         * Reduces the target requirement by removing a line.
         * 
         * Parameters:
         * - line: Line object to be removed from the target requirement
         */
        this.target.removeLine(line);
    }

    public void disableAllPokemons(boolean disable) {
        /*
         * Enables or disables all Pokemon image views.
         * 
         * Parameters:
         * - disable: true to disable all Pokemon, false to enable
         */
        isPokemonActive = !disable;
        if (disable) {
            pokemons.values().forEach(pokemon -> GameUtils.disablePokemon(pokemonImages.get(pokemon.getName())));
        } else {
            for (Pokemon pokemon : pokemons.values()) {
                ImageView image = pokemonImages.get(pokemon.getName());
                GameUtils.enablePokemon(image);
            }
        }
    }

    private void bindImageView(ImageView imageView, double X, double Y, double width, double height) {
        /*
         * Binds the layout and size properties of an ImageView to the stack pane.
         * 
         * Parameters:
         * - imageView: ImageView to be bound
         * - X: X-coordinate ratio for layout binding
         * - Y: Y-coordinate ratio for layout binding
         * - width: Width ratio for size binding
         * - height: Height ratio for size binding
         */
        imageView.layoutXProperty().bind(stackPane.widthProperty().multiply(X / 800.0));
        imageView.layoutYProperty().bind(stackPane.heightProperty().multiply(Y / 400.0));
        imageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(width));
        imageView.fitHeightProperty().bind(stackPane.heightProperty().multiply(height));
    }

    private void playVideo(Stage stage, String videoPath) throws IOException {
        /*
         * Plays a video in a new scene.
         * 
         * Parameters:
         * - stage: Stage to display the video
         * - videoPath: Path to the video file
         * 
         * Features:
         * - Pauses background music
         * - Loads media player FXML
         * - Sets up media playback
         * - Resumes background music after video
         * - Handles video completion
         */
        // Pause BGM before playing video instead of stopping it
        SoundManager.getInstance().pauseBGM();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/capture/onlymedia.fxml"));
        Scene onlyMediaScene = new Scene(fxmlLoader.load());
        OnlyMedia controller = fxmlLoader.getController();

        controller.initializeMedia(videoPath, stage.getWidth(), stage.getHeight());
        controller.setPreviousScene(stage, stage.getScene());
        
        // When video finishes, resume BGM using the new resumeBGM method
        controller.setOnVideoFinished(() -> {
            javafx.application.Platform.runLater(() -> {
                SoundManager.getInstance().resumeBGM();
                if (checkEndGame()) {
                    try {
                        endGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    nextTurn();
                }
            });
        });

        stage.setTitle("JavaFX MediaPlayer!");
        stage.setScene(onlyMediaScene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public void registerPlayer(String[] names) {
        /*
         * Registers players for the game.
         * 
         * Parameters:
         * - names: Array of player names
         * 
         * Features:
         * - Creates Player objects
         * - Updates player board
         * - Sets initial player
         * - Shows first turn transition
         */
        for (String name : names) {
            Player player = new Player(name);
            players.add(player);
        }

        updatePlayerBoard(players);
        // Set initial player and show first turn
        setCurPlayer(players.get(0));
        if (!helpSceneOpened){
            try {
                openHelpScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            showTurnTransition();
        }
    }

    private void showTurnTransition() {
        /*
         * Displays the turn transition overlay.
         * 
         * Features:
         * - Disables dice buttons
         * - Loads turn overlay if not already loaded
         * - Sets turn text
         * - Fades in and out the overlay
         * - Shows instruction for rolling dice
         */
        dicePaneController.disableButtons(true, true);

        if (turnOverlay == null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/game/nextTurn.fxml"));
                StackPane overlay = loader.load();
                turnOverlay = overlay;
                turnText = (Text) overlay.lookup("#turnText");
                stackPane.getChildren().add(overlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        turnText.setText(curPlayer.getName() + "'s Turn");
        GameUtils.fadeTransition(turnOverlay, FADE_DURATION, 2000, () -> {
            showInstruction("Click on the Roll button at the bottom left corner to roll one time.", 400, 100, 1, 0, 3000);
            if (isInstruction) {
                GameUtils.delay(3000, () -> dicePaneController.disableButtons(false, false));
            } else {
                dicePaneController.disableButtons(false, false);
            }
        });
    }

    public void updatePlayerBoard(List<Player> players) {
        /*
         * Updates the player board with current scores.
         * 
         * Parameters:
         * - players: List of Player objects
         * 
         * Features:
         * - Clears existing player info
         * - Adds player names and scores to the grid
         */
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

    public void endTurn() {
        /*
         * Ends the current player's turn.
         * 
         * Features:
         * - Attempts to catch Pokemon
         * - Advances to the next turn if necessary
         */
        catchPokemon();
    }

    public void nextTurn() {
        /*
         * Advances to the next player's turn.
         * 
         * Actions:
         * - Resets game state
         * - Updates current player
         * - Shows turn transition
         * - Prepares UI for next player
         */
        chosenPokemon = null;
        dicePaneController.resetDice();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        target = null;
        setCurPlayer(players.get(currentPlayerIndex));
        showTurnTransition();
    }

    public void setCurPlayer(Player player) {
        /*
         * Sets the current player for the game.
         * 
         * Parameters:
         * - player: Player object representing the current player
         */
        this.curPlayer = player;
        turnLabel.setText("Turn: " + player.getName());
    }

    public boolean showInstruction(String text, double width, double height, double x, double y, long outDelay) {
        /*
         * Displays an instruction dialog on the screen.
         * 
         * Parameters:
         * - text: Instruction text to display
         * - width: Width of the dialog
         * - height: Height of the dialog
         * - x: X-coordinate for dialog position
         * - y: Y-coordinate for dialog position
         * - outDelay: Delay before fading out the dialog
         * 
         * Returns:
         * - true if instruction is shown, false otherwise
         */
        try {
            if (!isInstruction) return false;

            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/game/dialog.fxml"));
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
            
            GameUtils.fadeTransition((Node) dialog, FADE_DURATION, outDelay, () -> {
                stackPane.getChildren().remove(dialog);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkEndGame() {
        /*
         * Checks if game ending conditions are met.
         * 
         * Returns:
         * - true if all Pokemon are caught
         * - false if uncaught Pokemon remain
         */
        for (Pokemon pokemon : pokemons.values()) {
            if (pokemon.getOwner() == null) {
                return false;
            }
        }
        return true;
    }

    private void endGame() throws IOException {
        /*
         * Handles game completion and results display.
         * 
         * Features:
         * - Loads result scene
         * - Sets up background
         * - Displays final scores
         * - Shows player rankings
         */
        Stage stage = (Stage) borderPane.getScene().getWindow(); 
        GameUtils.loadScene("/com/example/result/result.fxml", "Results", stage, loader -> {
            Parent root = loader.getRoot(); // Reuse the already loaded root
            // Set background image programmatically
            String imagePath = getClass().getResource("/com/example/assets/result.jpg").toExternalForm();
            Image backgroundImage = new Image(imagePath);
            BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            if (root instanceof HBox) {
                ((HBox) root).setBackground(new Background(background));
            }
            
            // For testing purposes
            ResultDisplay controller = loader.getController();
            controller.displayResults(players);
            });
    }

}
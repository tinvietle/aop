package com.example.game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.App;
import com.example.capture.OnlyMedia;
import com.example.help.HelpController;
import com.example.misc.Group;
import com.example.misc.GroupReader;
import com.example.misc.Player;
import com.example.misc.Pokeball;
import com.example.misc.Pokemon;
import com.example.misc.PokemonReader;
import com.example.misc.SoundManager;
import com.example.misc.Utils;
import com.example.result.ResultDisplay;
import com.example.settings.SettingsController;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;


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

    // Pokemon ImageViews
    @FXML private ImageView cloyster, galvantula, gengar, gyarados, hawlucha, helioptile, 
                         jellicent, klingklang, ludicolo, machamp, manectric, 
                         pangoro, pikachu, talonflame;

    // Constructor
    public GameController() {
        PokemonReader reader = new PokemonReader();
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

    @FXML
    private void initialize() {
        initializePokemonImages();
        setupUIBindings();
        setupDiceController();
        initializeTurnOverlay();
    }

    private void initializePokemonImages() {
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
        dicePaneController.setGameController(this);
        dicePaneController.setOnRollComplete(this::endTurn);
    }

    private void initializeTurnOverlay() {
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
        Stage stage = (Stage) borderPane.getScene().getWindow();
        GameUtils.loadScene("/com/example/menu/menu.fxml", "Age of Pokemon", stage, null);
    }

    @FXML
    private void closeProgram() {
        Utils.closeProgram();
    }

    @FXML
    private void openSettings() throws IOException {   
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
        try {
            GameUtils.loadScene("/com/example/help/help.fxml", "Help", (Stage) borderPane.getScene().getWindow(), loader -> {
                HelpController helpController = loader.getController();
                helpController.setPreviousScene((Stage) borderPane.getScene().getWindow(), borderPane.getScene());
            });
        } catch (Exception e) {
            System.err.println("Error opening settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void userInstruction() {
        isInstruction = !isInstruction;
        instructionButton.setText(isInstruction ? "Hide Instruction" : "Show Instruction");
    }

    @FXML
    private void choosePokemon(MouseEvent event) {
        if (!isPokemonActive) return;

        String pokemonName = ((Node) event.getSource()).getId();
        Pokemon attemptedPokemon = pokemons.get(pokemonName);
        if (confirmChoose()) {
            SoundManager.getInstance().playVoice("/com/example/assets/voice/" + pokemonName + ".wav");
            if (attemptedPokemon.getGroupOwner() != null) {
                GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "This Pokemon is part of a group already owned by Player " + attemptedPokemon.getGroupOwner().getName());
                return;
            }
            if (attemptedPokemon.getOwner() == curPlayer) {
                GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "You already own this Pokemon.");
                return;
            }
            chosenPokemon = pokemons.get(pokemonName);
            disableAllPokemons(true);
            showInstruction("Continue rolling to catch the chosen Pokemon.", 400, 100, 1, 0, 3000);
            dicePaneController.disableButtons(false, false);
        }
    }

    private boolean confirmChoose() {
        Alert alert = Utils.confirmBox("Choose a Pokemon", "Are you sure?", "Press OK to choose.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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
            chosenPokemon.setOwner(curPlayer);
            Group group = groups.get(chosenPokemon.getGroup());
            if (group != null && group.checkOwned(curPlayer)) {
                curPlayer.updateScore(group.getScore());
                for (Pokemon pokemon : group.getPokemons()) {
                    System.out.println(curPlayer.getName() + " owns " + pokemon.getName());
                    pokemon.setGroupOwner(curPlayer);
                    ImageView image = pokemonImages.get(pokemon.getName());
                    image.setOpacity(0.5);
                    GameUtils.updateToolTip(pokemon, image, borderPane);
                }
            } else {
                ImageView image = pokemonImages.get(chosenPokemon.getName());
                GameUtils.updateToolTip(chosenPokemon, image, borderPane);
            }
            updatePlayerBoard(players);

            String path = "src\\main\\resources\\com\\example\\assets\\stocks\\%s.mp4";
            String videoPath = Paths.get(String.format(path, chosenPokemon.getName())).toUri().toString();

            try {
                playVideo((Stage) playerInfo.getScene().getWindow(), videoPath);
            } catch (IOException e) {
                e.printStackTrace();
                nextTurn(); // Ensure turn proceeds even if video fails
            }
        } else {
            GameUtils.showAlert(Alert.AlertType.ERROR, "Failed to Catch", "You failed to catch the Pokemon. Better luck next time!");
            System.out.println("Failed to catch the pokemon");
            System.out.println("Requirements: " + chosenPokemon.getRequirements().toString());
            System.out.println("Roll: " + roll.toString());
            nextTurn();
        }
    }

    public void disableAllPokemons(boolean disable) {
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
            javafx.application.Platform.runLater(() -> {
                SoundManager.getInstance().playRandomBGM();
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
        Pokeball roll = new Pokeball(dicePaneController.getResult());
        catchPokemon(roll);
    }

    public void nextTurn() {
        dicePaneController.resetDice();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        setCurPlayer(players.get(currentPlayerIndex));
        showTurnTransition();
    }

    public void setCurPlayer(Player player) {
        curPlayer = player;
    }

    public boolean showInstruction(String text, double width, double height, double x, double y, long outDelay) {
        try {
            if (!isInstruction) return false;

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
            
            GameUtils.fadeTransition((Node) dialog, FADE_DURATION, outDelay, () -> {
                stackPane.getChildren().remove(dialog);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkEndGame() {
        for (Pokemon pokemon : pokemons.values()) {
            if (pokemon.getOwner() == null) {
                System.out.println("Game not ended yet");
                return false;
            }
        }
        System.out.println("Game ended");
        return true;
    }

    private void endGame() throws IOException {
        Stage stage = (Stage) borderPane.getScene().getWindow(); 
        GameUtils.loadScene("/com/example/result/result.fxml", "Results", stage, loader -> {
            Parent root = loader.getRoot(); // Reuse the already loaded root
            // Set background image programmatically
            String imagePath = Paths.get("src\\main\\resources\\com\\example\\assets\\result.jpg").toUri().toString();
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
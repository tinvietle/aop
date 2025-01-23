package com.example.register;

import java.io.IOException;

import com.example.App;
import com.example.game.GameController;
import com.example.misc.SoundManager;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class RegisterController {

    @FXML
    private Spinner<Integer> playerCountSpinner;

    @FXML
    private VBox playerNamesContainer;

    @FXML
    private Button backButton;

    @FXML
    private Button startGameButton;

    @FXML
    private StackPane registerRootPane;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label titleLabel;  

    @FXML
    public void initialize() {
        // Bind background image size to root pane size
        backgroundImage.fitWidthProperty().bind(registerRootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(registerRootPane.heightProperty());

        // Bind container sizes
        playerNamesContainer.maxHeightProperty().bind(registerRootPane.heightProperty().multiply(0.6));
        playerNamesContainer.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.5));

        // Bind spinner size
        playerCountSpinner.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.1));
        playerCountSpinner.prefHeightProperty().bind(registerRootPane.heightProperty().multiply(0.03));

        // Bind button sizes
        startGameButton.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.15));
        startGameButton.prefHeightProperty().bind(registerRootPane.heightProperty().multiply(0.05));
        backButton.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.15));
        backButton.prefHeightProperty().bind(registerRootPane.heightProperty().multiply(0.05));

        // Bind text sizes
        errorLabel.styleProperty().bind(
            Bindings.concat("-fx-font-size: ", registerRootPane.heightProperty().multiply(0.015), "px;")
        );

        // Update title label binding to match menu style
        titleLabel.styleProperty().bind(
            Bindings.concat(
                "-fx-font-size: ", registerRootPane.widthProperty().multiply(0.1),
                ";"
            )
        );

        titleLabel.paddingProperty().bind(
            Bindings.createObjectBinding(() -> {
                double top = registerRootPane.getHeight() * 0.15;
                return new Insets(top, 0, 0, 0);
            }, registerRootPane.heightProperty())
        );

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2);
        playerCountSpinner.setValueFactory(valueFactory);

        // Add listener to spinner to generate player name fields
        playerCountSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePlayerNameFields(newVal);
            }
        });

        // Initialize with default number of players (2)
        updatePlayerNameFields(2);

        // Set up button event handlers
        backButton.setOnAction(event -> handleBack());
        startGameButton.setOnAction(event -> handleStartGame());
    }

    private void updatePlayerNameFields(int count) {
        playerNamesContainer.getChildren().clear();
        for (int i = 1; i <= count; i++) {
            TextField nameField = new TextField();
            nameField.setPromptText("Player " + i + " Name");
            playerNamesContainer.getChildren().add(nameField);
        }
    }

    private void handleBack() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        try {
            // Load the menu screen first
            Parent menuRoot = App.loadFXML("menu/menu");
            Scene currentScene = registerRootPane.getScene();
            double sceneWidth = currentScene.getWidth();
            
            // Create a container for both screens
            StackPane container = new StackPane();
            menuRoot.setTranslateX(-sceneWidth);
            container.getChildren().addAll(registerRootPane, menuRoot);
            
            // Set the container as the new root
            currentScene.setRoot(container);
            
            // Create parallel transitions for both screens
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), registerRootPane);
            slideOut.setToX(sceneWidth);
            
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), menuRoot);
            slideIn.setToX(0);
            
            // Play both transitions
            slideOut.play();
            slideIn.play();
            
            // When animation finishes, clean up
            slideIn.setOnFinished(event -> {
                container.getChildren().remove(registerRootPane);
            });
            
        } catch (Exception e) {
            System.err.println("Error during transition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleStartGame() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        errorLabel.setText("");
        // Validate player names
        int playerCount = playerCountSpinner.getValue();
        String[] playerNames = new String[playerCount];
        java.util.HashSet<String> nameSet = new java.util.HashSet<>();
        for (int i = 0; i < playerCount; i++) {
            TextField nameField = (TextField) playerNamesContainer.getChildren().get(i);
            String name = nameField.getText().trim();
            if (!name.matches("[A-Za-z]{1,10}")) {
                errorLabel.setText("Error: Name must be 1 to 10 letters only.");
                return;
            }
            if (nameSet.contains(name)) {
                errorLabel.setText("Error: Name must be unique.");
                return;
            }
            nameSet.add(name);
            playerNames[i] = name;
            if (playerNames[i].isEmpty()) {
               errorLabel.setText("Player " + (i + 1) + " name is empty.");
                return;
            }
        }

        try {
            // Load the game scene
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/game/game.fxml"));
            Parent gameRoot = fxmlLoader.load();
            GameController controller = fxmlLoader.getController();
            
            // Get current scene dimensions
            Scene currentScene = registerRootPane.getScene();
            double sceneWidth = currentScene.getWidth();
            
            // Create container for transition
            StackPane container = new StackPane();
            gameRoot.setTranslateX(sceneWidth); // Start from right
            container.getChildren().addAll(registerRootPane, gameRoot);
            
            // Set the container as the root while maintaining size
            currentScene.setRoot(container);
            
            // Create transitions
            TranslateTransition slideOutCurrent = new TranslateTransition(Duration.millis(250), registerRootPane);
            slideOutCurrent.setToX(-sceneWidth);
            
            TranslateTransition slideInNew = new TranslateTransition(Duration.millis(250), gameRoot);
            slideInNew.setToX(0);
            
            // Play transitions
            slideOutCurrent.play();
            slideInNew.play();
            
            // Set up game after transition
            slideInNew.setOnFinished(event -> {
                container.getChildren().remove(registerRootPane);
                controller.registerPlayer(playerNames);
            });

        } catch (IOException e) {
            System.err.println("Error loading game scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


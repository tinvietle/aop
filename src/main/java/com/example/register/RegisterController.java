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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    private Label playerCountLabel;

    @FXML
    private ComboBox<String> difficultyComboBox;

    private Stage primaryStage;
    private Scene previousScene;

    private String difficulty;

    public void setPreviousScene(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.previousScene = scene;
    }

    @FXML
    public void initialize() {
        // Bind background image size to root pane size
        backgroundImage.fitWidthProperty().bind(registerRootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(registerRootPane.heightProperty());

        // Bind container sizes with minimum sizes
        playerNamesContainer.setMinWidth(250);
        playerNamesContainer.maxHeightProperty().bind(registerRootPane.heightProperty().multiply(0.4));
        playerNamesContainer.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.25));

        // Bind spinner size with minimum sizes
        playerCountSpinner.setMinWidth(100);
        playerCountSpinner.setMinHeight(30);
        playerCountSpinner.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.15));
        playerCountSpinner.prefHeightProperty().bind(registerRootPane.heightProperty().multiply(0.05));

        // Bind spinner font size with smaller minimum size
        playerCountSpinner.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", Math.max(10, registerRootPane.getWidth() * 0.015)),
                registerRootPane.widthProperty()
            )
        );

        // Fixed button sizes
        startGameButton.setPrefWidth(200);
        startGameButton.setPrefHeight(40);
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(40);

        // Bind font sizes only
        startGameButton.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", Math.min(20, registerRootPane.getWidth() * 0.02)),
                registerRootPane.widthProperty()
            )
        );

        backButton.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", Math.min(20, registerRootPane.getWidth() * 0.02)),
                registerRootPane.widthProperty()
            )
        );

        // Bind text sizes
        errorLabel.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", 
                    Math.max(14, registerRootPane.getHeight() * 0.02)),
                registerRootPane.heightProperty()
            )
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
                double top = registerRootPane.getHeight() * 0.05;
                return new Insets(top, 0, 0, 0);
            }, registerRootPane.heightProperty())
        );

        // Bind "Number of Players:" label
        playerCountLabel.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", 
                    Math.max(14, registerRootPane.getHeight() * 0.02)),
                registerRootPane.heightProperty()
            )
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

        errorLabel.styleProperty().bind(
            Bindings.concat("-fx-font-size: ", registerRootPane.heightProperty().multiply(0.018), "px;")
        );

        playerCountSpinner.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", registerRootPane.getWidth() * 0.02),
                registerRootPane.widthProperty()
            )
        );

        // Bind ComboBox size
        difficultyComboBox.prefWidthProperty().bind(registerRootPane.widthProperty().multiply(0.15));
        difficultyComboBox.prefHeightProperty().bind(registerRootPane.heightProperty().multiply(0.04));

        // Bind ComboBox font size
        difficultyComboBox.styleProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1fpx;", Math.max(10, registerRootPane.getWidth() * 0.012)),
                registerRootPane.widthProperty()
            )
        );

        difficultyComboBox.getItems().addAll("Easy", "Normal", "Hard");
        difficultyComboBox.setValue("Hard");

        // Bind text field font size with smaller minimum size
        playerNamesContainer.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                TextField nameField = (TextField) node;
                nameField.styleProperty().bind(
                    Bindings.createStringBinding(
                        () -> String.format("-fx-font-size: %.1fpx;", Math.max(10, registerRootPane.getWidth() * 0.015)),
                        registerRootPane.widthProperty()
                    )
                );
            }
        });
    }

    private void updatePlayerNameFields(int count) {
        playerNamesContainer.getChildren().clear();
        for (int i = 1; i <= count; i++) {
            TextField nameField = new TextField();
            nameField.setPromptText("Player " + i + " Name");
            nameField.setMinWidth(200);
            nameField.setMinHeight(25);

            // Add click sound when focusing on text field
            nameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) { // When gaining focus
                    SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
                }
            });

            // Bind font size to scene width with smaller minimum size
            nameField.styleProperty().bind(
                Bindings.createStringBinding(
                    () -> String.format("-fx-font-size: %.1fpx;", Math.max(10, registerRootPane.getWidth() * 0.015)),
                    registerRootPane.widthProperty()
                )
            );
            playerNamesContainer.getChildren().add(nameField);
        }
        
        // Play sound when changing player count
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
    }

    private void handleBack() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        if (primaryStage != null && previousScene != null) {
            primaryStage.setScene(previousScene);
            primaryStage.centerOnScreen();
        }
    }

    private void handleStartGame() {
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        errorLabel.setText("");
        difficulty = difficultyComboBox.getValue();
        // Use difficulty as needed
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
            // GameController controller = fxmlLoader.getController();
            GameController controller = new GameController(difficulty); // Create controller with argument
            fxmlLoader.setController(controller); // Manually set controller
            Parent gameRoot = fxmlLoader.load();
            
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


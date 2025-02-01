/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.help;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.function.Consumer;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

/*
 * Controls the help/tutorial interface of the game.
 * This class manages:
 * - Tutorial image navigation
 * - Responsive UI layout
 * - Transition animations
 * - User interaction handling
 */
public class HelpController {

    @FXML
    private Pane root;

    @FXML
    private StackPane contentPane;

    @FXML
    private ImageView gameTutorContent;

    @FXML
    private ImageView nextButton;

    @FXML
    private ImageView prevButton;

    @FXML
    private ImageView frame;

    @FXML
    private Text closeButton;

    private String[] tutorialImages;
    private int currentIndex = 0;

    private Stage primaryStage;
    private Scene previousScene;

    @FXML
    private void initialize() {
        /*
         * Initializes the help screen interface.
         * 
         * Features:
         * - Sets up clip region for root pane
         * - Loads tutorial images
         * - Configures responsive layout bindings
         * - Sets up navigation button effects
         * - Initializes hover animations
         */
        try {
            // Clip the root pane
            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(root.widthProperty());
            clip.heightProperty().bind(root.heightProperty());
            root.setClip(clip);

            // Load only images with "tutorial" in their names from the directory
            File dir = new File(getClass().getResource("/com/example/assets/helpScene").toURI());
            tutorialImages = Arrays.stream(dir.listFiles((d, name) -> name.endsWith(".png") && name.contains("tutorial")))
                    .map(file -> file.toURI().toString())
                    .sorted()
                    .toArray(String[]::new);

            // Check if there are any tutorial images
            if (tutorialImages.length == 0) {
                throw new IllegalStateException("No tutorial images found in the directory.");
            }

            // Display the first tutorial image
            gameTutorContent.setImage(new Image(tutorialImages[currentIndex]));

            // Set initial button visibility
            updateNavigationButtons();

            // Bind the StackPane size to the root pane size
            contentPane.prefWidthProperty().bind(root.widthProperty());
            contentPane.prefHeightProperty().bind(root.heightProperty());

            // Bind the imageView size to the root pane size
            gameTutorContent.fitWidthProperty().bind(root.widthProperty());
            gameTutorContent.fitHeightProperty().bind(root.heightProperty());
            nextButton.fitHeightProperty().bind(root.heightProperty().multiply(60.0 / 500.0));
            prevButton.fitHeightProperty().bind(root.heightProperty().multiply(60.0 / 500.0));
            frame.fitWidthProperty().bind(root.widthProperty());
            frame.fitHeightProperty().bind(root.heightProperty());

            // Bind location of the navigation buttons
            nextButton.layoutXProperty().bind(root.widthProperty().multiply(759.0 / 800.0));
            nextButton.layoutYProperty().bind(root.heightProperty().multiply(220.0 / 500.0));
            prevButton.layoutXProperty().bind(root.widthProperty().multiply(15.0 / 800.0));
            prevButton.layoutYProperty().bind(root.heightProperty().multiply(220.0 / 500.0));

            // Bind location of the close button
            closeButton.layoutXProperty().bind(root.widthProperty().multiply(374.0 / 800.0));
            closeButton.layoutYProperty().bind(root.heightProperty().multiply(480.0 / 500.0));

            // Bind the size of the close button
            closeButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.heightProperty().multiply(0.05).asString(), "px;",
                "-fx-cursor:", "hand;"
                ));
            // Create DropShadow for the glow effect
            DropShadow glowEffect = new DropShadow();
            glowEffect.setColor(Color.WHITE);
            glowEffect.spreadProperty().set(0.2);

            // Bind the glow radius to a fraction of the root pane height
            glowEffect.radiusProperty().bind(root.heightProperty().multiply(0.05));

            // Add hover effect for the nextButton
            nextButton.setOnMouseEntered(e -> nextButton.setEffect(glowEffect));
            nextButton.setOnMouseExited(e -> nextButton.setEffect(null));

            // Add hover effect for the prevButton
            prevButton.setOnMouseEntered(e -> prevButton.setEffect(glowEffect));
            prevButton.setOnMouseExited(e -> prevButton.setEffect(null));

            // Add hover effect for the closeButton
            closeButton.setOnMouseEntered(e -> closeButton.setEffect(glowEffect));
            closeButton.setOnMouseExited(e -> closeButton.setEffect(null));

        } catch (URISyntaxException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNextButtonClicked() {
        /*
         * Handles next button click to show next tutorial image.
         * 
         * Features:
         * - Validates navigation bounds
         * - Updates current image index
         * - Triggers transition animation
         */
        if (currentIndex < tutorialImages.length - 1) {
            currentIndex++;
            updateTutorialImage(true); // true for forward transition
        }
    }

    @FXML
    private void onPrevButtonClicked() {
        /*
         * Handles previous button click to show previous tutorial image.
         * 
         * Features:
         * - Validates navigation bounds
         * - Updates current image index
         * - Triggers transition animation
         */
        if (currentIndex > 0) {
            currentIndex--;
            updateTutorialImage(false); // false for backward transition
        }
    }

    private Consumer<Void> closeAction;

    public void setCloseAction(Consumer<Void> closeAction) {
        /*
         * Sets the callback action for closing the help screen.
         * 
         * Parameters:
         * - closeAction: Consumer to execute when closing
         */
        this.closeAction = closeAction;
    }

    @FXML
    private void closeHelp() {
        /*
         * Executes the close action if defined.
         * 
         * Features:
         * - Triggers registered close callback
         * - Handles cleanup
         */
        if (closeAction != null) {
            closeAction.accept(null); // Trigger the close action
        }
    }

    private void updateTutorialImage(boolean isForward) {
        /*
         * Updates the tutorial image with animation.
         * 
         * Parameters:
         * - isForward: Direction of navigation (true for next, false for previous)
         * 
         * Features:
         * - Slide-out animation for current image
         * - Image update
         * - Slide-in animation for new image
         * - Updates navigation button states
         */
        // Create a slide-out transition for the current image
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(100), gameTutorContent);
        slideOut.setFromX(0);
        slideOut.setToX(isForward ? -gameTutorContent.getFitWidth() : gameTutorContent.getFitWidth());

        slideOut.setOnFinished(e -> {
            // Update the image after the slide-out transition
            gameTutorContent.setImage(new Image(tutorialImages[currentIndex]));

            // Reset the position of the ImageView for the slide-in transition
            gameTutorContent.setTranslateX(isForward ? gameTutorContent.getFitWidth() : -gameTutorContent.getFitWidth());

            // Create a slide-in transition for the new image
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(100), gameTutorContent);
            slideIn.setFromX(isForward ? gameTutorContent.getFitWidth() : -gameTutorContent.getFitWidth());
            slideIn.setToX(0);
            slideIn.play();
        });

        slideOut.play();
        updateNavigationButtons();
    }

    private void updateNavigationButtons() {
        /*
         * Updates the visibility of navigation buttons.
         * 
         * Features:
         * - Hides prev button on first image
         * - Hides next button on last image
         */
        // Hide prevButton if on the first tutorial
        prevButton.setVisible(currentIndex > 0);

        // Hide nextButton if on the last tutorial
        nextButton.setVisible(currentIndex < tutorialImages.length - 1);
    }

    public void setPreviousScene(Stage stage, Scene scene) {
        /*
         * Stores references to previous scene for navigation.
         * 
         * Parameters:
         * - stage: Primary stage reference
         * - scene: Previous scene reference
         */
        this.primaryStage = stage;
        this.previousScene = scene;
    }
}

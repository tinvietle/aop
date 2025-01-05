package com.example.help;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

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
    private Text backButton;

    private String[] tutorialImages;
    private int currentIndex = 0;

    private Stage primaryStage;
    private Scene previousScene;

    @FXML
    private void initialize() {
        try {
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

            // Bind the text size to the root pane size
            backButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.heightProperty().multiply(0.04), ";"));

            // Bind location of the navigation buttons
            nextButton.layoutXProperty().bind(root.widthProperty().multiply(759.0 / 800.0));
            nextButton.layoutYProperty().bind(root.heightProperty().multiply(220.0 / 500.0));
            prevButton.layoutXProperty().bind(root.widthProperty().multiply(15.0 / 800.0));
            prevButton.layoutYProperty().bind(root.heightProperty().multiply(220.0 / 500.0));

            // Bind location of the back button
            backButton.layoutXProperty().bind(root.widthProperty().multiply(342.0 / 800.0));
            backButton.layoutYProperty().bind(root.heightProperty().multiply(485.0 / 500.0));

        } catch (URISyntaxException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNextButtonClicked() {
        if (currentIndex < tutorialImages.length - 1) {
            currentIndex++;
            updateTutorialImage(true); // true for forward transition
        }
    }

    @FXML
    private void onPrevButtonClicked() {
        if (currentIndex > 0) {
            currentIndex--;
            updateTutorialImage(false); // false for backward transition
        }
    }

    private void updateTutorialImage(boolean isForward) {
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
        // Hide prevButton if on the first tutorial
        prevButton.setVisible(currentIndex > 0);

        // Hide nextButton if on the last tutorial
        nextButton.setVisible(currentIndex < tutorialImages.length - 1);
    }

    @FXML
    private void backtoGame() {
        if (primaryStage != null && previousScene != null) {
            primaryStage.setScene(previousScene);
            primaryStage.centerOnScreen();
        }
    }

    public void setPreviousScene(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.previousScene = scene;
    }
}

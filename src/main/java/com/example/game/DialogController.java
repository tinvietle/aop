package com.example.game;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class DialogController {
    @FXML
    private StackPane welcomeOverlay;
    
    @FXML
    private Text welcomeText;
    
    @FXML
    private VBox dialogBox;

    public void customizeDialog(String text, double width, double height, double x, double y) {
        welcomeText.setText(text);
        
        // Set the size of both the overlay and dialog box
        welcomeOverlay.setPrefSize(width, height);
        welcomeOverlay.setMaxSize(width, height);
        dialogBox.setPrefSize(width, height);
        dialogBox.setMaxSize(width, height);
        
        // Position the dialog
        welcomeOverlay.setTranslateX(x);
        welcomeOverlay.setTranslateY(y);
        
        // Ensure text wraps properly
        welcomeText.setWrappingWidth(width - 40); // Account for padding
    }

    public void fadeOut() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), welcomeOverlay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> welcomeOverlay.setVisible(false));
        fadeOut.play();
    }

    public void setVisibility(boolean visible) {
        welcomeOverlay.setVisible(visible);
    }
}

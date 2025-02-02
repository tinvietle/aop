/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.game;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/*
 * Controls dialog overlays in the game interface.
 * This class manages dialog boxes with features like:
 * - Custom text display
 * - Configurable size and position
 * - Fade transitions
 * - Visibility control
 */
public class DialogController {
    @FXML
    private StackPane welcomeOverlay;
    
    @FXML
    private Text welcomeText;
    
    @FXML
    private VBox dialogBox;

    public void customizeDialog(String text, double width, double height, double x, double y) {
        /*
         * Configures the dialog box appearance and position.
         * 
         * Parameters:
         * - text: Content to display in the dialog
         * - width: Width of the dialog box
         * - height: Height of the dialog box
         * - x: Horizontal position
         * - y: Vertical position
         * 
         * Features:
         * - Sets text content
         * - Configures size constraints
         * - Positions the dialog
         * - Adjusts text wrapping
         */
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
        /*
         * Applies a fade out transition to the dialog.
         * 
         * Features:
         * - 1-second fade duration
         * - Hides overlay when complete
         * - Smooth opacity transition from 1 to 0
         */
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), welcomeOverlay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> welcomeOverlay.setVisible(false));
        fadeOut.play();
    }

    public void setVisibility(boolean visible) {
        /*
         * Controls the visibility of the dialog overlay.
         * 
         * Parameters:
         * - visible: Boolean flag to show/hide the dialog
         */
        welcomeOverlay.setVisible(visible);
    }
}

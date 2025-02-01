/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */

package com.example;

import java.io.IOException;

import com.example.misc.SoundManager;
import com.example.misc.Utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
 
    @Override
    public void start(Stage stage) {
        /*
         * Initializes and starts the JavaFX application.
         * 
         * Parameters:
         * - stage: Primary stage for the application
         * 
         * Features:
         * - Loads custom font
         * - Starts background music
         * - Sets up main menu scene
         * - Configures window properties
         * - Sets up close handler
         * 
         * Error Handling:
         * - Catches IO and null pointer exceptions
         * - Reports errors loading FXML
         */
        try {
            Font.loadFont(getClass().getResourceAsStream("/com/example/assets/font/PocketMonk.otf"), 14);
            SoundManager.getInstance().playRandomBGM();
            
            Parent root = loadFXML("menu/menu");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Age Of Pokemon");
            stage.getIcons().add(new Image(getClass().getResource("/com/example/assets/icon.png").toExternalForm())); // Set the application icon
            stage.setMaximized(true); 
            stage.setResizable(true); 
            stage.setOnCloseRequest(e -> {
                e.consume();
                closeProgram();
            });
            stage.show();
            
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        /*
         * Loads an FXML file and returns its root element.
         * 
         * Parameters:
         * - fxml: Path to the FXML file without extension
         * 
         * Returns:
         * - Parent node containing the loaded FXML content
         * 
         * Throws:
         * - IOException if FXML file cannot be loaded
         */
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        /*
         * Entry point of the application.
         * 
         * Parameters:
         * - args: Command line arguments (not used)
         */
        launch();
    }

    public void closeProgram() {
        /*
         * Handles application closure with confirmation.
         * 
         * Features:
         * - Shows confirmation dialog
         * - Exits if confirmed
         */
        if (Utils.confirmExit()) {
            System.exit(0);
        }
    }
}
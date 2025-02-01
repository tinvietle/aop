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
            // Print error
            // e.printStackTrace();
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // public static void main(String[] args) {
    //     launch();
    // }

    public void closeProgram() {
        if (Utils.confirmExit()) {
            System.exit(0);
        }
    }
}
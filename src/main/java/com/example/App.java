package com.example;

import java.io.IOException;
import java.util.Optional;

import com.example.misc.SoundManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    // private static Scene scene;
 
    @Override
    public void start(Stage stage) {
        try {
            SoundManager.getInstance().playRandomBGM();
            
            Parent root = loadFXML("menu/menu");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Age Of Pokemon");
            stage.setMaximized(true); 
            stage.setResizable(false); 
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public void closeProgram() {
        if (confirmExit()) {
            System.out.println("Close Program");
            System.exit(0);
        }
    }

    public Boolean confirmExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit the program.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

}
package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * JavaFX App
 */
public class App extends Application {

    // private static Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = loadFXML("menu");

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
            System.err.println(e.getMessage());
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    // static void setRoot(String fxml) throws IOException {
    //     scene.setRoot(loadFXML(fxml));
    // }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
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
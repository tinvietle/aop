package com.example.register;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class register_test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load Pocket Monk font:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/register/register.fxml"));
        Parent root = loader.load();
        
              
        stage.setTitle("Age of Pokemon");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setHeight(500);
        stage.setWidth(800);
        stage.setMaximized(true);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/register/register.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
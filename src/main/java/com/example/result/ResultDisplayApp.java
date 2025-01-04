package com.example.result;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.example.misc.Player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ResultDisplayApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load Pocket Monk font:
        Font.loadFont(getClass().getResourceAsStream("/com/example/assets/font/PocketMonk.otf"), 14);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/result/result.fxml"));
        Parent root = loader.load();
        
        // Set background image programmatically
        String imagePath = Paths.get("src\\main\\resources\\com\\example\\assets\\result.jpg").toUri().toString();
        Image backgroundImage = new Image(imagePath);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
        if (root instanceof HBox) {
            ((HBox) root).setBackground(new Background(background));
        }
        
        // For testing purposes
        ResultDisplay controller = loader.getController();
        List<Player> testPlayers = Arrays.asList(
            new Player("Alice") {{ updateScore(5); }},
            new Player("Bob") {{ updateScore(3); }},
            new Player("Charlie") {{ updateScore(1); }},
            new Player("David") {{ updateScore(4); }},
            new Player("Eve") {{ updateScore(2); }},
            new Player("Frank") {{ updateScore(6); }}
        );
        controller.displayResults(testPlayers);
        
        stage.setTitle("Age of Pokemon");
        stage.setMaximized(true);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/result/result.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.game;

import java.io.IOException;
import java.util.function.Consumer;

import com.example.App;
import com.example.misc.Pokemon;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameUtils {
    public static void disablePokemon(ImageView pokemon) {

        pokemon.setStyle("-fx-cursor: default;");
    }

    public static void enablePokemon(ImageView pokemon) {
        pokemon.setStyle("-fx-cursor: hand;");
    }

    public static void delay(long millis, Runnable continuation) {
      Task<Void> sleeper = new Task<Void>() {
          @Override
          protected Void call() throws Exception {
              try { Thread.sleep(millis); }
              catch (InterruptedException e) { }
              return null;
          }
      };
      sleeper.setOnSucceeded(event -> continuation.run());
      new Thread(sleeper).start();
    }

    public static FXMLLoader loadScene(String fxmlPath, String title, Stage stage, Consumer<FXMLLoader> beforeShow) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
        Parent root = loader.load();

        // Allow custom actions before showing the scene
        if (beforeShow != null) {
            beforeShow.accept(loader);
        }

        Scene scene = new Scene(root);
        
        // Add CSS if it exists (same path as FXML but with .css extension)
        String cssPath = fxmlPath.substring(0, fxmlPath.lastIndexOf('.')) + ".css";
        if (App.class.getResource(cssPath) != null) {
            scene.getStylesheets().add(App.class.getResource(cssPath).toExternalForm());
        }

        stage.setTitle(title);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        return loader;
    }

    public static void fadeTransition(Node node, long fadeDuration, long fadeOutDelay, Runnable callback) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(fadeDuration), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(fadeDuration), node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.millis(fadeOutDelay));
        fadeIn.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> callback.run());
    }

    public static void updateToolTip(Pokemon pokemon, ImageView image) {
        Tooltip tooltip = new Tooltip(pokemon.toString());
        tooltip.setShowDelay(javafx.util.Duration.ZERO);
        Tooltip.install(image, tooltip);
    }

    public static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait(); // Wait for the user to close the alert
    }
}

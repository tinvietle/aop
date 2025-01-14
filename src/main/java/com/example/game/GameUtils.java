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
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextFlow;
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

    public static void updateToolTip_deprecated(Pokemon pokemon, ImageView image) {
        Tooltip tooltip = new Tooltip(pokemon.toString());
        tooltip.setShowDelay(javafx.util.Duration.ZERO);
        Tooltip.install(image, tooltip);
    }

    public static void updateToolTip(Pokemon pokemon, ImageView image, BorderPane borderPane) {
        TextFlow styledContent = pokemon.getStyledTooltipContent(borderPane);
        Tooltip tooltip = new Tooltip();
        
        tooltip.setGraphic(styledContent);
        tooltip.getStyleClass().add("tooltip");
        
        // Set tooltip pref width and height binding borderPane size
        tooltip.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.28));
        // tooltip.prefHeightProperty().bind(borderPane.heightProperty().multiply(0.30));
        final double contentHeight;
        if (pokemon.getGroupOwner() != null) {
            contentHeight = 100;
        } else if (pokemon.getOwned()) {
            contentHeight = 170;
        } else {
            contentHeight = 164;
        }
        double defaultHeight = contentHeight * 1.1 * borderPane.getHeight() / 800 ; // Adjust multiplier as needed
        System.out.println("Height: " + defaultHeight);
        tooltip.setPrefHeight(defaultHeight);

        // Add listener for tooltip pref height
        borderPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double adjustedHeight = contentHeight * 1.1 * borderPane.getHeight() / 800; // Adjust multiplier as needed
            tooltip.setPrefHeight(adjustedHeight);
            System.out.println("Height: " + adjustedHeight);
        });

        double defaultPadding = borderPane.getWidth() * 0.002 ; // Adjust multiplier as needed
        System.out.println("Padding: " + defaultPadding);
        String dynamicPadding = String.format("-fx-padding: %.2f;",
            defaultPadding);
        tooltip.setStyle(dynamicPadding);

        // Bind tooltip padding to borderPane
        borderPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double paddingHorizontal = borderPane.getWidth() * 0.002; // Adjust multiplier as needed
            double borderWidth = borderPane.getWidth() * 0.002;
            System.out.println("Padding: " + paddingHorizontal);
            String newDynamicPadding = String.format(
                "-fx-padding: %.2f;", paddingHorizontal,
                "-fx-border-width: %.2f;", borderWidth
            );
            tooltip.setStyle(newDynamicPadding);
        });
        // String fixedPadding = "-fx-padding: 0.5em;";
        // tooltip.setStyle(fixedPadding);

        // Set the show delay to 0 milliseconds (instant display)
        tooltip.setShowDelay(javafx.util.Duration.ZERO);

        // Set the show duration to INDEFINITE
        tooltip.setShowDuration(Duration.INDEFINITE);

        Tooltip.install(image, tooltip);
    }

    public static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        // Apply styling
        styleDialog(alert);
        
        // Set resizable to allow content to expand
        alert.getDialogPane().setMinHeight(200);
        alert.setResizable(true);
        
        alert.showAndWait();
    }

    private static void styleDialog(Alert alert) {
        alert.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
            GameUtils.class.getResource("/misc/style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }
}

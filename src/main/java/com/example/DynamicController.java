package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class DynamicController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox dice;

    @FXML
    private VBox listOfPokemon;

    @FXML
    private ImageView machamp;

    @FXML
    private ImageView mapView;

    @FXML
    private StackPane stackPane;

    @FXML
    private Button button;

    @FXML
    public void initialize() {
        // Get screen dimensions
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Set stackPane size to screen dimensions
        stackPane.setPrefWidth(screenWidth);
        stackPane.setPrefHeight(screenHeight);

        // Bind stackPane's dimensions to the borderPane
        stackPane.prefWidthProperty().bind(borderPane.widthProperty());
        stackPane.prefHeightProperty().bind(borderPane.heightProperty());

        // Bind mapView's dimensions to the stackPane
        mapView.fitWidthProperty().bind(stackPane.widthProperty());
        mapView.fitHeightProperty().bind(stackPane.heightProperty());

        // Scale machamp's size proportionally
        machamp.fitWidthProperty().bind(stackPane.widthProperty().multiply(0.2)); // 20% of stackPane width
        machamp.fitHeightProperty().bind(stackPane.heightProperty().multiply(0.2)); // 20% of stackPane height

        // Adjust machamp's position proportionally
        machamp.layoutXProperty().bind(stackPane.widthProperty().multiply(136.0 / 518.0)); // Based on original layoutX relative to mapView width
        machamp.layoutYProperty().bind(stackPane.heightProperty().multiply(39.0 / 282.0)); // Based on original layoutY relative to mapView height

        // Scale HBox (dice) height
        dice.prefHeightProperty().bind(borderPane.heightProperty().multiply(0.2)); // 10% of BorderPane height
        dice.prefWidthProperty().bind(borderPane.widthProperty()); // Full width of BorderPane

        // Scale Button size within HBox
        button.prefWidthProperty().bind(dice.prefWidthProperty().multiply(0.1)); // 20% of HBox width
        button.prefHeightProperty().bind(dice.prefHeightProperty().multiply(0.7)); // Slightly larger than HBox height

        // Scale VBox (listOfPokemon) dimensions
        listOfPokemon.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.15)); // 15% of BorderPane width
        listOfPokemon.prefHeightProperty().bind(borderPane.heightProperty()); // Full height of BorderPane
    }
}

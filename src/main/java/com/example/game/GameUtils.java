package com.example.game;

import javafx.scene.image.ImageView;

public class GameUtils {
    public static void disablePokemon(ImageView pokemon) {

        pokemon.setStyle("-fx-cursor: default;");
    }

    public static void enablePokemon(ImageView pokemon) {
        pokemon.setStyle("-fx-cursor: hand;");
    }
}

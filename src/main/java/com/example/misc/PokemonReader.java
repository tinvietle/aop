package com.example.misc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PokemonReader {

    private String difficulty;

    public PokemonReader(String difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<Pokemon> readPokemons() {
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = getFilePathBasedOnDifficulty();

        try {
            // Parse JSON into a List of Pokemon objects
            ArrayList<Pokemon> pokemons = objectMapper.readValue(
                new File(filePath),
                new TypeReference<ArrayList<Pokemon>>() {}
            );

            // Print all Pokémon
            return pokemons;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an exception
        }
    }

    private String getFilePathBasedOnDifficulty() {
        switch (difficulty) {
            case "Easy":
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon_easy.json";
            case "Normal":
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon_medium.json";
            case "Hard":
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon.json";
            default:
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon.json";
        }
    }
}
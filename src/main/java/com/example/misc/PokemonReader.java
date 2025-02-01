/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.misc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PokemonReader {

    private String difficulty;

    public PokemonReader(String difficulty) {
        /*
         * Creates a new PokemonReader for specified difficulty level.
         * 
         * Parameters:
         * - difficulty: Game difficulty level that determines which JSON file to read
         */
        this.difficulty = difficulty;
    }

    public ArrayList<Pokemon> readPokemons() {
        /*
         * Reads Pokemon data from a JSON file based on difficulty level.
         * 
         * Process:
         * - Creates ObjectMapper for JSON parsing
         * - Determines file path based on difficulty
         * - Reads and parses JSON file
         * 
         * Returns:
         * - ArrayList<Pokemon> containing parsed Pokemon data
         * - Empty ArrayList if file reading or parsing fails
         */
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = getFilePathBasedOnDifficulty();

        try {
            // Parse JSON into a List of Pokemon objects
            ArrayList<Pokemon> pokemons = objectMapper.readValue(
                new File(filePath),
                new TypeReference<ArrayList<Pokemon>>() {}
            );

            return pokemons;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an exception
        }
    }

    private String getFilePathBasedOnDifficulty() {
        /*
         * Determines the appropriate JSON file path based on difficulty level.
         * 
         * Returns:
         * - String containing file path for the selected difficulty
         * - Defaults to hard difficulty if invalid difficulty is provided
         * 
         * File Paths:
         * - Easy: pokemon_easy.json
         * - Normal: pokemon_normal.json
         * - Hard: pokemon_hard.json
         */
        switch (difficulty) {
            case "Easy":
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon_easy.json";
            case "Normal":
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon_normal.json";
            case "Hard":
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon_hard.json";
            default:
                return "src\\main\\resources\\com\\example\\assets\\stocks\\pokemon_hard.json";
        }
    }
}
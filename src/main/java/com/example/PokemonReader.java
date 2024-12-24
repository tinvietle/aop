package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PokemonReader {
    public ArrayList<Pokemon> readPokemons() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse JSON into a List of Pokemon objects
            ArrayList<Pokemon> pokemons = objectMapper.readValue(
                new File("assets\\stocks\\pokemon.json"),
                new TypeReference<ArrayList<Pokemon>>() {}
            );

            // Print all Pokémon
            return pokemons;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an exception
        }
    }
}
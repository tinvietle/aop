package com.example.misc;

import java.util.List;
import java.util.ArrayList;

public class Player {
    String name;
    int score;
    List<Pokemon> capturedPokemons;
    int numGroups;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.capturedPokemons = new ArrayList<>();
        this.numGroups = 0;
    }

    public String getName() {
        return name;
    }  

    public int getScore() {
        return score;
    }

    public void updateScore(int points) {
        score += points;
    }

    public void addPokemon(Pokemon pokemon) {
        capturedPokemons.add(pokemon);
    }

    public void removePokemon(Pokemon pokemon) {
        capturedPokemons.remove(pokemon);
    }

    public int getNumCapturedPokemons() {
        return capturedPokemons.size();
    }

    public void updateNumGroup() {
        numGroups++;
    }

    public int getNumGroups() {
        return numGroups;
    }
}

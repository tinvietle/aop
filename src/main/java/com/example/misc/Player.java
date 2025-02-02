/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.misc;

import java.util.ArrayList;
import java.util.List;

public class Player {
    String name;
    int score;
    List<Pokemon> capturedPokemons;
    int numGroups;

    public Player(String name) {
        /*
         * Creates a new Player with default values.
         * 
         * Parameters:
         * - name: Player's name identifier
         * 
         * Initializes:
         * - score to 0
         * - empty list of captured Pokemon
         * - number of completed groups to 0
         */
        this.name = name;
        this.score = 0;
        this.capturedPokemons = new ArrayList<>();
        this.numGroups = 0;
    }

    public String getName() {
        /*
         * Retrieves the player's name.
         * 
         * Returns:
         * - String containing player's name
         */
        return name;
    }  

    public int getScore() {
        /*
         * Gets the player's current score.
         * 
         * Returns:
         * - Integer value of player's score
         */
        return score;
    }

    public void updateScore(int points) {
        /*
         * Updates the player's score by adding points.
         * 
         * Parameters:
         * - points: Number of points to add (can be negative)
         */
        score += points;
    }

    public void addPokemon(Pokemon pokemon) {
        /*
         * Adds a Pokemon to player's captured list.
         * 
         * Parameters:
         * - pokemon: Pokemon object to add
         */
        capturedPokemons.add(pokemon);
    }

    public void removePokemon(Pokemon pokemon) {
        /*
         * Removes a Pokemon from player's captured list.
         * 
         * Parameters:
         * - pokemon: Pokemon object to remove
         */
        capturedPokemons.remove(pokemon);
    }

    public int getNumCapturedPokemons() {
        /*
         * Gets the count of captured Pokemon.
         * 
         * Returns:
         * - Number of Pokemon in player's captured list
         */
        return capturedPokemons.size();
    }

    public void updateNumGroup() {
        /*
         * Increments the count of completed Pokemon groups.
         */
        numGroups++;
    }

    public int getNumGroups() {
        /*
         * Gets the number of completed Pokemon groups.
         * 
         * Returns:
         * - Number of completed groups
         */
        return numGroups;
    }
}
/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.misc;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

    @JsonProperty("name")
    private String name;
    @JsonProperty("score")
    private int score;
    @JsonProperty("pokemon")
    private List<String> pokemonNames;

    private ArrayList<Pokemon> pokemons;
    private Player owner;  
    
    public Group(String name, int score) {
        this.name = name;
        this.score = score;
        owner = null;
    }

    public Group() {}
    
    public Group(
        @JsonProperty("name") String name,
        @JsonProperty("score") int score,
        @JsonProperty("pokemon") List<String> pokemons
    ) {
        /*
         * Creates a Group with specified attributes.
         * 
         * Parameters:
         * - name: Group identifier
         * - score: Points value for completing the group
         * - pokemonNames: List of Pokemon names in this group
         */
        this.name = name;
        this.score = score;
        this.pokemonNames = pokemons;
        owner = null;
    }

    public void mapToPokemons(HashMap<String, Pokemon> pokemonMap) {
        pokemons = new ArrayList<>();
        for (String name : this.pokemonNames) {
            pokemons.add(pokemonMap.get(name));
        }
    }

    public boolean checkOwned(Player player) {
        /*
         * Checks if all Pokemon in the group are owned by a specific player.
         * 
         * Parameters:
         * - player: Player to check ownership against
         * 
         * Returns:
         * - true if player owns all Pokemon in group
         * - false otherwise
         */
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getOwner() != player) {
                return false;
            }
        }
        setOwner(player);
        return true;
    }

    public Player getOwner() {
        return owner;
    }   

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getName() {
        /*
         * Gets the group's name.
         * 
         * Returns:
         * - String name identifier of the group
         */
        return name;
    }

    public int getScore() {
        /*
         * Gets the group's score value.
         * 
         * Returns:
         * - Integer score for completing the group
         */
        return score;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }
}

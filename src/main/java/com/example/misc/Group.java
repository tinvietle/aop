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
        return name;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }
}

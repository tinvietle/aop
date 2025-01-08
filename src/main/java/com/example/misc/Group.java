package com.example.misc;

import java.util.List;

public class Group {
    private String name;
    private int score;
    private List<Pokemon> pokemons;
    private boolean isOwned;
    private Player owner;  

    public Group(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
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

    public void setOwner(Player owner) {
        this.isOwned = true;
        this.owner = owner;
    }
}

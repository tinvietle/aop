package com.example.misc;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    @JsonProperty("name")
    String name;

    @JsonProperty("score")
    int score;

    @JsonProperty("requirements")
    Map<String, List<Integer>> requirementsMap;

    @JsonProperty("description")
    String description;

    boolean owned;
    Player owner;
    String group;

    Pokeball requirements;

    public Pokemon() {}

    @JsonCreator
    public Pokemon(
        @JsonProperty("name") String name,
        @JsonProperty("score") int score,
        @JsonProperty("requirements") Map<String, List<Integer>> requirementsMap,
        @JsonProperty("description") String description
    ) {
        this.name = name;
        this.score = score;
        this.description = description;
        this.requirementsMap = requirementsMap;
        this.requirements = mapToPokeball(requirementsMap); // Convert the map to a Pokeball object
    }

    private Pokeball mapToPokeball(Map<String, List<Integer>> requirementsMap) {
        Pokeball pokeball = new Pokeball();
        for (Map.Entry<String, List<Integer>> entry : requirementsMap.entrySet()) {
            switch (entry.getKey()) {
                case "red ball":
                    pokeball.red = entry.getValue();
                    break;
                case "great ball":
                    pokeball.great = entry.getValue().get(0);
                    break;
                case "ultra ball":
                    pokeball.ultra = entry.getValue().get(0);
                    break;
                case "master ball":
                    pokeball.master = entry.getValue().get(0);
                    break;
            }
        }
        return pokeball;
    }

    @Override
    public String toString() {
        return  "Name: " + name + '\n' +
                "Score: " + score + '\n' +
                "Requirements: " + requirements.toString() + '\n' +
                "Description: " + description + '\n';
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }

    public boolean getOwned() {
        return owned;
    }

    public Player getOwner() {
        return owner;
    }

    public String getGroup() {
        return group;
    }

    public Pokeball getRequirements() {
        return requirements;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void updateOwner(Player owner) {
        this.owner = owner;
        if (!this.owned) {
            this.owned = true;
            requirements.addMasterBall();
        }
        owner.addPokemon(this);
    }
}

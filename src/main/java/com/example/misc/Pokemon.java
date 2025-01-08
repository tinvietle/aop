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
    @JsonProperty("group")
    String group;
    @JsonProperty("requirements")
    Map<String, List<Integer>> requirementsMap;
    @JsonProperty("description")
    String description;

    boolean owned;
    Player owner;
    Player groupOwner;
    Pokeball requirements;
    int groupScore;

    public Pokemon() {}

    @JsonCreator
    public Pokemon(
        @JsonProperty("name") String name,
        @JsonProperty("score") int score,
        @JsonProperty("group") String group,
        @JsonProperty("requirements") Map<String, List<Integer>> requirementsMap,
        @JsonProperty("description") String description
    ) {
        this.name = name;
        this.score = score;
        this.group = group;
        this.description = description;
        this.requirementsMap = requirementsMap;
        this.requirements = mapToPokeball(requirementsMap); // Convert the map to a Pokeball object
        this.owned = false;
        this.owner = null;
        this.groupOwner = null;
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
        if (groupOwner != null) {
            return  "Group " + this.group + " is already owned by Player " + groupOwner.getName() + '\n' + 
                    "Name: " + name + "\n" + 
                    "Score: " + score + '\n' +
                    "Group Score: " + groupScore + '\n';
        }
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

    public void setGroup(String group) {
        this.group = group;
    }

    public void setGroupScore(int groupScore) {
        this.groupScore = groupScore;
    }

    public void setGroupOwner(Player groupOwner) {
        this.groupOwner = groupOwner;
    }

    public Player getGroupOwner() {
        return groupOwner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        if (!this.owned) {
            this.owned = true;
            requirements.addMasterBall();
        }
        owner.addPokemon(this);
    }
}

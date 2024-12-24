package com.example.misc;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    @JsonProperty("name")
    String name;

    @JsonProperty("score")
    int score;

    @JsonProperty("requirements")
    Map<String, List<Integer>> requirements;

    @JsonProperty("description")
    String description;
    
    boolean owned;
    Player owner;
    String group;

    public Pokemon() {}

    public Pokemon(String name, int score, Map<String, List<Integer>> requirements, String description) {
        this.name = name;
        this.score = score;
        this.requirements = requirements;
        this.description = description;
    }

    @Override
    public String toString() {
        return  "Name: " + name + '\n' +
                "Score: " + score + '\n' +
                "Requirements: " + requirements + '\n' +
                "Description: " + description + '\n';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
}

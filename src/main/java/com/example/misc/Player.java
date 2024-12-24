package com.example.misc;

import java.util.List;
import java.util.ArrayList;

public class Player {
    String name;
    int score;
    List<Pokemon> capturedCastles;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.capturedCastles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }  

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }
}

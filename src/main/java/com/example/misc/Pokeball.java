package com.example.misc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Pokeball {
    List<Integer> red;
    int great;
    int ultra;
    int master;

    public Pokeball() {
        red = Arrays.asList(0);
        great = 0;
        ultra = 0;
        master = 0;
    }

    public Pokeball(List<Integer> red, int great, int ultra, int master) {
        this.red = red;
        this.great = great;
        this.ultra = ultra;
        this.master = master;
    }

    public Pokeball(String rollString) {
        this.red = new ArrayList<>(Arrays.asList(0));;
        this.great = 0;
        this.ultra = 0;
        this.master = 0;

        // Split the input string by commas
        String[] balls = rollString.split(",\\s*");

        // Define a pattern to capture the number and the ball name
        Pattern pattern = Pattern.compile("(\\d*)([A-Za-z]+)");

        // Iterate over the balls
        for (String ball : balls) {
            Matcher matcher = pattern.matcher(ball.trim());
            
            if (matcher.find()) {
                // Get the number (if any) and the ball name
                String numberStr = matcher.group(1);
                String ballName = matcher.group(2).toLowerCase();

                // If no number is provided, assume it's 1
                int number = (numberStr.isEmpty()) ? 1 : Integer.parseInt(numberStr);

                switch(ballName) {
                    case "pokeball":
                        this.red.add(number);
                        break;
                    case "greatball":
                        this.great++;
                        break;
                    case "ultraball":
                        this.ultra++;
                        break;
                    case "masterball":
                        this.master++;
                        break;
                }
            }
        }
    }

    public String toString() {
        return "- Red: " + redToString() + '\n' +
                "- Great: " + great + '\n' +
                "- Ultra: " + ultra + '\n' +
                "- Master: " + master;
    }

    public void addMasterBall() {
        this.master++;
    }

    // Getters and setters
    public List<Integer> getRed() {
        return red;
    }

    public void setRed(List<Integer> red) {
        this.red = red;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public int getUltra() {
        return ultra;
    }

    public void setUltra(int ultra) {
        this.ultra = ultra;
    }

    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public boolean compare(Pokeball pokeball) {
        return compareRed(this.red, pokeball.getRed()) &&
                this.great >= pokeball.getGreat() &&
                this.ultra >= pokeball.getUltra() &&
                this.master >= pokeball.getMaster();
    }

    public static boolean compareRed(List<Integer> roll, List<Integer> target) {
        List<Integer> rollCopy = new ArrayList<>(roll);
        List<Integer> targetCopy = new ArrayList<>(target);
    
        // Sort both lists in descending order for efficient matching
        Collections.sort(rollCopy, Collections.reverseOrder());
        Collections.sort(targetCopy, Collections.reverseOrder());
    
        // Use backtracking to check if we can match all targets
        return canMatchTargets(rollCopy, targetCopy);
    }
    
    private static boolean canMatchTargets(List<Integer> roll, List<Integer> target) {
        if (target.isEmpty()) {
            return true; // All targets matched
        }
    
        int currentTarget = target.get(0);
        for (int i = 0; i < roll.size(); i++) {
            int rollValue = roll.get(i);
    
            if (rollValue == currentTarget || rollValue > currentTarget) {
                // Use this roll element completely for this target
                List<Integer> newRoll = new ArrayList<>(roll);
                newRoll.remove(i);
                List<Integer> newTarget = new ArrayList<>(target);
                newTarget.remove(0); // Move to the next target
                if (canMatchTargets(newRoll, newTarget)) {
                    return true;
                }
            } else if (rollValue < currentTarget) {
                // Try to combine this roll element with others
                List<Integer> newRoll = new ArrayList<>(roll);
                newRoll.remove(i);
                List<Integer> newTarget = new ArrayList<>(target);
                newTarget.set(0, currentTarget - rollValue); // Reduce the current target by rollValue
                if (canMatchTargets(newRoll, newTarget)) {
                    return true;
                }
            }
        }
        return false; // No match found for this target
    }

    private String redToString() {
        return red.stream().map(String::valueOf) // Convert each Integer to String
                            .collect(Collectors.joining(", ")); // Join with ", ";
    }

    public List<String> getAllRequirements(){
        List<String> requirements = new ArrayList<>();
        System.out.println("Red: " + red);
        // if red not contains 0
        if (!red.contains(0)) {
            requirements.add("Red:" + redToString());
        }
        if (great > 0) {
            requirements.add("Great:" + great);
        }
        if (ultra > 0) {
            requirements.add("Ultra:" + ultra);
        }
        if (master > 0) {
            requirements.add("Master:" + master);
        }
        return requirements;
    }
}


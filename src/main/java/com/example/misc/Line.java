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
import java.util.Map;

/*
 * Represents a line of Pokeball requirements for catching Pokemon.
 * This class handles:
 * - Different types of Pokeball counts (red, great, ultra, master)
 * - Requirement satisfaction checking
 * - String representation of requirements
 */
public class Line {
    int red;
    int great;
    int ultra;
    int master;

    public Line() {
        /*
         * Initializes a Line with zero requirements for all Pokeball types.
         */
        red = 0;
        great = 0;
        ultra = 0;
        master = 0;
    }

    public Line(int red, int great, int ultra, int master) {
        /*
         * Creates a Line with specified Pokeball requirements.
         * 
         * Parameters:
         * - red: Number of red Pokeballs required
         * - great: Number of great Pokeballs required
         * - ultra: Number of ultra Pokeballs required
         * - master: Number of master Pokeballs required
         */
        this.red = red;
        this.great = great;
        this.ultra = ultra;
        this.master = master;
    }

    public Line(List<Integer> line) {
        /*
         * Creates a Line from a List of integers representing Pokeball counts.
         * 
         * Parameters:
         * - line: List containing counts in order [red, great, ultra, master]
         */
        this.red = line.get(0);
        this.great = line.get(1);
        this.ultra = line.get(2);
        this.master = line.get(3);
    }
    
    public Line(Line line) {
        /*
         * Creates a copy of an existing Line.
         * 
         * Parameters:
         * - line: Line object to copy
         */
        this.red = line.red;
        this.great = line.great;
        this.ultra = line.ultra;
        this.master = line.master;
    }
    
    public boolean isBigger(Line pokeball) {
        /*
         * Compares red Pokeball count with another Line.
         * 
         * Parameters:
         * - pokeball: Line to compare against
         * 
         * Returns:
         * - true if this Line has more red Pokeballs, false otherwise
         */
        return this.red > pokeball.red;
    }

    public String toString() {
        /*
         * Creates a string representation of the Line's requirements.
         * Only includes Pokeball types with non-zero counts.
         * 
         * Returns:
         * - String describing Pokeball requirements
         */
        StringBuilder out = new StringBuilder();

        if (red > 0) {
            out.append("Red(").append(red).append(") ");
        }
        if (great > 0) {
            out.append("Great(").append(great).append(") ");
        }
        if (ultra > 0) {
            out.append("Ultra(").append(ultra).append(") ");
        }
        if (master > 0) {
            out.append("Master(").append(master).append(") ");
        }

        return out.toString();
    }

    public boolean satisfied(Map<String, Integer> roll) {
        /*
         * Checks if a roll satisfies the Line's requirements.
         * For red Pokeballs, the roll must meet or exceed the requirement.
         * For other types, the roll must exactly match requirements.
         * 
         * Parameters:
         * - roll: Map containing roll results for each Pokeball type
         * 
         * Returns:
         * - true if requirements are satisfied, false otherwise
         */
        // Except for red, other balls must strictly match
        if (red > 0) {
            return roll.get("Red") >= red && roll.get("Great") == 0 && roll.get("Ultra") == 0 && roll.get("Master") == 0;
        }
        return  roll.get("Red") == 0 && roll.get("Great") == great && roll.get("Ultra") == ultra && roll.get("Master") == master;
    }
}

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

public class Requirement {
    List<Line> lines;

    public Requirement() {
        /*
         * Creates a new empty Requirement with no lines.
         */
        lines = new ArrayList<>();
    }

    public Requirement(List<Line> lines) {
        /*
         * Creates a Requirement with existing lines.
         * 
         * Parameters:
         * - lines: List of Line objects to initialize with
         */
        this.lines = lines;
    }

    public Requirement(Requirement requirement) {
        /*
         * Creates a deep copy of an existing Requirement.
         * 
         * Parameters:
         * - requirement: Requirement object to copy
         * 
         * Note:
         * - Creates new Line objects for each line in the original
         */
        lines = new ArrayList<>();
        for (Line line : requirement.getLines()) {
            lines.add(new Line(line));
        }
    }

    public void addLine(Line requirement) {
        /*
         * Adds a new line to the requirement.
         * 
         * Parameters:
         * - requirement: Line object to add
         */
        lines.add(requirement);
    }

    public List<Line> getLines() {
        /*
         * Gets all lines in the requirement.
         * 
         * Returns:
         * - List of Line objects
         */
        return lines;
    }

    public String toString() {
        /*
         * Creates a string representation of all requirement lines.
         * 
         * Returns:
         * - Formatted string showing each line's requirements
         * 
         * Format:
         * - Line 1: [requirements]
         * - Line 2: [requirements]
         * etc.
         */
        StringBuilder out = new StringBuilder();

        int lineCount = 1;
        for (Line line : lines) {
            out.append("- Line ").append(lineCount).append(": ").append(line.toString()).append("\n");
            lineCount++;
        }

        return out.toString();
    }
    
    public void removeLine(Line line) {
        /*
         * Removes a line from the requirement.
         * 
         * Parameters:
         * - line: Line object to remove
         */
        lines.remove(line);
    } 

    public int length() {
        /*
         * Gets the number of lines in the requirement.
         * 
         * Returns:
         * - Number of Line objects in the requirement
         */
        return lines.size();
    }
}

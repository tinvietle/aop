package com.example.misc;

import java.util.ArrayList;
import java.util.List;

public class Requirement {
    List<Line> lines;

    public Requirement() {
        lines = new ArrayList<>();
    }

    public Requirement(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line requirement) {
        lines.add(requirement);
    }

    public List<Line> getLines() {
        return lines;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();

        int lineCount = 1;
        for (Line line : lines) {
            out.append("- Line ").append(lineCount).append(": ").append(line.toString()).append("\n");
            lineCount++;
        }

        return out.toString();
    }
    
    public void removeLine(Line line) {
        lines.remove(line);
    } 

    public int length() {
        return lines.size();
    }
}

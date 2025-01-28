package com.example.misc;

import java.util.List;
import java.util.Map;

public class Line {
    int red;
    int great;
    int ultra;
    int master;

    public Line() {
        red = 0;
        great = 0;
        ultra = 0;
        master = 0;
    }

    public Line(int red, int great, int ultra, int master) {
        this.red = red;
        this.great = great;
        this.ultra = ultra;
        this.master = master;
    }

    public Line(List<Integer> line) {
        this.red = line.get(0);
        this.great = line.get(1);
        this.ultra = line.get(2);
        this.master = line.get(3);
    }
    
    public Line(Line line) {
        this.red = line.red;
        this.great = line.great;
        this.ultra = line.ultra;
        this.master = line.master;
    }
    
    public boolean isBigger(Line pokeball) {
        return this.red > pokeball.red;
    }

    public String toString() {
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
            out.append("Master( ").append(master).append(") ");
        }

        return out.toString();
    }

    public boolean satisfied(Map<String, Integer> roll) {
        // Except for red, other balls must strictly match
        if (red > 0) {
            return roll.get("Red") >= red && roll.get("Great") == 0 && roll.get("Ultra") == 0 && roll.get("Master") == 0;
        }
        return  roll.get("Red") == 0 && roll.get("Great") == great && roll.get("Ultra") == ultra && roll.get("Master") == master;
    }
}

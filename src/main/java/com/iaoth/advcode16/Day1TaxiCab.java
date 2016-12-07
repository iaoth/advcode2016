package com.iaoth.advcode16;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day1TaxiCab {
    static final String INPUT = "R3, L5, R2, L2, R1, L3, R1, R3, L4, R3, L1, L1, R1, L3, R2, L3, L2, R1, R1, L1, R4, L1, L4, R3, L2, L2, R1, L1, R5, R4, R2, L5, L2, R5, R5, L2, R3, R1, R1, L3, R1, L4, L4, L190, L5, L2, R4, L5, R4, R5, L4, R1, R2, L5, R50, L2, R1, R73, R1, L2, R191, R2, L4, R1, L5, L5, R5, L3, L5, L4, R4, R5, L4, R4, R4, R5, L2, L5, R3, L4, L4, L5, R2, R2, R2, R4, L3, R4, R5, L3, R5, L2, R3, L1, R2, R2, L3, L1, R5, L3, L5, R2, R4, R1, L1, L5, R3, R2, L3, L4, L5, L1, R3, L5, L2, R2, L3, L4, L1, R1, R4, R2, R2, R4, R2, R2, L3, L3, L4, R4, L4, L4, R1, L4, L4, R1, L2, R5, R2, R3, R3, L2, L5, R3, L3, R5, L2, R3, R2, L4, L3, L1, R2, L2, L3, L5, R3, L1, L3, L4, L3";
//    static final String INPUT = "R8, R4, R4, R8";

    public static void main(String[] args) {
        Map<Integer, Set<Integer>> visited = new HashMap<>();

        int x = 0, y = 0;
        int dx = 0, dy = -1;

        for (String command : INPUT.split(", ")) {
            int ndy;
            int ndx;
            if (command.startsWith("R")) {
                ndx = -dy;
                ndy = dx;
            } else {
                ndx = dy;
                ndy = -dx;
            }
            dx = ndx;
            dy = ndy;

            int steps = Integer.parseInt(command.substring(1));

            boolean done = false;
            for (int i = 0; i < steps; i++) {
                if (visited.containsKey(x) && visited.get(x).contains(y)) {
                    System.out.println("I've already been here: " + x + ", " + y);
                    done = true;
                    break;
                }

                visited.computeIfAbsent(x, k -> new HashSet<>()).add(y);

                x += dx;
                y += dy;
            }

            if (done) {
                break;
            }
        }

        System.out.println("Final position: " + x + ", " + y);
        System.out.println("Manhattan distance: " + (Math.abs(x) + Math.abs(y)));
    }
}

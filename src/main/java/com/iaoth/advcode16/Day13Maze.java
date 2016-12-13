package com.iaoth.advcode16;

import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Day13Maze {
    private static final int INPUT = 1352;
    private static final int XDEST = 31;
    private static final int YDEST = 39;

    public static void main(String... args) {
        Deque<int[]> queue = new ArrayDeque<>();
        int[] start = new int[]{1, 1, 0};
        queue.addLast(start);
        visitedSpace.add(new Pair<>(1, 1));

        boolean part2 = false;

        while (!queue.isEmpty()) {
            int[] step = queue.removeFirst();
            int x = step[0];
            int y = step[1];
            int length = step[2];

            if (x == XDEST && y == YDEST) {
                System.out.println("part1: " + length);
                break;
            }

            if (length == 50 && !part2) {
                System.out.println("part2: " + visitedSpace.size());
                part2 = true;
            }

            if (x > 0 && visitable(x - 1, y)) {
                queue.addLast(new int[]{x - 1, y, length + 1});
            }

            if (visitable(x + 1, y)) {
                queue.addLast(new int[]{x + 1, y, length + 1});
            }

            if (y > 0 && visitable(x, y - 1)) {
                queue.addLast(new int[]{x, y - 1, length + 1});
            }

            if (visitable(x, y + 1)) {
                queue.addLast(new int[]{x, y + 1, length + 1});
            }
        }
    }

    private static Set<Pair<Integer, Integer>> visitedSpace = new HashSet<>();

    private static boolean visitable(int x, int y) {
        Pair<Integer, Integer> coord = new Pair<>(x, y);
        if (visitedSpace.contains(coord)) {
            return false;
        }

        boolean openSpace = evenBits(x*x + 3*x + 2*x*y + y + y*y + INPUT);
        if (openSpace) {
            visitedSpace.add(coord);
        }

        return openSpace;
    }

    private static boolean evenBits(int val) {
        byte parity = 0;
        while (val > 0) {
            parity = (byte) (parity ^ (val & 1));
            val = val >> 1;
        }
        return parity == 0;
    }
}

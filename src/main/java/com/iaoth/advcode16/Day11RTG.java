package com.iaoth.advcode16;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11RTG {

    private static final Pattern CHIP = Pattern.compile("a ([a-z]+)-compatible microchip");
    private static final Pattern GENERATOR = Pattern.compile("a ([a-z]+) generator");
    static int nTypes;

    static public void main(String... arg) {
        List<HashSet<String>> floorChips = IntStream.range(0, 4)
                .mapToObj(i -> new HashSet<String>())
                .collect(Collectors.toList());
        List<HashSet<String>> floorGens = IntStream.range(0, 4)
                .mapToObj(i -> new HashSet<String>())
                .collect(Collectors.toList());
        Set<String> names = new HashSet<>();

        AdvCodeUtil.readResource("day11input.txt").lines().forEach(line -> {
            int floor = -1;
            if (line.contains("first floor")) {
                floor = 0;
            } else if (line.contains("second floor")) {
                floor = 1;
            } else if (line.contains("third floor")) {
                floor = 2;
            } else if (line.contains("fourth floor")) {
                floor = 3;
            }
            if (floor != -1) {
                Matcher chip = CHIP.matcher(line);
                while (chip.find()) {
                    floorChips.get(floor).add(chip.group(1));
                    names.add(chip.group(1));
                }

                Matcher generator = GENERATOR.matcher(line);
                while (generator.find()) {
                    floorGens.get(floor).add(generator.group(1));
                    names.add(generator.group(1));
                }
            }
        });

        nTypes = names.size();
        State start = new State(names, floorChips, floorGens);
        List<State> solution = solve(start);
        if (solution == null) {
            System.out.println("No solution found");
        } else {
            System.out.println(solution.size() - 1);
        }
    }

    private static List<State> solve(State start) {
        int length = 0;
        Deque<List<State>> paths = new ArrayDeque<>(Collections.singletonList(Collections.singletonList(start)));
        while (!paths.isEmpty()) {
            List<State> path = paths.removeFirst();
            List<List<State>> newPaths = generateStates(path);
            for (List<State> newPath : newPaths) {
                if (newPath.get(newPath.size() - 1).isWinner()) {
                    return newPath;
                }
            }

            paths.addAll(newPaths);

            if (length < path.size()) {
                length = path.size();
                System.out.println("Searching paths of length: " + length);
                System.out.println("Paths on queue: " + paths.size());
            }
        }

        return null;
    }

    private static List<List<State>> generateStates(List<State> states) {
        List<List<State>> paths = new ArrayList<>();

        State current = states.get(states.size() - 1);

        for (int dy = 1; dy >= -1; dy -= 2) {
            int nextFloor = current.floor + dy;
            if (nextFloor < 0 || nextFloor > 3) {
                continue;
            }

            for (int i = -1; i < nTypes * 2 - 1; i++) {
                for (int j = i + 1; j < nTypes * 2; j++) {
                    if (i >= 0 && !current.items[current.floor][i]) {
                        continue;
                    }
                    if (!current.items[current.floor][j]) {
                        continue;
                    }

                    State next = new State(current);
                    next.floor = nextFloor;

                    if (i >= 0) {
                        next.items[current.floor][i] = false;
                        next.items[next.floor][i] = true;
                    }
                    next.items[current.floor][j] = false;
                    next.items[next.floor][j] = true;

                    if (next.wouldFry())
                        continue;

                    if (states.contains(next))
                        continue;

                    List<State> path = new ArrayList<>(states);
                    path.add(next);

                    paths.add(path);
                }
            }
        }

        return paths;
    }

    static class State {
        boolean[][] items = new boolean[4][];
        int floor;

        State(State other) {
            for (int i = 0; i < 4; i++) {
                items[i] = new boolean[nTypes * 2];
                System.arraycopy(other.items[i], 0, items[i], 0, nTypes * 2);
            }
            floor = other.floor;
        }

        State(Set<String> names, List<HashSet<String>> floorChips, List<HashSet<String>> floorGens) {
            String[] nameArr = names.toArray(new String[names.size()]);
            for (int i = 0; i < 4; i++) {
                items[i] = new boolean[nTypes * 2];
                for (int j = 0; j < nTypes; j++) {
                    items[i][j] = floorChips.get(i).contains(nameArr[j]);
                    items[i][j + nTypes] = floorGens.get(i).contains(nameArr[j]);
                }
            }
        }

        boolean isWinner() {
            if (floor != 3)
                return false;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < nTypes * 2; j++) {
                    if (items[i][j]) {
                        return false;
                    }
                }
            }

            for (int j = 0; j < nTypes * 2; j++) {
                if (!items[3][j]) {
                    return false;
                }
            }

            return true;
        }

        boolean wouldFry() {
            boolean noGenerators = true;
            for (int i = 0; i < nTypes; i++) {
                if (items[floor][i + nTypes]) {
                    noGenerators = false;
                    break;
                }
            }
            if (noGenerators)
                return false;

            for (int i = 0; i < nTypes; i++) {
                if (items[floor][i] && !items[floor][i + nTypes])
                    return true;
            }

            return false;   // no chips
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof State))
                return false;
            final State other = (State) obj;
            if (this.floor != other.floor) {
                return false;
            }
            for (int i = 0; i < 4; i++) {
                if (!Arrays.equals(this.items[i], other.items[i]))
                    return false;
            }
            return true;
        }
    }
}

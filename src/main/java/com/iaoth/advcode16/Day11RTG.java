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

        AdvCodeUtil.readResource("day11test.txt").lines().forEach(line -> {
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
        if (nTypes > 16) {
            throw new RuntimeException("Implementation only handles a max of 16 types of RTGs");
        }

        State start = new State(names, floorChips, floorGens);
        State solution = solve(start);
        if (solution == null) {
            System.out.println("No solution found");
        } else {
            System.out.println(solution.depth);
        }
    }

    private static State solve(State start) {
        allStates.add(start);
        int depth = 0;
        int gc = 0;
        Deque<State> states = new ArrayDeque<>(Collections.singletonList(start));
        while (!states.isEmpty()) {
            State state = states.removeFirst();
            List<State> newStates = generateStates(state);

            if (newStates.isEmpty()) {
                continue;
            }

            for (State newState : newStates) {
                if (newState.isWinner()) {
                    return newState;
                }
            }

            states.addAll(newStates);

            if (depth < state.depth) {
                depth = state.depth;
                System.out.println("Searching paths of length: " + depth);
                System.out.println("Paths on queue: " + states.size());
                System.out.println("States so far: " + allStates.size());
            }
        }

        return null;
    }

    private static Set<State> allStates = new HashSet<>();

    private static List<State> generateStates(State current) {
        List<State> newStates = new ArrayList<>();

        for (int dy = 1; dy >= -1; dy -= 2) {
            int nextFloor = current.floor + dy;
            if (nextFloor < 0 || nextFloor > 3) {
                continue;
            }

            for (int i = -1; i < nTypes * 2 - 1; i++) {
                for (int j = i + 1; j < nTypes * 2; j++) {
                    int ibm = i >= 0 ? 1 << i : 0;
                    if (ibm != 0 && (current.items[current.floor] & ibm) == 0) {
                        continue;
                    }

                    int jbm = 1 << j;
                    if ((current.items[current.floor] & jbm) == 0) {
                        continue;
                    }

                    int bring = ibm | jbm;

                    State next = new State(current);
                    next.floor = nextFloor;

                    next.items[current.floor] = next.items[current.floor] & ~bring;
                    next.items[next.floor] = next.items[next.floor] | bring;

                    if (next.wouldFry())
                        continue;

                    if (allStates.contains(next))
                        continue;

                    allStates.add(next);

                    newStates.add(next);
                }
            }
        }

        return newStates;
    }

    static class State {
        static final int CHIPMASK = (1 << nTypes) - 1;
        int[] items = new int[4];
        int floor;
        int depth;

        State(State parent) {
            System.arraycopy(parent.items, 0, items, 0, 4);
            floor = parent.floor;
            this.depth = parent.depth + 1;
        }

        State(Set<String> names, List<HashSet<String>> floorChips, List<HashSet<String>> floorGens) {
            String[] nameArr = names.toArray(new String[names.size()]);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < nTypes; j++) {
                    if (floorChips.get(i).contains(nameArr[j])) {
                        items[i] = items[i] | (1 << j);
                    }
                    if (floorGens.get(i).contains(nameArr[j])) {
                        items[i] = items[i] | (1 << (j + nTypes));
                    }
                }
            }
            depth = 0;
        }

        boolean isWinner() {
            return floor == 3 && items[0] == 0 && items[1] == 0 && items[2] == 0 && items[3] != 0;
        }

        boolean wouldFry() {
            if (items[floor] == 0) {
                return false;
            }

            int chips = items[floor] & CHIPMASK;
            int gens = items[floor] >> nTypes;
            if (chips == 0 || gens == 0) {
                return false;
            }

            int shielded = chips & gens;
            return (chips & ~shielded) != 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof State))
                return false;
            final State other = (State) obj;
            return this.floor == other.floor && Arrays.equals(this.items, other.items);
        }
    }
}

package com.iaoth.advcode16;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15Timing {

    private static final Pattern PATTERN =
            Pattern.compile("Disc #\\d+ has (\\d+) positions; at time=0, it is at position (\\d+).");

    public static void main(String... args) {
        List<int[]> discs =
                AdvCodeUtil.readResource("day15input2.txt").lines()
                        .map(PATTERN::matcher)
                        .filter(Matcher::matches)
                        .map(m -> new int[]{Integer.parseInt(m.group(1)),
                                Integer.parseInt(m.group(2))})
                        .collect(Collectors.toList());

        int[] biggest = discs.stream().max(Comparator.comparing(d -> d[0])).orElseThrow(RuntimeException::new);

        long time = -biggest[1] - (discs.indexOf(biggest) + 1);
        boolean gotCapsule = false;
        while (!gotCapsule) {
            time += biggest[0];
            gotCapsule = true;
            for (int i = 0; i < discs.size(); i++) {
                int[] disc = discs.get(i);
                long pos = (disc[1] + time + i + 1) % disc[0];
                if (pos != 0) {
                    gotCapsule = false;
                    break;
                }
            }
        }
        System.out.println(time);
    }
}

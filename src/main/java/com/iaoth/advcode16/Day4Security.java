package com.iaoth.advcode16;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Joakim.Almgren on 2016-12-05.
 */
public class Day4Security {

    private static final Pattern REGEX = Pattern.compile("((?:[a-z]+-)+)([0-9]+)\\[([a-z]+)\\]");

    public static void main(String... args) {
        AdvCodeUtil.readResource("day4.txt")
                .lines()
                .map(REGEX::matcher)
                .filter(Matcher::matches)
                .filter(Day4Security::checkRoom)
                .map(Day4Security::decrypt)
                .forEach(System.out::println);
    }

    private static String decrypt(Matcher matcher) {
        String name = matcher.group(1);
        final int sector = getSectorID(matcher);

        return Arrays.stream(name.split(""))
                .map(str -> "-".equals(str) ? " " :
                        String.valueOf((char) ('a' + (str.charAt(0) + sector - 'a') % ('z' - 'a' + 1)))
                )
                .collect(Collectors.joining()) + "[" + sector + "]";
    }

    private static int getSectorID(Matcher matcher) {
        return Integer.parseInt(matcher.group(2));
    }

    private static boolean checkRoom(Matcher matcher) {
        String name = matcher.group(1);
        String checksum = matcher.group(3);

        Map<String, Integer> count = Arrays.stream(name.split(""))
                .filter(str -> !"-".equals(str))
                .collect(Collectors.toMap(
                        Function.identity(),
                        key -> 100 + 99 - (key.charAt(0) - 'a'),
                        (x, y) -> (x / 100 + y / 100) * 100 + x % 100
                ));

        String calcsum = count.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> b.compareTo(a)))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining());

        return calcsum.startsWith(checksum);
    }
}

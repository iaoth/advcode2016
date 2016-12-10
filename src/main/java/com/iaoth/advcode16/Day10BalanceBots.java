package com.iaoth.advcode16;

import javafx.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10BalanceBots {
    static Map<String, Bot> bots = new TreeMap<>();
    static Map<String, Integer> outputs = new TreeMap<>();

    static public void main(String... args) {
        List<Pair<String, String>> inputs = new ArrayList<>();

        Pattern in = Pattern.compile("value (\\d+) goes to (bot \\d+)");
        Pattern give = Pattern.compile("(bot \\d+) gives low to ((?:bot|output) \\d+) and high to ((?:bot|output) \\d+)");

        AdvCodeUtil.readResource("day10input.txt").lines().forEach(line -> {
            Matcher m = in.matcher(line);
            if (m.matches()) {
                inputs.add(new Pair<>(m.group(2), m.group(1)));
            } else {
                m = give.matcher(line);
                if (m.matches()) {
                    bots.put(m.group(1), new Bot(m.group(1), m.group(2), m.group(3)));
                }
            }
        });

        for (Pair<String, String> input : inputs) {
            bots.get(input.getKey()).giveValue(Integer.parseInt(input.getValue()));
        }

        outputs.forEach((output, value) -> System.out.println(output + " = " + value));
    }

    static class Bot {
        final String high, low, name;
        List<Integer> values = new ArrayList<>();

        Bot(String name, String lowDest, String highDest) {
            this.name = name;
            this.high = highDest;
            this.low = lowDest;
        }

        void giveValue(int value) {
            values.add(value);
            if (values.size() == 2) {
                values.sort(null);
                System.out.println(name + " has values " + values.get(0) + ", " + values.get(1));
                give(low, values.get(0));
                give(high, values.get(1));
            }
        }

        private void give(String dest, Integer value) {
            if (dest.startsWith("bot")) {
                bots.get(dest).giveValue(value);
            } else {
                outputs.put(dest, value);
            }
        }
    }
}

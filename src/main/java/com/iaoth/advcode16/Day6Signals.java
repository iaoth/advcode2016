package com.iaoth.advcode16;

import javafx.util.Pair;

import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.iaoth.advcode16.AdvCodeUtil.readResource;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

/**
 * Created by Joakim.Almgren on 2016-12-06.
 */
public class Day6Signals {
    static public void main(String... args) {
        System.out.println(
                readResource("day6input.txt").lines().flatMap(
                        line -> range(0, line.length()).mapToObj(
                                i -> new Pair<>(i, line.charAt(i))
                        )
                ).collect(toMap(
                        Pair::getKey,
                        p -> Stream.of(new SimpleEntry<>(p.getValue(), 1))
                                .collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue)),
                        (x, y) -> Stream.concat(x.keySet().stream(), y.keySet().stream())
                                .collect(toMap(
                                        Function.identity(),
                                        key -> x.getOrDefault(key, y.getOrDefault(key, 0)),
                                        (u, v) -> u + v
                                ))
                )).entrySet().stream().map(
                        entry -> entry.getValue().entrySet().stream()
                                .min((freq1, freq2) -> freq1.getValue().compareTo(freq2.getValue()))
                                .orElseThrow(() -> new RuntimeException("No min frequency found for pos " + entry.getKey()))
                                .getKey()
                ).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString()
        );
    }
}

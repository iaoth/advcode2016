package com.iaoth.advcode16;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.iaoth.advcode16.AdvCodeUtil.readResource;

/**
 * Created by Joakim.Almgren on 2016-12-07.
 */
public class Day7IPv7 {

    public static void main(String... args) {
        System.out.println(
                readResource("day7input.txt").lines()
                        .filter(Day7IPv7::supportsSSL)
                        .count()
        );
    }

    private static boolean supportsSSL(String ip) {
        List<String> hypernets = new ArrayList<>();
        List<String> supernets = new ArrayList<>();
        getHyperSuper(ip, hypernets, supernets);

        return supernets.stream()
                .flatMap(Day7IPv7::getBABs)
                .filter(bab -> hypernets.stream()
                        .filter(net -> net.contains(bab))
                        .findAny().isPresent()
                )
                .findAny().isPresent();
    }

    private static boolean supportsTLS(String ip) {
        List<String> hypernets = new ArrayList<>();
        List<String> supernets = new ArrayList<>();
        getHyperSuper(ip, hypernets, supernets);

        return supernets.stream().filter(Day7IPv7::hasABBA).findAny().isPresent() &&
                !hypernets.stream().filter(Day7IPv7::hasABBA).findAny().isPresent();
    }

    private static void getHyperSuper(String ip, List<String> hypernets, List<String> supernets) {
        Matcher matcher = Pattern.compile("\\[[a-z]+\\]").matcher(ip);
        int i = 0;
        while (matcher.find()) {
            int j = matcher.start();
            int k = matcher.end();
            hypernets.add(ip.substring(j, k));
            if (j > i) {
                supernets.add(ip.substring(i, j));
                i = k;
            }
        }
        if (ip.length() - 1 > i) {
            supernets.add(ip.substring(i));
        }
    }

    private static boolean hasABBA(String str) {
        for (int i = 0; i < str.length() - 3; i++) {
            if (str.charAt(i) != str.charAt(i + 1) &&
                    str.charAt(i) == str.charAt(i + 3) &&
                    str.charAt(i + 1) == str.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    private static Stream<String> getBABs(String str) {
        List<String> babs = new ArrayList<>();
        for (int i = 0; i < str.length() - 2; i++) {
            char x = str.charAt(i);
            char y = str.charAt(i + 1);
            char z = str.charAt(i + 2);
            if (x != y && x == z) {
                babs.add(String.valueOf(new char[]{y, x, y}));
            }
        }
        return babs.stream();
    }
}

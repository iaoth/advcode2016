package com.iaoth.advcode16;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joakim.Almgren on 2016-12-05.
 */
public class Day3Squares {
    static public void main(String... args) {
        int numValid = 0;

        List<List<Integer>> matrix =
                AdvCodeUtil.readResource("day3input.txt").lines().map(
                        row -> Arrays.stream(row.trim().split("\\s+"))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                ).collect(Collectors.toList());

        for (int y = 0; y < matrix.size(); y += 3) {
            for (int x = 0; x < 3; x++) {
                List<Integer> values = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    values.add(matrix.get(y + i).get(x));
                }

                boolean valid = true;
                for (int i = 0; i < values.size() && valid; i++) {
                    valid = checkSum(values, i);
                }

                if (valid) {
                    numValid++;
                }
            }
        }

        System.out.println(numValid);
    }

    private static boolean checkSum(List<Integer> values, int i) {
        Integer val = values.remove(i);
        Integer sum = values.stream().reduce((x, y) -> x + y).orElse(0);
        values.add(i, val);
        return sum > val;
    }
}

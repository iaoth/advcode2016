package com.iaoth.advcode16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.iaoth.advcode16.AdvCodeUtil.readResource;

public class Day8TwoFactor {

    private static final int HEIGHT = 6;
    private static final int WIDTH = 50;

    static public void main(String... args) {
        List<List<Boolean>> display = new ArrayList<>();
        for (int y = 0; y < HEIGHT; y++) {
            display.add(new ArrayList<>(Collections.nCopies(WIDTH, false)));
        }

        readResource("day8input.txt").lines().forEach(line -> {
            System.out.println(line);

            if (line.startsWith("rect ")) {
                int split = line.indexOf('x');
                int a = Integer.parseInt(line.substring(5, split));
                int b = Integer.parseInt(line.substring(split + 1));
                for (int x = 0; x < a; x++) {
                    for (int y = 0; y < b; y++) {
                        display.get(y).set(x, true);
                    }
                }
            } else if (line.startsWith("rotate")) {
                int equal = line.indexOf('=');
                int by = line.indexOf(" by ");
                int pos = Integer.parseInt(line.substring(equal + 1, by));
                int count = Integer.parseInt(line.substring(by + 4));

                if (line.startsWith("rotate row y=")) {
                    Collections.rotate(display.get(pos), count);
                } else if (line.startsWith("rotate column x=")) {
                    count = count % HEIGHT;
                    if (count < 0) {
                        count += HEIGHT;
                    }
                    for (int i = 0; i < count; i++) {
                        Boolean tmp = display.get(HEIGHT-1).get(pos);
                        for (int y = 0; y < HEIGHT; y++) {
                            tmp = display.get(y).set(pos, tmp);
                        }
                    }
                }
            }

            display.stream().map(row ->
                    row.stream().map(pixel -> pixel ? "#" : ".").collect(Collectors.joining())
            ).forEach(System.out::println);
            System.out.println();
        });
        System.out.println("Active pixels: " +
            display.stream().flatMap(List::stream).filter(Boolean::booleanValue).count()
        );
    }
}

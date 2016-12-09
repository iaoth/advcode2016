package com.iaoth.advcode16;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9Explosives {

    public static final Pattern COMMAND = Pattern.compile("\\((\\d+)x(\\d+)\\)");

    static public void main(String... args) throws IOException {
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    private static long part2() throws IOException {
        String input = AdvCodeUtil.readResource("day9input.txt").readLine();
        input = input.replaceAll("\\s", "");

        return decompressedLength(input);
    }

    private static long decompressedLength(String input) {
        Matcher matcher = COMMAND.matcher(input);
        int pos = 0;
        long decompressed = 0;
        while (matcher.find(pos)) {
            if (matcher.start() > pos) {
                decompressed += matcher.start() - pos;
            }
            int length = Integer.parseInt(matcher.group(1));
            int repeat = Integer.parseInt(matcher.group(2));
            decompressed += decompressedLength(input.substring(matcher.end(), matcher.end() + length)) * repeat;
            pos = matcher.end() + length;
        }
        return decompressed + input.length() - pos;
    }

    private static int part1() throws IOException {
        String input = AdvCodeUtil.readResource("day9input.txt").readLine();

        input = input.replaceAll("\\s", "");

        Matcher matcher = COMMAND.matcher(input);
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        while (pos < input.length()) {
            if (matcher.find(pos)) {
                if (matcher.start() > pos) {
                    sb.append(input, pos, matcher.start());
                }

                int length = Integer.parseInt(matcher.group(1));
                int repeat = Integer.parseInt(matcher.group(2));
                for (int i = 0; i < repeat; i++) {
                    sb.append(input, matcher.end(), matcher.end() + length);
                }

                pos = matcher.end() + length;
            } else {
                sb.append(input.substring(pos));
                break;
            }
        }

        return sb.length();
    }
}

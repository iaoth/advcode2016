package com.iaoth.advcode16;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14OneTimePad {
    private final static String input = "ihaygndm";
    private static final int LOOK_FORWARD = 1000 + 1;
    private static MessageDigest md5;

    public static void main(String... arg) throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("MD5");

        String[] hashes = new String[LOOK_FORWARD];
        Integer index = 0;

        for (int i = 0; i < LOOK_FORWARD; i++) {
            hashes[i] = generateHash(i);
        }

        List<String> keys = new ArrayList<>();
        while (keys.size() < 64) {
            String hash = hashes[index % LOOK_FORWARD];
            Character c = repeatingChars(hash, 3);
            if (c != null) {
                String rep5 = IntStream.range(0, 5).mapToObj(i -> String.valueOf(c)).collect(Collectors.joining());
                boolean found = false;
                for (int i = 0; i < LOOK_FORWARD; i++) {
                    if (i == index % LOOK_FORWARD) {
                        continue;
                    }

                    if (hashes[i].contains(rep5)) {
                        found = true;
                    }
                }
                if (found) {
                    keys.add(hash);
                    if (keys.size() == 64) {
                        break;
                    }
                }
            }
            hashes[index % LOOK_FORWARD] = generateHash(index + LOOK_FORWARD);
            index++;
        }
        System.out.println(index);
    }

    private static String generateHash(Integer index) {
        md5.reset();
        md5.update(input.getBytes());
        md5.update(index.toString().getBytes());

        String hash = DatatypeConverter.printHexBinary(md5.digest()).toLowerCase();

        for (int i = 0; i < 2016; i++) {
            hash = DatatypeConverter.printHexBinary(md5.digest(hash.getBytes())).toLowerCase();
        }

        return hash;
    }

    private static Character repeatingChars(String str, int repeat) {
        int i = 0;
        while (i < str.length() - repeat + 1) {
            int j = i + 1;
            while (j < i + repeat) {
                if (str.charAt(j) != str.charAt(i)) {
                    i = j;
                    break;
                }
                j++;
            }
            if (i != j)
                return str.charAt(i);
        }
        return null;
    }

}

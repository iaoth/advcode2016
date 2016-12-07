package com.iaoth.advcode16;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joakim.Almgren on 2016-12-05.
 */
public class Day5NiceGame {
    private static final String input = "ugkcyxxp";
    private static final int DISPLAY_INTERVAL = 400000;

    public static void main(String... arg) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        List<String> password = new ArrayList<>(Collections.nCopies(8, null));
        Integer index = 0;
        while (password.contains(null)) {
            md5.reset();
            md5.update(input.getBytes("UTF-8"));
            md5.update(index.toString().getBytes("UTF-8"));

            String hash = DatatypeConverter.printHexBinary(md5.digest()).toLowerCase();

            if (hash.startsWith("00000")) {
                int pos = Integer.parseInt(hash.substring(5, 6), 16);
                if (pos < 8 && password.get(pos) == null) {
                    password.set(pos, hash.substring(6, 7));
                }
            }

            index++;

            if (index % DISPLAY_INTERVAL == 0) {
                String filler = String.format("%x", (index / DISPLAY_INTERVAL) % 16);
                for (String ch : password) {
                    if (ch == null) {
                        ch = filler;
                    }
                    System.out.print(ch);
                }
                System.out.println();
            }
        }

        System.out.println(String.join("", password));
    }
}

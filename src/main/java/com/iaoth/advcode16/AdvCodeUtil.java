package com.iaoth.advcode16;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Joakim.Almgren on 2016-12-06.
 */
class AdvCodeUtil {
    static BufferedReader readResource(String filename) {
        return new BufferedReader(new InputStreamReader(getResourceAsStream(filename)));
    }

    static InputStream getResourceAsStream(String filename) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    }
}

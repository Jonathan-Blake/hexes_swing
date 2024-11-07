package com.example.hexes_nov.utility;

import java.util.Random;
import java.util.Set;

public class RandUtil {
    private static final Random r = new Random(90);

    public static <T> T pick(Set<T> enums) {
        return (T) enums.toArray()[r.nextInt(enums.size())];
    }
}

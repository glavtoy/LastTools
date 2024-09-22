package ru.lastlord.lasttools.buyer.util;

import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static int generateRandomNumber(int min, int max) {
        return RANDOM.nextInt(max-min+1)+min;
    }
}

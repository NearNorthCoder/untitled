package com.closeddoor.client.antiban;

import java.util.Random;

public class RandomizedDelay {

    private static final Random random = new Random();

    /**
     * Sleeps the thread for a randomized duration based on the action type.
     * @param baseMillis Base delay in milliseconds
     * @param varianceMillis Max variance to add/subtract from base
     */
    public static void sleep(int baseMillis, int varianceMillis) {
        int delay = baseMillis + random.nextInt(varianceMillis * 2 + 1) - varianceMillis;
        delay = Math.max(50, delay); // Avoid 0 or negative delay
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns a random boolean with ~50% chance.
     */
    public static boolean coinFlip() {
        return random.nextBoolean();
    }

    /**
     * Simulates a variable action delay such as a misclick recovery or idle hesitation.
     */
    public static void simulateHumanDelay() {
        sleep(400, 200);
    }

    /**
     * Small idle pause to simulate distraction or momentary pause.
     */
    public static void idlePause() {
        sleep(1000, 500);
    }

    /**
     * Longer idle pause like stepping away from keyboard.
     */
    public static void afkPause() {
        sleep(5000 + random.nextInt(5000), 1000);
    }
}

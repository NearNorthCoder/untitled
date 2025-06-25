package com.closeddoor.client.antiban;

import java.awt.*;
import java.util.Random;

public class HumanLikeMouse {

    private static final Robot robot;
    private static final Random random = new Random();

    static {
        Robot temp;
        try {
            temp = new Robot();
        } catch (AWTException e) {
            temp = null;
            e.printStackTrace();
        }
        robot = temp;
    }

    /**
     * Moves the mouse in a human-like way to the given screen coordinate.
     * @param targetX X coordinate to move to
     * @param targetY Y coordinate to move to
     */
    public static void moveMouseSmoothly(int targetX, int targetY) {
        if (robot == null) return;

        Point current = MouseInfo.getPointerInfo().getLocation();
        int steps = 10 + random.nextInt(10);
        for (int i = 0; i <= steps; i++) {
            int x = current.x + (targetX - current.x) * i / steps + random.nextInt(3) - 1;
            int y = current.y + (targetY - current.y) * i / steps + random.nextInt(3) - 1;
            robot.mouseMove(x, y);
            RandomizedDelay.sleep(20, 10);
        }
        robot.mouseMove(targetX, targetY);
    }

    /**
     * Slight mouse wiggle to simulate minor adjustment or fidgeting.
     */
    public static void smallMouseWiggle() {
        if (robot == null) return;

        Point current = MouseInfo.getPointerInfo().getLocation();
        int x = current.x + random.nextInt(5) - 2;
        int y = current.y + random.nextInt(5) - 2;
        robot.mouseMove(x, y);
    }

    /**
     * Randomly decides whether to perform a passive mouse action.
     */
    public static void maybeFidget() {
        if (RandomizedDelay.coinFlip()) {
            smallMouseWiggle();
        }
    }
}

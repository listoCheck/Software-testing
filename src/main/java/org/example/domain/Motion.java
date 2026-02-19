package org.example.domain;

public class Motion {
    public static void glideToWindow(Person person, Window window) {
        person.setPosition(Position.AIR);
        if (window.getFloor() == 2) {
            person.setPosition(Position.WINDOW_SECOND_FLOOR);
        }
    }
}


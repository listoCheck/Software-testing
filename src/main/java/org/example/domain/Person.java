package org.example.domain;

import java.util.Objects;

public class Person {
    private final String name;
    private Position position;
    private Mood mood;

    public Person(String name) {
        this.name = Objects.requireNonNull(name);
        this.position = Position.GROUND;
        this.mood = Mood.NEUTRAL;
    }

    public String getName() { return name; }
    public Position getPosition() { return position; }
    public Mood getMood() { return mood; }

    public void setPosition(Position position) { this.position = position; }
    public void setMood(Mood mood) { this.mood = mood; }
}


package org.example.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Crowd {
    private final List<Person> people = new ArrayList<>();

    public void add(Person p) { people.add(p); }
    public List<Person> getPeople() { return Collections.unmodifiableList(people); }

    public void cheer() {
        for (Person p : people) {
            p.setMood(Mood.CHEERING);
        }
    }
}


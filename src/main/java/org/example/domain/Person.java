package org.example.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Person {
    @NonNull
    private final String name;
    private Position position = Position.GROUND;
    private Mood mood = Mood.NEUTRAL;
}

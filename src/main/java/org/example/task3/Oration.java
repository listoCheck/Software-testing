package org.example.task3;

import lombok.Getter;

@Getter
public class Oration {
    private final Person speaker;
    private final Stage stage;

    public Oration(Person speaker, Stage stage) {
        this.speaker = speaker;
        this.stage = stage;
        this.speaker.setPosition(Position.STAGE);
    }
}

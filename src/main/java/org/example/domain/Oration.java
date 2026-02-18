package org.example.domain;

public class Oration {
    private final Person speaker;
    private final Stage stage;

    public Oration(Person speaker, Stage stage) {
        this.speaker = speaker;
        this.stage = stage;
        this.speaker.setPosition(Position.STAGE);
    }

    public Person getSpeaker() { return speaker; }
    public Stage getStage() { return stage; }
}


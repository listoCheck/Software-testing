package org.example.task3;

public class Stage {
    private Boolean mic;
    public Stage() {
        this.mic = true;
    }
    public Stage(Boolean mic) {
        this.mic = mic;
    }
    public Boolean getMic() { return mic; }
    public void setMic(Boolean mic) { this.mic = mic; }
}

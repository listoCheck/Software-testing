package org.example.domain;

public class Scene {
    private final Crowd crowd;
    private final Building building;
    private final Window window;
    private final Stage stage;
    private final Oration oration;
    private final Person arthur;

    public Scene(Crowd crowd, Building building, Window window, Stage stage, Oration oration, Person arthur) {
        this.crowd = crowd;
        this.building = building;
        this.window = window;
        this.stage = stage;
        this.oration = oration;
        this.arthur = arthur;
    }

    public Crowd getCrowd() { return crowd; }
    public Building getBuilding() { return building; }
    public Window getWindow() { return window; }
    public Stage getStage() { return stage; }
    public Oration getOration() { return oration; }
    public Person getArthur() { return arthur; }
}


package org.example.domain;

public class SceneFactory {
    public static Scene createScene() {
        Crowd crowd = new Crowd();
        for (int i = 0; i < 5; i++) crowd.add(new Person("Person" + i));
        crowd.cheer();

        Building building = new Building(3);
        Window window = new Window(2);
        Stage stage = new Stage(true);

        Person orator = new Person("Orator");
        Oration oration = new Oration(orator, stage);

        Person arthur = new Person("Arthur");
        Motion.glideToWindow(arthur, window);

        return new Scene(crowd, building, window, stage, oration, arthur);
    }
}


package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Scene {
    private final Crowd crowd;
    private final Building building;
    private final Window window;
    private final Stage stage;
    private final Oration oration;
    private final Person arthur;
}

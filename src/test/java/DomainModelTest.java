import org.example.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DomainModelTest {

    @Test
    void sceneReflectsNarrativeFacts() {
        Scene s = SceneFactory.createScene();

        assertTrue(s.getCrowd().getPeople().stream().allMatch(p -> p.getMood() == Mood.CHEERING));

        assertTrue(s.getBuilding().getFloors() >= 2);
        assertEquals(2, s.getWindow().getFloor());

        assertEquals(Position.STAGE, s.getOration().getSpeaker().getPosition());

        assertEquals("Arthur", s.getArthur().getName());
        assertEquals(Position.WINDOW_SECOND_FLOOR, s.getArthur().getPosition());
    }

    @Test
    void motionUpdatesPositionsConsistently() {
        Person p = new Person("Test");
        Window w = new Window(2);
        assertEquals(Position.GROUND, p.getPosition());
        Motion.glideToWindow(p, w);
        assertEquals(Position.WINDOW_SECOND_FLOOR, p.getPosition());
    }
}


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

    @Test
    void crowdCheer_isIdempotentAndAffectsAll() {
        Crowd c = new Crowd();
        c.add(new Person("a"));
        c.add(new Person("b"));
        c.cheer();
        c.cheer(); // idempotent
        assertTrue(c.getPeople().stream().allMatch(p -> p.getMood() == Mood.CHEERING));
    }

    @Test
    void orationPlacesSpeakerOnStage() {
        Person speaker = new Person("spk");
        Stage stage = new Stage();
        Oration oration = new Oration(speaker, stage);
        assertEquals(Position.STAGE, oration.getSpeaker().getPosition());
        assertSame(stage, oration.getStage());
    }

    @Test
    void motionToSecondFloorWindow_updatesToWindowPosition() {
        Person p = new Person("arthur");
        Motion.glideToWindow(p, new Window(2));
        assertEquals(Position.WINDOW_SECOND_FLOOR, p.getPosition());
    }

    @Test
    void motionToOtherFloor_putsInAirButNotSecondFloor() {
        Person p = new Person("arthur");
        Motion.glideToWindow(p, new Window(3));
        assertEquals(Position.AIR, p.getPosition());
    }

    @Test
    void sceneFactory_defaultsAreConsistent() {
        Scene s = SceneFactory.createScene();
        assertNotNull(s.getCrowd());
        assertNotNull(s.getBuilding());
        assertNotNull(s.getWindow());
        assertNotNull(s.getStage());
        assertNotNull(s.getOration());
        assertEquals("Arthur", s.getArthur().getName());
        assertEquals(2, s.getWindow().getFloor());
    }
}


package org.example.integration.mocks;

import org.example.math.SystemFunction;
import org.example.integration.support.RecordingFunction;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
public class IntegrationSystemRoutingWithMocksTest {
    @Test
    void routesToTrigForNegativeX() {
        RecordingFunction trig = new RecordingFunction(x -> -100.0);
        RecordingFunction log = new RecordingFunction(x -> 200.0);
        SystemFunction system = new SystemFunction(trig, log);

        assertEquals(-100.0, system.value(-1.0), 1e-12);
        assertEquals(List.of(-1.0), trig.calls());
        assertEquals(0, log.callCount());
    }

    @Test
    void routesToTrigAtBoundaryXZero() {
        RecordingFunction trig = new RecordingFunction(x -> -50.0);
        RecordingFunction log = new RecordingFunction(x -> 50.0);
        SystemFunction system = new SystemFunction(trig, log);

        assertEquals(-50.0, system.value(0.0), 1e-12);
        assertEquals(List.of(0.0), trig.calls());
        assertEquals(0, log.callCount());
    }

    @Test
    void routesToLogForPositiveX() {
        RecordingFunction trig = new RecordingFunction(x -> -100.0);
        RecordingFunction log = new RecordingFunction(x -> 200.0);
        SystemFunction system = new SystemFunction(trig, log);

        assertEquals(200.0, system.value(2.0), 1e-12);
        assertEquals(0, trig.callCount());
        assertEquals(List.of(2.0), log.calls());
    }
}

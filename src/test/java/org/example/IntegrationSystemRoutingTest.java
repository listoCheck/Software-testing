package org.example;

import org.example.math.MathFunction;
import org.example.math.SystemFunction;
import org.example.stub.StubFunction;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationSystemRoutingTest {
    @Test
    void routesToTrigWhenXLeqZero() {
        MathFunction trigStub = new StubFunction(Map.of(-1.0, 42.0), 1e-9);
        MathFunction logStub = new StubFunction(Map.of(1.0, 7.0), 1e-9);
        SystemFunction system = new SystemFunction(trigStub, logStub);

        assertEquals(42.0, system.value(-1.0), 1e-9);
    }

    @Test
    void routesToLogWhenXGreaterZero() {
        MathFunction trigStub = new StubFunction(Map.of(-1.0, 42.0), 1e-9);
        MathFunction logStub = new StubFunction(Map.of(2.0, 7.0), 1e-9);
        SystemFunction system = new SystemFunction(trigStub, logStub);

        assertEquals(7.0, system.value(2.0), 1e-9);
    }
}

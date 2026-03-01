package org.example;

import org.example.math.MathFunction;
import org.example.math.TrigSystem;
import org.example.stub.StubFunction;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTrigSystemTest {
    @Test
    void trigSystemAlwaysDivisionByZeroBecauseCosMinusCos() {
        MathFunction sec = new StubFunction(Map.of(-1.0, 2.0), 1e-9);
        MathFunction cos = new StubFunction(Map.of(-1.0, 3.0), 1e-9);
        MathFunction csc = new StubFunction(Map.of(-1.0, 4.0), 1e-9);
        MathFunction tan = new StubFunction(Map.of(-1.0, 5.0), 1e-9);
        MathFunction cot = new StubFunction(Map.of(-1.0, 6.0), 1e-9);

        TrigSystem trigSystem = new TrigSystem(sec, cos, csc, tan, cot, 1e-9);

        assertThrows(ArithmeticException.class, () -> trigSystem.value(-1.0));
    }
}

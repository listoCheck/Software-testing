package org.example;

import org.example.math.LogSystem;
import org.example.math.MathFunction;
import org.example.stub.StubFunction;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationLogSystemTest {
    @Test
    void logSystemUsesProvidedLogFunctions() {
        MathFunction log3 = new StubFunction(Map.of(2.0, 2.0), 1e-9);
        MathFunction log10 = new StubFunction(Map.of(2.0, 3.0), 1e-9);
        LogSystem logSystem = new LogSystem(log3, log10, 1e-9);

        double expected = Math.pow(Math.pow((2.0 - 2.0 + 2.0 * 3.0), 2.0) / 3.0, 2.0);
        assertEquals(expected, logSystem.value(2.0), 1e-9);
    }

    @Test
    void logSystemThrowsWhenLog10Zero() {
        MathFunction log3 = new StubFunction(Map.of(2.0, 2.0), 1e-9);
        MathFunction log10 = new StubFunction(Map.of(2.0, 0.0), 1e-9);
        LogSystem logSystem = new LogSystem(log3, log10, 1e-9);

        assertThrows(ArithmeticException.class, () -> logSystem.value(2.0));
    }
}

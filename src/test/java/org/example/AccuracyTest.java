package org.example;

import org.example.math.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccuracyTest {
    @Test
    void trigSystemThrowsForAnyXBecauseCosMinusCos() {
        double eps = 1e-10;
        SeriesSin sin = new SeriesSin(eps);
        Cos cos = new Cos(sin);
        TrigSystem trigSystem = new TrigSystem(
                new Sec(cos, eps),
                cos,
                new Csc(sin, eps),
                new Tan(sin, cos, eps),
                new Cot(sin, cos, eps),
                eps
        );

        assertThrows(ArithmeticException.class, () -> trigSystem.value(-0.7));
    }
}

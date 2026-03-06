package org.example;

import org.example.math.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTrigSystemTest {
    @Tag("integration")
    @Test
    void trigSystemAlwaysDivisionByZeroBecauseCosMinusCos() {
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

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(-1.0));
        assertEquals("division by zero in trig denominator", ex.getMessage());
    }
}

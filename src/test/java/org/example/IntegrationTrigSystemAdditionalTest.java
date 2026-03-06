package org.example;

import org.example.math.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTrigSystemAdditionalTest {
    private static TrigSystem createTrigSystem(double eps) {
        SeriesSin sin = new SeriesSin(eps);
        Cos cos = new Cos(sin);
        return new TrigSystem(
                new Sec(cos, eps),
                cos,
                new Csc(sin, eps),
                new Tan(sin, cos, eps),
                new Cot(sin, cos, eps),
                eps
        );
    }

    @Tag("integration")
    @ParameterizedTest
    @CsvSource({
            "-2.3",
            "-2.0",
            "-1.3",
            "-1.0",
            "-0.8",
            "-0.7",
            "-0.5",
            "-0.3"
    })
    void trigSystemThrowsForAllXBecauseDenominatorZero(double x) {
        TrigSystem trigSystem = createTrigSystem(1e-10);
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(x));
        assertEquals("division by zero in trig denominator", ex.getMessage());
    }
}

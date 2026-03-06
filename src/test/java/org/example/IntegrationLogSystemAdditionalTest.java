package org.example;

import org.example.math.LogSystem;
import org.example.math.Logarithm;
import org.example.math.SeriesLn;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationLogSystemAdditionalTest {
    private static LogSystem createLogSystem(double eps) {
        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        return new LogSystem(log3, log10, eps);
    }

    @Tag("integration")
    @ParameterizedTest
    @CsvSource({
            "0.2",
            "0.5",
            "1.5",
            "2.0",
            "2.5",
            "3.0",
            "5.0",
            "7.0",
            "10.0",
            "20.0"
    })
    void logSystemMatchesMathLogForTypicalX(double x) {
        double eps = 1e-10;
        LogSystem logSystem = createLogSystem(eps);

        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log(x) / Math.log(10.0);
        double expected = Math.pow(Math.pow(log3Val * log10Val, 2.0) / log10Val, 2.0);

        assertEquals(expected, logSystem.value(x), 1e-6);
    }

    @Tag("integration")
    @Test
    void logSystemPropagatesDomainErrorForNonPositiveX() {
        LogSystem logSystem = createLogSystem(1e-10);
        assertThrows(IllegalArgumentException.class, () -> logSystem.value(0.0));
    }
}

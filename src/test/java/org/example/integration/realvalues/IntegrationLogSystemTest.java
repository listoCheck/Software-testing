package org.example.integration.realvalues;

import org.example.math.LogSystem;
import org.example.math.Logarithm;
import org.example.math.SeriesLn;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationLogSystemTest {
    @Tag("integration")
    @Test
    void logSystemUsesProvidedLogFunctions() {
        double eps = 1e-10;
        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, eps);

        double x = 2.0;
        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log(x) / Math.log(10.0);
        double expected = Math.pow(Math.pow(log3Val * log10Val, 2.0) / log10Val, 2.0);
        assertEquals(expected, logSystem.value(x), 1e-6);
    }

    @Tag("integration")
    @Test
    void logSystemThrowsWhenLog10Zero() {
        double eps = 1e-10;
        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, eps);

        assertThrows(ArithmeticException.class, () -> logSystem.value(1.0));
    }
}

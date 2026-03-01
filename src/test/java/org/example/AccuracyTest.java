package org.example;

import org.example.math.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccuracyTest {
    @Test
    void sinSeriesMatchesMath() {
        SeriesSin sin = new SeriesSin(1e-12);
        double x = -1.2345;
        assertEquals(Math.sin(x), sin.value(x), 1e-9);
    }

    @Test
    void lnSeriesMatchesMath() {
        SeriesLn ln = new SeriesLn(1e-12);
        double x = 2.5;
        assertEquals(Math.log(x), ln.value(x), 1e-9);
    }

    @Test
    void logSystemMatchesMathForPositiveX() {
        double eps = 1e-10;
        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, eps);

        double x = 2.2;
        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log10(x);
        double expected = Math.pow(Math.pow((log3Val - log3Val + log3Val * log10Val), 2.0) / log10Val, 2.0);
        assertEquals(expected, logSystem.value(x), 1e-6);
    }

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

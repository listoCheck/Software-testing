package org.example.integration.realvalues;

import org.example.math.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationSystemRoutingTest {
    @Tag("integration")
    @Test
    void routesToTrigWhenXLeqZero() {
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

        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, eps);

        SystemFunction system = new SystemFunction(trigSystem, logSystem);

        assertThrows(ArithmeticException.class, () -> system.value(-1.0));
    }

    @Tag("integration")
    @Test
    void routesToLogWhenXGreaterZero() {
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

        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, eps);

        SystemFunction system = new SystemFunction(trigSystem, logSystem);

        double x = 2.0;
        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log(x) / Math.log(10.0);
        double expected = Math.pow(Math.pow(log3Val * log10Val, 2.0) / log10Val, 2.0);
        assertEquals(expected, system.value(x), 1e-6);
    }
}

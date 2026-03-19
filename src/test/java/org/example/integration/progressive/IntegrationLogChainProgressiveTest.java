package org.example.integration.progressive;

import org.example.math.LogSystem;
import org.example.math.Logarithm;
import org.example.math.SeriesLn;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class IntegrationLogChainProgressiveTest {
    private static final double EPS = 1e-12;
    private static final double ASSERT_EPS = 1e-6;

    @ParameterizedTest
    @CsvSource({
            "2.0",
            "3.5",
            "7.2"
    })
    void logarithmsWorkAfterReplacingMockedLnWithRealSeries(double x) {
        SeriesLn ln = new SeriesLn(EPS);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);

        assertEquals(Math.log(x) / Math.log(3.0), log3.value(x), ASSERT_EPS);
        assertEquals(Math.log10(x), log10.value(x), ASSERT_EPS);
    }

    @ParameterizedTest
    @CsvSource({
            "2.0",
            "3.5",
            "7.2"
    })
    void logSystemWorksAfterRealLogarithmsAreIntroduced(double x) {
        SeriesLn ln = new SeriesLn(EPS);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);

        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log10(x);
        double expected = Math.pow(Math.pow(log3Val * log10Val, 2.0) / log10Val, 2.0);

        assertEquals(expected, logSystem.value(x), ASSERT_EPS);
    }

    @Test
    void logSystemKeepsGuardWhenRealValuesReachBoundaryCase() {
        SeriesLn ln = new SeriesLn(EPS);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> logSystem.value(1.0));
        assertEquals("division by log10(x)", ex.getMessage());
    }
}

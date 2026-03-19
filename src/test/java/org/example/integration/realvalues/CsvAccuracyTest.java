package org.example.integration.realvalues;

import org.example.math.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
public class CsvAccuracyTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/sin_reference.csv", numLinesToSkip = 1)
    void sinSeriesMatchesReference(double x, double expected) {
        SeriesSin sin = new SeriesSin(1e-12);
        assertEquals(expected

                , sin.value(x), 1e-9);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ln_reference.csv", numLinesToSkip = 1)
    void lnSeriesMatchesReference(double x, double expected) {
        SeriesLn ln = new SeriesLn(1e-12);
        assertEquals(expected, ln.value(x), 1e-9);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/log3_reference.csv", numLinesToSkip = 1)
    void log3MatchesReference(double x, double expected) {
        SeriesLn ln = new SeriesLn(1e-12);
        Logarithm log3 = new Logarithm(ln, 3.0);
        assertEquals(expected, log3.value(x), 1e-9);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/log10_reference.csv", numLinesToSkip = 1)
    void log10MatchesReference(double x, double expected) {
        SeriesLn ln = new SeriesLn(1e-12);
        Logarithm log10 = new Logarithm(ln, 10.0);
        assertEquals(expected, log10.value(x), 1e-9);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/logsystem_reference.csv", numLinesToSkip = 1)
    void logSystemMatchesReference(double x, double expected) {
        double eps = 1e-10;
        SeriesLn ln = new SeriesLn(eps);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, eps);
        assertEquals(expected, logSystem.value(x), 1e-6);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/logsystem_reference.csv", numLinesToSkip = 1)
    void systemMatchesReferenceForPositiveX(double x, double expected) {
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

        assertEquals(expected, system.value(x), 1e-6);
    }
}

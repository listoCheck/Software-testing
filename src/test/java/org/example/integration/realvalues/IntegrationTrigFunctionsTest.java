package org.example.integration.realvalues;

import org.example.math.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
public class IntegrationTrigFunctionsTest {
    private static final double EPS = 1e-12;
    private static final double ASSERT_EPS = 1e-6;

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
    void sinSeriesMatchesMathSin(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        assertEquals(Math.sin(x), sin.value(x), ASSERT_EPS);
    }

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
    void cosMatchesMathCos(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);
        assertEquals(Math.cos(x), cos.value(x), ASSERT_EPS);
    }

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
    void tanMatchesMathTan(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);
        Tan tan = new Tan(sin, cos, EPS);
        assertEquals(Math.tan(x), tan.value(x), ASSERT_EPS);
    }

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
    void secMatchesMathSec(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);
        Sec sec = new Sec(cos, EPS);
        assertEquals(1.0 / Math.cos(x), sec.value(x), ASSERT_EPS);
    }

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
    void cscMatchesMathCsc(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Csc csc = new Csc(sin, EPS);
        assertEquals(1.0 / Math.sin(x), csc.value(x), ASSERT_EPS);
    }

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
    void cotMatchesMathCot(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);
        Cot cot = new Cot(sin, cos, EPS);
        assertEquals(1.0 / Math.tan(x), cot.value(x), ASSERT_EPS);
    }
}

package org.example.integration.progressive;

import org.example.math.Cos;
import org.example.math.Cot;
import org.example.math.Csc;
import org.example.math.Sec;
import org.example.math.SeriesSin;
import org.example.math.Tan;
import org.example.math.TrigSystem;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class IntegrationTrigChainProgressiveTest {
    private static final double EPS = 1e-12;
    private static final double ASSERT_EPS = 1e-6;

    @ParameterizedTest
    @CsvSource({
            "-2.3",
            "-1.3",
            "-0.8",
            "-0.5"
    })
    void cosWorksAfterReplacingMockedSinWithRealSeries(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);

        assertEquals(Math.cos(x), cos.value(x), ASSERT_EPS);
    }

    @ParameterizedTest
    @CsvSource({
            "-2.3",
            "-1.3",
            "-0.8",
            "-0.5"
    })
    void trigLeafFunctionsWorkAfterReplacingDependenciesWithRealOnes(double x) {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);
        Tan tan = new Tan(sin, cos, EPS);
        Cot cot = new Cot(sin, cos, EPS);
        Sec sec = new Sec(cos, EPS);
        Csc csc = new Csc(sin, EPS);

        assertEquals(Math.tan(x), tan.value(x), ASSERT_EPS);
        assertEquals(1.0 / Math.tan(x), cot.value(x), ASSERT_EPS);
        assertEquals(1.0 / Math.cos(x), sec.value(x), ASSERT_EPS);
        assertEquals(1.0 / Math.sin(x), csc.value(x), ASSERT_EPS);
    }

    @Test
    void trigSystemCanBeConnectedAfterRealTrigLeafChainIsReady() {
        SeriesSin sin = new SeriesSin(EPS);
        Cos cos = new Cos(sin);
        TrigSystem trigSystem = new TrigSystem(
                new Sec(cos, EPS),
                cos,
                new Csc(sin, EPS),
                new Tan(sin, cos, EPS),
                new Cot(sin, cos, EPS),
                EPS
        );

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(-1.0));
        assertEquals("division by zero in trig denominator", ex.getMessage());
    }
}

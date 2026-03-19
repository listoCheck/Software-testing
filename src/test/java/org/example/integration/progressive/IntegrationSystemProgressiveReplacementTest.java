package org.example.integration.progressive;

import org.example.integration.support.RecordingFunction;
import org.example.math.Cos;
import org.example.math.Cot;
import org.example.math.Csc;
import org.example.math.LogSystem;
import org.example.math.Logarithm;
import org.example.math.Sec;
import org.example.math.SeriesLn;
import org.example.math.SeriesSin;
import org.example.math.SystemFunction;
import org.example.math.Tan;
import org.example.math.TrigSystem;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class IntegrationSystemProgressiveReplacementTest {
    private static final double EPS = 1e-12;
    private static final double ASSERT_EPS = 1e-6;

    @Test
    void systemCanSwitchPositiveBranchToRealLogSubsystemWhileTrigIsStillMocked() {
        RecordingFunction trig = new RecordingFunction(x -> {
            throw new AssertionError("trig branch must stay isolated for x > 0");
        });

        SeriesLn ln = new SeriesLn(EPS);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);
        SystemFunction system = new SystemFunction(trig, logSystem);

        double x = 2.0;
        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log10(x);
        double expected = Math.pow(Math.pow(log3Val * log10Val, 2.0) / log10Val, 2.0);

        assertEquals(expected, system.value(x), ASSERT_EPS);
        assertEquals(0, trig.callCount());
    }

    @Test
    void systemCanSwitchNegativeBranchToRealTrigSubsystemWhileLogIsStillMocked() {
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
        RecordingFunction log = new RecordingFunction(x -> {
            throw new AssertionError("log branch must stay isolated for x <= 0");
        });

        SystemFunction system = new SystemFunction(trigSystem, log);

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> system.value(-1.0));
        assertEquals("division by zero in trig denominator", ex.getMessage());
        assertEquals(0, log.callCount());
    }

    @Test
    void systemWorksWhenBothSubsystemsBecomeReal() {
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

        SeriesLn ln = new SeriesLn(EPS);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);
        SystemFunction system = new SystemFunction(trigSystem, logSystem);

        double x = 2.0;
        double log3Val = Math.log(x) / Math.log(3.0);
        double log10Val = Math.log10(x);
        double expected = Math.pow(Math.pow(log3Val * log10Val, 2.0) / log10Val, 2.0);

        assertEquals(expected, system.value(x), ASSERT_EPS);
    }
}

package org.example.integration.mocks;

import org.example.math.Cos;
import org.example.math.Cot;
import org.example.math.Csc;
import org.example.math.Logarithm;
import org.example.math.Sec;
import org.example.math.Tan;
import org.example.integration.support.RecordingFunction;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class IntegrationLeafFunctionsWithMocksTest {
    private static final double EPS = 1e-10;

    @Test
    void cosUsesSinWithPiByTwoShift() {
        RecordingFunction sin = new RecordingFunction(x -> 0.42);
        Cos cos = new Cos(sin);

        double x = -1.25;
        assertEquals(0.42, cos.value(x), 1e-12);
        assertEquals(1, sin.callCount());
        assertEquals(x + Math.PI / 2.0, sin.calls().get(0), 1e-12);
    }

    @Test
    void tanUsesProvidedSinAndCos() {
        RecordingFunction sin = new RecordingFunction(x -> 6.0);
        RecordingFunction cos = new RecordingFunction(x -> 2.0);
        Tan tan = new Tan(sin, cos, EPS);

        double x = 0.77;
        assertEquals(3.0, tan.value(x), 1e-12);
        assertEquals(List.of(x), sin.calls());
        assertEquals(List.of(x), cos.calls());
    }

    @Test
    void tanFailsFastWhenCosIsZero() {
        RecordingFunction sin = new RecordingFunction(x -> 6.0);
        RecordingFunction cos = new RecordingFunction(x -> 0.0);
        Tan tan = new Tan(sin, cos, EPS);

        assertThrows(ArithmeticException.class, () -> tan.value(0.77));
        assertEquals(1, cos.callCount());
        assertEquals(0, sin.callCount());
    }

    @Test
    void cotUsesProvidedSinAndCos() {
        RecordingFunction sin = new RecordingFunction(x -> 2.0);
        RecordingFunction cos = new RecordingFunction(x -> 8.0);
        Cot cot = new Cot(sin, cos, EPS);

        double x = -0.33;
        assertEquals(4.0, cot.value(x), 1e-12);
        assertEquals(List.of(x), sin.calls());
        assertEquals(List.of(x), cos.calls());
    }

    @Test
    void secUsesProvidedCos() {
        RecordingFunction cos = new RecordingFunction(x -> 0.25);
        Sec sec = new Sec(cos, EPS);

        double x = -2.0;
        assertEquals(4.0, sec.value(x), 1e-12);
        assertEquals(List.of(x), cos.calls());
    }

    @Test
    void cscUsesProvidedSin() {
        RecordingFunction sin = new RecordingFunction(x -> -0.5);
        Csc csc = new Csc(sin, EPS);

        double x = 1.75;
        assertEquals(-2.0, csc.value(x), 1e-12);
        assertEquals(List.of(x), sin.calls());
    }

    @Test
    void logarithmUsesProvidedLnAndCachesLnBase() {
        RecordingFunction ln = new RecordingFunction(x -> x + 1.0);
        Logarithm log3 = new Logarithm(ln, 3.0);

        assertEquals(6.0 / 4.0, log3.value(5.0), 1e-12);
        assertEquals(List.of(3.0, 5.0), ln.calls());
    }
}

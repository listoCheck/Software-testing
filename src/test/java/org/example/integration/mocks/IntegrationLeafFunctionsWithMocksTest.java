package org.example.integration.mocks;

import org.example.math.Cos;
import org.example.math.Cot;
import org.example.math.Csc;
import org.example.math.Logarithm;
import org.example.math.MathFunction;
import org.example.math.Sec;
import org.example.math.Tan;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Tag("integration")
public class IntegrationLeafFunctionsWithMocksTest {
    private static final double EPS = 1e-10;

    @Test
    void cosUsesSinWithPiByTwoShift() {
        MathFunction sin = mock(MathFunction.class);
        Cos cos = new Cos(sin);

        double x = -1.25;
        when(sin.value(x + Math.PI / 2.0)).thenReturn(0.42);

        assertEquals(0.42, cos.value(x), 1e-12);
        verify(sin).value(x + Math.PI / 2.0);
        verifyNoMoreInteractions(sin);
    }

    @Test
    void tanUsesProvidedSinAndCos() {
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        Tan tan = new Tan(sin, cos, EPS);

        double x = 0.77;
        when(sin.value(x)).thenReturn(6.0);
        when(cos.value(x)).thenReturn(2.0);

        assertEquals(3.0, tan.value(x), 1e-12);
        verify(sin).value(x);
        verify(cos).value(x);
        verifyNoMoreInteractions(sin, cos);
    }

    @Test
    void tanFailsFastWhenCosIsZero() {
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        Tan tan = new Tan(sin, cos, EPS);

        when(cos.value(0.77)).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> tan.value(0.77));
        verify(cos).value(0.77);
        verify(sin, never()).value(0.77);
        verifyNoMoreInteractions(sin, cos);
    }

    @Test
    void cotUsesProvidedSinAndCos() {
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        Cot cot = new Cot(sin, cos, EPS);

        double x = -0.33;
        when(sin.value(x)).thenReturn(2.0);
        when(cos.value(x)).thenReturn(8.0);

        assertEquals(4.0, cot.value(x), 1e-12);
        verify(sin).value(x);
        verify(cos).value(x);
        verifyNoMoreInteractions(sin, cos);
    }

    @Test
    void secUsesProvidedCos() {
        MathFunction cos = mock(MathFunction.class);
        Sec sec = new Sec(cos, EPS);

        double x = -2.0;
        when(cos.value(x)).thenReturn(0.25);

        assertEquals(4.0, sec.value(x), 1e-12);
        verify(cos).value(x);
        verifyNoMoreInteractions(cos);
    }

    @Test
    void cscUsesProvidedSin() {
        MathFunction sin = mock(MathFunction.class);
        Csc csc = new Csc(sin, EPS);

        double x = 1.75;
        when(sin.value(x)).thenReturn(-0.5);

        assertEquals(-2.0, csc.value(x), 1e-12);
        verify(sin).value(x);
        verifyNoMoreInteractions(sin);
    }

    @Test
    void logarithmUsesProvidedLnAndCachesLnBase() {
        MathFunction ln = mock(MathFunction.class);
        when(ln.value(3.0)).thenReturn(4.0);
        when(ln.value(5.0)).thenReturn(6.0);

        Logarithm log3 = new Logarithm(ln, 3.0);

        assertEquals(6.0 / 4.0, log3.value(5.0), 1e-12);
        verify(ln).value(3.0);
        verify(ln).value(5.0);
        verifyNoMoreInteractions(ln);
    }
}

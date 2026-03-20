package org.example.integration.mocks;

import org.example.math.LogSystem;
import org.example.math.MathFunction;
import org.example.math.TrigSystem;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Tag("integration")
public class IntegrationSystemsWithMocksTest {
    private static final double EPS = 1e-10;

    @Test
    void logSystemUsesProvidedLogFunctions() {
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);

        double x = 2.0;
        when(log3.value(x)).thenReturn(4.0);
        when(log10.value(x)).thenReturn(2.0);

        assertEquals(1024.0, logSystem.value(x), 1e-12);
        verify(log3).value(x);
        verify(log10).value(x);
        verifyNoMoreInteractions(log3, log10);
    }

    @Test
    void logSystemThrowsWhenLog10IsZero() {
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);

        when(log3.value(2.0)).thenReturn(4.0);
        when(log10.value(2.0)).thenReturn(0.0);

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> logSystem.value(2.0));
        assertEquals("division by log10(x)", ex.getMessage());
        verify(log3).value(2.0);
        verify(log10).value(2.0);
        verifyNoMoreInteractions(log3, log10);
    }

    @Test
    void trigSystemCallsAllDependenciesAndFailsOnZeroDenominator() {
        MathFunction sec = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction csc = mock(MathFunction.class);
        MathFunction tan = mock(MathFunction.class);
        MathFunction cot = mock(MathFunction.class);

        when(sec.value(-1.0)).thenReturn(2.0);
        when(cos.value(-1.0)).thenReturn(0.5);
        when(csc.value(-1.0)).thenReturn(4.0);
        when(tan.value(-1.0)).thenReturn(3.0);
        when(cot.value(-1.0)).thenReturn(0.25);

        TrigSystem trigSystem = new TrigSystem(sec, cos, csc, tan, cot, EPS);

        double x = -1.0;
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(x));
        assertEquals("division by zero in trig denominator", ex.getMessage());
        verify(sec).value(x);
        verify(cos).value(x);
        verify(csc).value(x);
        verify(tan).value(x);
        verify(cot).value(x);
        verifyNoMoreInteractions(sec, cos, csc, tan, cot);
    }

    @Test
    void trigSystemFailsFastWhenTanIsZero() {
        MathFunction sec = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction csc = mock(MathFunction.class);
        MathFunction tan = mock(MathFunction.class);
        MathFunction cot = mock(MathFunction.class);

        when(sec.value(-1.0)).thenReturn(2.0);
        when(cos.value(-1.0)).thenReturn(0.5);
        when(csc.value(-1.0)).thenReturn(4.0);
        when(tan.value(-1.0)).thenReturn(0.0);
        when(cot.value(-1.0)).thenReturn(0.25);

        TrigSystem trigSystem = new TrigSystem(sec, cos, csc, tan, cot, EPS);

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(-1.0));
        assertEquals("division by tan(x)", ex.getMessage());
        verify(sec).value(-1.0);
        verify(cos).value(-1.0);
        verify(csc).value(-1.0);
        verify(tan).value(-1.0);
        verify(cot).value(-1.0);
        verifyNoMoreInteractions(sec, cos, csc, tan, cot);
    }
}

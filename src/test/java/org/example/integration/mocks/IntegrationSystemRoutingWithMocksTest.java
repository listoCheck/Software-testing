package org.example.integration.mocks;

import org.example.math.MathFunction;
import org.example.math.SystemFunction;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Tag("integration")
public class IntegrationSystemRoutingWithMocksTest {
    @Test
    void routesToTrigForNegativeX() {
        MathFunction trig = mock(MathFunction.class);
        MathFunction log = mock(MathFunction.class);
        when(trig.value(-1.0)).thenReturn(-100.0);

        SystemFunction system = new SystemFunction(trig, log);

        assertEquals(-100.0, system.value(-1.0), 1e-12);
        verify(trig).value(-1.0);
        verify(log, never()).value(-1.0);
        verifyNoMoreInteractions(trig, log);
    }

    @Test
    void routesToTrigAtBoundaryXZero() {
        MathFunction trig = mock(MathFunction.class);
        MathFunction log = mock(MathFunction.class);
        when(trig.value(0.0)).thenReturn(-50.0);

        SystemFunction system = new SystemFunction(trig, log);

        assertEquals(-50.0, system.value(0.0), 1e-12);
        verify(trig).value(0.0);
        verify(log, never()).value(0.0);
        verifyNoMoreInteractions(trig, log);
    }

    @Test
    void routesToLogForPositiveX() {
        MathFunction trig = mock(MathFunction.class);
        MathFunction log = mock(MathFunction.class);
        when(log.value(2.0)).thenReturn(200.0);

        SystemFunction system = new SystemFunction(trig, log);

        assertEquals(200.0, system.value(2.0), 1e-12);
        verify(trig, never()).value(2.0);
        verify(log).value(2.0);
        verifyNoMoreInteractions(trig, log);
    }
}

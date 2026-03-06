package org.example.integration.mocks;

import org.example.math.LogSystem;
import org.example.math.MathFunction;
import org.example.math.TrigSystem;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class IntegrationSubsystemsWithMocksTest {
    private static final double EPS = 1e-10;

    @Test
    void logSystemUsesProvidedLogFunctions() {
        RecordingFunction log3 = new RecordingFunction(x -> 4.0);
        RecordingFunction log10 = new RecordingFunction(x -> 2.0);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);

        double x = 2.0;
        assertEquals(1024.0, logSystem.value(x), 1e-12);
        assertEquals(List.of(x), log3.calls());
        assertEquals(List.of(x), log10.calls());
    }

    @Test
    void logSystemThrowsWhenLog10IsZero() {
        RecordingFunction log3 = new RecordingFunction(x -> 4.0);
        RecordingFunction log10 = new RecordingFunction(x -> 0.0);
        LogSystem logSystem = new LogSystem(log3, log10, EPS);

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> logSystem.value(2.0));
        assertEquals("division by log10(x)", ex.getMessage());
        assertEquals(1, log3.callCount());
        assertEquals(1, log10.callCount());
    }

    @Test
    void trigSystemCallsAllDependenciesAndFailsOnZeroDenominator() {
        RecordingFunction sec = new RecordingFunction(x -> 2.0);
        RecordingFunction cos = new RecordingFunction(x -> 0.5);
        RecordingFunction csc = new RecordingFunction(x -> 4.0);
        RecordingFunction tan = new RecordingFunction(x -> 3.0);
        RecordingFunction cot = new RecordingFunction(x -> 0.25);

        TrigSystem trigSystem = new TrigSystem(sec, cos, csc, tan, cot, EPS);

        double x = -1.0;
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(x));
        assertEquals("division by zero in trig denominator", ex.getMessage());
        assertEquals(List.of(x), sec.calls());
        assertEquals(List.of(x), cos.calls());
        assertEquals(List.of(x), csc.calls());
        assertEquals(List.of(x), tan.calls());
        assertEquals(List.of(x), cot.calls());
    }

    @Test
    void trigSystemFailsFastWhenTanIsZero() {
        RecordingFunction sec = new RecordingFunction(x -> 2.0);
        RecordingFunction cos = new RecordingFunction(x -> 0.5);
        RecordingFunction csc = new RecordingFunction(x -> 4.0);
        RecordingFunction tan = new RecordingFunction(x -> 0.0);
        RecordingFunction cot = new RecordingFunction(x -> 0.25);

        TrigSystem trigSystem = new TrigSystem(sec, cos, csc, tan, cot, EPS);

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> trigSystem.value(-1.0));
        assertEquals("division by tan(x)", ex.getMessage());
        assertEquals(1, sec.callCount());
        assertEquals(1, cos.callCount());
        assertEquals(1, csc.callCount());
        assertEquals(1, tan.callCount());
        assertEquals(1, cot.callCount());
    }

    private static final class RecordingFunction implements MathFunction {
        private final DoubleUnaryOperator delegate;
        private final List<Double> calls = new ArrayList<>();

        private RecordingFunction(DoubleUnaryOperator delegate) {
            this.delegate = delegate;
        }

        @Override
        public double value(double x) {
            calls.add(x);
            return delegate.applyAsDouble(x);
        }

        private int callCount() {
            return calls.size();
        }

        private List<Double> calls() {
            return calls;
        }
    }
}

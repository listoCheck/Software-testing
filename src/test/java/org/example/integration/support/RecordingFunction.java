package org.example.integration.support;

import org.example.math.MathFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public final class RecordingFunction implements MathFunction {
    private final DoubleUnaryOperator delegate;
    private final List<Double> calls = new ArrayList<>();

    public RecordingFunction(DoubleUnaryOperator delegate) {
        this.delegate = delegate;
    }

    @Override
    public double value(double x) {
        calls.add(x);
        return delegate.applyAsDouble(x);
    }

    public int callCount() {
        return calls.size();
    }

    public List<Double> calls() {
        return calls;
    }
}

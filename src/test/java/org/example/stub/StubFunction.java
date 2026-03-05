package org.example.stub;

import org.example.math.MathFunction;

import java.util.Map;

public class StubFunction implements MathFunction {
    private final Map<Double, Double> table;
    private final double tolerance;

    public StubFunction(Map<Double, Double> table, double tolerance) {
        this.table = table;
        this.tolerance = tolerance;
    }

    @Override
    public double value(double x) {
        for (Map.Entry<Double, Double> entry : table.entrySet()) {
            if (Math.abs(entry.getKey() - x) <= tolerance) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("No stub value for x=" + x);
    }
}

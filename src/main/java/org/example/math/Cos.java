package org.example.math;

public class Cos implements MathFunction {
    private final MathFunction sin;

    public Cos(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public double value(double x) {
        return sin.value(x + Math.PI / 2.0);
    }
}

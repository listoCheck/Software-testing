package org.example.math;

public class Csc implements MathFunction {
    private final MathFunction sin;
    private final double epsilon;

    public Csc(MathFunction sin, double epsilon) {
        this.sin = sin;
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double s = sin.value(x);
        if (Math.abs(s) < epsilon) {
            throw new ArithmeticException("csc(x) undefined for sin(x)=0");
        }
        return 1.0 / s;
    }
}

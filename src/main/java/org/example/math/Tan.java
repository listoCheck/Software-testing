package org.example.math;

public class Tan implements MathFunction {
    private final MathFunction sin;
    private final MathFunction cos;
    private final double epsilon;

    public Tan(MathFunction sin, MathFunction cos, double epsilon) {
        this.sin = sin;
        this.cos = cos;
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double c = cos.value(x);
        if (Math.abs(c) < epsilon) {
            throw new ArithmeticException("tan(x) undefined for cos(x)=0");
        }
        return sin.value(x) / c;
    }
}

package org.example.math;

public class Cot implements MathFunction {
    private final MathFunction sin;
    private final MathFunction cos;
    private final double epsilon;

    public Cot(MathFunction sin, MathFunction cos, double epsilon) {
        this.sin = sin;
        this.cos = cos;
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double s = sin.value(x);
        if (Math.abs(s) < epsilon) {
            throw new ArithmeticException("cot(x) undefined for sin(x)=0");
        }
        return cos.value(x) / s;
    }
}

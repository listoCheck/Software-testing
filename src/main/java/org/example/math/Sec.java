package org.example.math;

public class Sec implements MathFunction {
    private final MathFunction cos;
    private final double epsilon;

    public Sec(MathFunction cos, double epsilon) {
        this.cos = cos;
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double c = cos.value(x);
        if (Math.abs(c) < epsilon) {
            throw new ArithmeticException("sec(x) undefined for cos(x)=0");
        }
        return 1.0 / c;
    }
}

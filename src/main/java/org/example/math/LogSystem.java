package org.example.math;

public class LogSystem implements MathFunction {
    private final MathFunction log3;
    private final MathFunction log10;
    private final double epsilon;

    public LogSystem(MathFunction log3, MathFunction log10, double epsilon) {
        this.log3 = log3;
        this.log10 = log10;
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double log3Val = log3.value(x);
        double log10Val = log10.value(x);

        if (Math.abs(log10Val) < epsilon) {
            throw new ArithmeticException("division by log10(x)");
        }

        double a = log3Val - log3Val;
        double b = log3Val * log10Val;
        double c = a + b;
        double d = Math.pow(c, 2.0);
        double e = d / log10Val;
        double f = Math.pow(e, 2.0);
        return f;
    }
}

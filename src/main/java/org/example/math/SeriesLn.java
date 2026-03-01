package org.example.math;

public class SeriesLn implements MathFunction {
    private final double epsilon;

    public SeriesLn(double epsilon) {
        if (epsilon <= 0) {
            throw new IllegalArgumentException("epsilon must be > 0");
        }
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) is defined for x > 0");
        }
        double y = (x - 1.0) / (x + 1.0);
        double y2 = y * y;
        double term = y;
        double sum = 0.0;
        int n = 0;
        while (Math.abs(term) > epsilon) {
            sum += term / (2.0 * n + 1.0);
            term *= y2;
            n++;
            if (n > 1_000_000) {
                break;
            }
        }
        return 2.0 * sum;
    }
}

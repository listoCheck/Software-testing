package org.example.math;

public class SeriesSin implements MathFunction {
    private final double epsilon;

    public SeriesSin(double epsilon) {
        if (epsilon <= 0) {
            throw new IllegalArgumentException("epsilon must be > 0");
        }
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double y = normalize(x);
        double term = y;
        double sum = term;
        int n = 1;
        while (Math.abs(term) > epsilon) {
            term *= -1.0 * y * y / ((2.0 * n) * (2.0 * n + 1.0));
            sum += term;
            n++;
            if (n > 10_000) {
                break;
            }
        }
        return sum;
    }

    private double normalize(double x) {
        double twoPi = 2.0 * Math.PI;
        double y = x % twoPi;
        if (y > Math.PI) {
            y -= twoPi;
        } else if (y < -Math.PI) {
            y += twoPi;
        }
        return y;
    }
}

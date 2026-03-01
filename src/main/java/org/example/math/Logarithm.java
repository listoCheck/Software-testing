package org.example.math;

public class Logarithm implements MathFunction {
    private final MathFunction ln;
    private final double base;
    private final double lnBase;

    public Logarithm(MathFunction ln, double base) {
        if (base <= 0 || base == 1.0) {
            throw new IllegalArgumentException("base must be > 0 and != 1");
        }
        this.ln = ln;
        this.base = base;
        this.lnBase = ln.value(base);
    }

    @Override
    public double value(double x) {
        return ln.value(x) / lnBase;
    }
}

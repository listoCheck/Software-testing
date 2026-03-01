package org.example.math;

public class SystemFunction implements MathFunction {
    private final MathFunction trigSystem;
    private final MathFunction logSystem;

    public SystemFunction(MathFunction trigSystem, MathFunction logSystem) {
        this.trigSystem = trigSystem;
        this.logSystem = logSystem;
    }

    @Override
    public double value(double x) {
        if (x <= 0) {
            return trigSystem.value(x);
        }
        return logSystem.value(x);
    }
}

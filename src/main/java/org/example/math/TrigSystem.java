package org.example.math;

public class TrigSystem implements MathFunction {
    private final MathFunction sec;
    private final MathFunction cos;
    private final MathFunction csc;
    private final MathFunction tan;
    private final MathFunction cot;
    private final double epsilon;

    public TrigSystem(MathFunction sec, MathFunction cos, MathFunction csc, MathFunction tan, MathFunction cot, double epsilon) {
        this.sec = sec;
        this.cos = cos;
        this.csc = csc;
        this.tan = tan;
        this.cot = cot;
        this.epsilon = epsilon;
    }

    @Override
    public double value(double x) {
        double secVal = sec.value(x);
        double cosVal = cos.value(x);
        double cscVal = csc.value(x);
        double tanVal = tan.value(x);
        double cotVal = cot.value(x);

        if (Math.abs(tanVal) < epsilon) {
            throw new ArithmeticException("division by tan(x)");
        }

        double a = secVal / cosVal;
        double b = a * cosVal;
        double c = b * cscVal;
        double d = Math.pow(c, 3.0);
        double e = Math.pow(d, 2.0);
        double f = e / tanVal;

        double g = Math.pow(cotVal, 3.0);
        double h = g * cscVal;
        double i = cosVal - cosVal;
        double j = i * cotVal;
        double k = h * j;
        double l = k / tanVal;

        if (Math.abs(l) < epsilon) {
            throw new ArithmeticException("division by zero in trig denominator");
        }

        return f / l;
    }
}

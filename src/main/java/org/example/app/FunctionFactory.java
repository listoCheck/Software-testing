package org.example.app;

import org.example.math.*;

public class FunctionFactory {
    private final double epsilon;

    public FunctionFactory(double epsilon) {
        this.epsilon = epsilon;
    }

    public MathFunction build(String name) {
        SeriesSin sin = new SeriesSin(epsilon);
        Cos cos = new Cos(sin);
        Tan tan = new Tan(sin, cos, epsilon);
        Cot cot = new Cot(sin, cos, epsilon);
        Sec sec = new Sec(cos, epsilon);
        Csc csc = new Csc(sin, epsilon);

        SeriesLn ln = new SeriesLn(epsilon);
        Logarithm log3 = new Logarithm(ln, 3.0);
        Logarithm log10 = new Logarithm(ln, 10.0);

        TrigSystem trigSystem = new TrigSystem(sec, cos, csc, tan, cot, epsilon);
        LogSystem logSystem = new LogSystem(log3, log10, epsilon);
        SystemFunction system = new SystemFunction(trigSystem, logSystem);

        return switch (name.toLowerCase()) {
            case "sin" -> sin;
            case "cos" -> cos;
            case "tan" -> tan;
            case "cot" -> cot;
            case "sec" -> sec;
            case "csc" -> csc;
            case "ln" -> ln;
            case "log3" -> log3;
            case "log10" -> log10;
            case "trig" -> trigSystem;
            case "log" -> logSystem;
            case "system" -> system;
            default -> throw new IllegalArgumentException("Unknown function: " + name);
        };
    }
}

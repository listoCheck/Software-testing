package org.example;

import org.example.app.FunctionFactory;
import org.example.io.CsvExporter;
import org.example.math.MathFunction;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 6) {
            printUsage();
            return;
        }

        String functionName = args[0];
        double from = Double.parseDouble(args[1]);
        double to = Double.parseDouble(args[2]);
        double step = Double.parseDouble(args[3]);
        double epsilon = Double.parseDouble(args[4]);
        Path out = Path.of(args[5]);
        String separator = args.length >= 7 ? args[6] : ";";

        FunctionFactory factory = new FunctionFactory(epsilon);
        MathFunction function = factory.build(functionName);
        new CsvExporter().export(function, from, to, step, out, separator);
    }

    private static void printUsage() {
        System.out.println("Usage: <function> <from> <to> <step> <epsilon> <out> [separator]");
        System.out.println("Functions: sin, cos, tan, cot, sec, csc, ln, log3, log10, trig, log, system");
    }
}

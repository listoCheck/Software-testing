package org.example.io;

import org.example.math.MathFunction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvExporter {
    public void export(MathFunction function, double from, double to, double step, Path out, String separator) throws IOException {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be > 0");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            writer.write("X");
            writer.write(separator);
            writer.write("Result(X)");
            writer.newLine();
            double x = from;
            if (from > to) {
                for (; x >= to; x -= step) {
                    writeLine(writer, x, function, separator);
                }
            } else {
                for (; x <= to; x += step) {
                    writeLine(writer, x, function, separator);
                }
            }
        }
    }

    private void writeLine(BufferedWriter writer, double x, MathFunction function, String separator) throws IOException {
        double y;
        try {
            y = function.value(x);
        } catch (RuntimeException ex) {
            y = Double.NaN;
        }
        writer.write(Double.toString(x));
        writer.write(separator);
        writer.write(Double.toString(y));
        writer.newLine();
    }
}

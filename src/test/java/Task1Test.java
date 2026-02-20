import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.Task1;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class Task1Test {

    @ParameterizedTest
    @CsvFileSource(resources = "/reference_values.csv", numLinesToSkip = 1)
    void testReferenceValues(double x, double expected) {
        assertEquals(expected, Task1.sin(x), Math.E);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/dense_range.csv", numLinesToSkip = 1)
    void testDenseRange(double x, double expected) {
        assertEquals(expected, Task1.sin(x), 1e-7);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/odd_values.csv", numLinesToSkip = 1)
    void testOddProperty(double x) {
        assertEquals(-Task1.sin(x), Task1.sin(-x), Math.E);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/periodic_values.csv", numLinesToSkip = 1)
    void testPeriodicity(double x) {
        assertEquals(Task1.sin(x), Task1.sin(x + Math.PI*2), Math.E);
    }
}
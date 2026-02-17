import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.Task1;
import org.junit.jupiter.api.Test;

public class Task1Test {

    private static final double EPS = 1e-6;

    @Test
    void testZero() {
        assertEquals(0.0, Task1.sin(0), EPS);
    }

    @Test
    void testSmallValues() {
        double x = 0.001;
        assertEquals(Math.sin(x), Task1.sin(x), EPS);
    }

    @Test
    void testTypicalValues() {
        double[] values = {Math.PI / 6, Math.PI / 4, Math.PI / 2, Math.PI};

        for (double x : values) {
            assertEquals(Math.sin(x), Task1.sin(x), 1e-9);
        }
    }

    @Test
    void testNegativeValues() {
        double x = 1.3;
        assertEquals(-Task1.sin(x), Task1.sin(-x), EPS);
    }

    @Test
    void testLargeValues() {
        double x = 20;
        assertEquals(Math.sin(x), Task1.sin(x), EPS);
    }

}

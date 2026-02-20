import org.example.CountingSort;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CountingSortGiantTest {

    @Test
    void sortsGiganticArrayAndPreservesFrequencies() {
        final int N = 1_000_000;
        final int K = 1000;

        int[] in = new int[N];
        int[] freqIn = new int[K + 1];
        Random rnd = new Random(42L);
        for (int i = 0; i < N; i++) {
            int v = rnd.nextInt(K + 1);
            in[i] = v;
            freqIn[v]++;
        }

        int[] out = CountingSort.sort(in);

        for (int i = 1; i < out.length; i++) {
            if (out[i - 1] > out[i]) {
                fail("Array is not sorted at index " + i + ": " + out[i - 1] + ">" + out[i]);
            }
        }

        int[] freqOut = new int[K + 1];
        for (int v : out) {
            assertTrue(v >= 0 && v <= K, "Value out of expected range: " + v);
            freqOut[v]++;
        }
        for (int v = 0; v <= K; v++) {
            assertEquals(freqIn[v], freqOut[v], "Mismatched frequency for value=" + v);
        }
    }
}


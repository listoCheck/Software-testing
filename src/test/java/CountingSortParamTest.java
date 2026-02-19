import org.example.CountingSort;
import org.example.CountingSort.CP;
import org.example.CountingSort.TraceResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CountingSortParamTest {

    static Stream<int[]> arrays() {
        return Stream.of(
                new int[]{0},
                new int[]{0,0,0},
                new int[]{1,0,2,0,3,1,2},
                new int[]{3,3,2,2,1,1,0,0},
                new int[]{5,2,5,2,5,2},
                new int[]{9,8,7,6,5,4,3,2,1,0},
                new int[]{3,1,0},
                new int[]{0,1000,1,999,2,998}
        );
    }

    private static long count(List<CP> trace, CP p) {
        return trace.stream().filter(e -> e == p).count();
    }

    @ParameterizedTest
    @MethodSource("arrays")
    void resultMatchesJavaSort(int[] input) {
        int[] expected = Arrays.stream(input).sorted().toArray();
        assertArrayEquals(expected, CountingSort.sort(Arrays.copyOf(input, input.length)));
    }

    @ParameterizedTest
    @MethodSource("arrays")
    void traceHasCorrectItemCountsAndBoundaries(int[] input) {
        TraceResult r = CountingSort.sortWithTrace(Arrays.copyOf(input, input.length));
        List<CP> t = r.trace;
        assertEquals(input.length, count(t, CP.COUNT_ITEM));
        assertEquals(input.length, count(t, CP.PLACE_ITEM));
        assertEquals(CP.ENTRY, t.get(0));
        assertEquals(CP.EXIT, t.get(t.size()-1));
    }

    @ParameterizedTest
    @MethodSource("arrays")
    void prefixItemsEqualMaxForMaxGreaterZero(int[] input) {
        int max = Arrays.stream(input).max().orElse(0);
        int expected = Math.max(0, max); // prefix loop runs for i=1..max
        TraceResult r = CountingSort.sortWithTrace(Arrays.copyOf(input, input.length));
        assertEquals(expected, count(r.trace, CP.PREFIX_ITEM));
    }

    @ParameterizedTest
    @MethodSource("arrays")
    void newMaxEventsMatchFirstTimeExceedances(int[] input) {
        int cur = 0;
        int expected = 0;
        for (int v : input) {
            if (v > cur) { expected++; cur = v; }
        }
        TraceResult r = CountingSort.sortWithTrace(Arrays.copyOf(input, input.length));
        assertEquals(expected, count(r.trace, CP.NEW_MAX));
    }
}


package task2;

import org.example.Task2;
import org.example.Task2.CP;
import org.example.Task2.TraceResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CountingSortTest {

    private static long count(List<CP> trace, CP point) {
        return trace.stream().filter(p -> p == point).count();
    }

    @Test
    void nullInput_returnsEmptyAndMinimalTrace() {
        TraceResult r = Task2.sortWithTrace(null);
        assertArrayEquals(new int[]{}, r.sorted);
        assertEquals(List.of(CP.ENTRY, CP.EMPTY_RETURN, CP.EXIT), r.trace);
    }

    @Test
    void allEqualElements_sortedAndTraceCountsMatch() {
        TraceResult r = Task2.sortWithTrace(new int[]{5, 5, 5, 5});
        assertArrayEquals(new int[]{5, 5, 5, 5}, r.sorted);
        assertEquals(4, count(r.trace, CP.COUNT_ITEM));
        assertEquals(4, count(r.trace, CP.PLACE_ITEM));
        // max==5 -> prefix loop runs 5 times
        assertEquals(5, count(r.trace, CP.PREFIX_ITEM));
    }

    @Test
    void traceStructure_hasExpectedPhasesAndCounts() {
        int[] in = {3, 0, 3, 1, 0}; // n=5, max=3
        TraceResult r = Task2.sortWithTrace(in);
        assertArrayEquals(new int[]{0, 0, 1, 3, 3}, r.sorted);
        // Phases appear in order
        var t = r.trace;
        assertTrue(t.indexOf(CP.SCAN_START) < t.indexOf(CP.ALLOCATE_COUNTS));
        assertTrue(t.indexOf(CP.ALLOCATE_COUNTS) < t.indexOf(CP.COUNTING_START));
        assertTrue(t.indexOf(CP.COUNTING_START) < t.indexOf(CP.PREFIX_SUM_START));
        assertTrue(t.indexOf(CP.PREFIX_SUM_START) < t.indexOf(CP.PLACE_START));
        assertTrue(t.get(0) == CP.ENTRY && t.get(t.size()-1) == CP.EXIT);
        // Item-level counts
        assertEquals(5, count(t, CP.COUNT_ITEM));
        assertEquals(5, count(t, CP.PLACE_ITEM));
        assertEquals(3, count(t, CP.PREFIX_ITEM)); // 1..max
    }

    @Test
    void alreadySortedAndReverseSorted() {
        assertArrayEquals(new int[]{0,1,2,3}, Task2.sort(new int[]{0,1,2,3}));
        assertArrayEquals(new int[]{0,1,2,3,4,5}, Task2.sort(new int[]{5,4,3,2,1,0}));
    }

    @Test
    void zerosOnlyAndZerosMixed() {
        assertArrayEquals(new int[]{0,0,0}, Task2.sort(new int[]{0,0,0}));
        assertArrayEquals(new int[]{0,0,2,3}, Task2.sort(new int[]{3,0,2,0}));
    }

    @Test
    void outlierLargeGapAndBigKSmallN() {
        assertArrayEquals(new int[]{1,3,5,30}, Task2.sort(new int[]{30,1,5,3}));
        assertArrayEquals(new int[]{0,1000}, Task2.sort(new int[]{1000,0}));
    }
}

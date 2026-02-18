import org.example.CountingSort;
import org.example.CountingSort.CP;
import org.example.CountingSort.TraceResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CountingSortTest {

    @Test
    void emptyArray_hasMinimalTrace() {
        TraceResult r = CountingSort.sortWithTrace(new int[]{});
        assertArrayEquals(new int[]{}, r.sorted);
        assertEquals(List.of(CP.ENTRY, CP.EMPTY_RETURN, CP.EXIT), r.trace);
    }

    @Test
    void singleElement_recordsExpectedTrace() {
        TraceResult r = CountingSort.sortWithTrace(new int[]{3});
        assertArrayEquals(new int[]{3}, r.sorted);
        assertEquals(
                List.of(
                        CP.ENTRY,
                        CP.SCAN_START,
                        CP.SCAN_ITEM,
                        CP.NEW_MAX,
                        CP.ALLOCATE_COUNTS,
                        CP.COUNTING_START,
                        CP.COUNT_ITEM,
                        CP.PREFIX_SUM_START,
                        CP.PREFIX_ITEM,
                        CP.PREFIX_ITEM,
                        CP.PREFIX_ITEM,
                        CP.PLACE_START,
                        CP.PLACE_ITEM,
                        CP.EXIT
                ), r.trace
        );
    }

    @Test
    void typicalArray_fullTraceMatches() {
        int[] input = {2, 0, 2, 1};
        TraceResult r = CountingSort.sortWithTrace(input);
        assertArrayEquals(new int[]{0,1,2,2}, r.sorted);

        assertEquals(
                List.of(
                        CP.ENTRY,
                        CP.SCAN_START,
                        CP.SCAN_ITEM, // 2
                        CP.NEW_MAX,
                        CP.SCAN_ITEM, // 0
                        CP.SCAN_ITEM, // 2
                        CP.SCAN_ITEM, // 1
                        CP.ALLOCATE_COUNTS,
                        CP.COUNTING_START,
                        CP.COUNT_ITEM,
                        CP.COUNT_ITEM,
                        CP.COUNT_ITEM,
                        CP.COUNT_ITEM,
                        CP.PREFIX_SUM_START,
                        CP.PREFIX_ITEM, // i=1
                        CP.PREFIX_ITEM, // i=2
                        CP.PLACE_START,
                        CP.PLACE_ITEM, // 1
                        CP.PLACE_ITEM, // 2
                        CP.PLACE_ITEM, // 0
                        CP.PLACE_ITEM, // 2
                        CP.EXIT
                ), r.trace
        );
    }

    @Test
    void negativeValues_throw() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> CountingSort.sortWithTrace(new int[]{1, -1, 2}));
        assertTrue(ex.getMessage().toLowerCase().contains("non-negative"));
    }
}

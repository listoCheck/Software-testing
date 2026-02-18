package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountingSort {

    private CountingSort() {}

    public enum CP {
        ENTRY,
        EMPTY_RETURN,
        SCAN_START,
        SCAN_ITEM,
        NEW_MAX,
        ERROR_NEGATIVE,
        ALLOCATE_COUNTS,
        COUNTING_START,
        COUNT_ITEM,
        PREFIX_SUM_START,
        PREFIX_ITEM,
        PLACE_START,
        PLACE_ITEM,
        EXIT
    }

    public static final class TraceResult {
        public final int[] sorted;
        public final List<CP> trace;

        public TraceResult(int[] sorted, List<CP> trace) {
            this.sorted = sorted;
            this.trace = trace;
        }

        @Override
        public String toString() {
            String ln = System.lineSeparator();
            StringBuilder sb = new StringBuilder();
            sb.append("sorted=").append(Arrays.toString(sorted)).append(ln);
            sb.append("trace:");
            for (int i = 0; i < trace.size(); i++) {
                sb.append(ln).append(i).append(": ").append(trace.get(i));
            }
            return sb.toString();
        }
    }

    public static TraceResult sortWithTrace(int[] input) {
        List<CP> trace = new ArrayList<>();
        trace.add(CP.ENTRY);

        if (input == null || input.length == 0) {
            trace.add(CP.EMPTY_RETURN);
            trace.add(CP.EXIT);
            return new TraceResult(new int[0], Collections.unmodifiableList(trace));
        }

        int max = 0;
        trace.add(CP.SCAN_START);
        for (int v : input) {
            trace.add(CP.SCAN_ITEM);
            if (v < 0) {
                trace.add(CP.ERROR_NEGATIVE);
                throw new IllegalArgumentException("Counting sort supports only non-negative integers");
            }
            if (v > max) {
                max = v;
                trace.add(CP.NEW_MAX);
            }
        }

        trace.add(CP.ALLOCATE_COUNTS);
        int[] counts = new int[max + 1];

        trace.add(CP.COUNTING_START);
        for (int v : input) {
            trace.add(CP.COUNT_ITEM);
            counts[v]++;
        }

        trace.add(CP.PREFIX_SUM_START);
        for (int i = 1; i < counts.length; i++) {
            trace.add(CP.PREFIX_ITEM);
            counts[i] += counts[i - 1];
        }

        trace.add(CP.PLACE_START);
        int[] output = new int[input.length];
        for (int i = input.length - 1; i >= 0; i--) {
            trace.add(CP.PLACE_ITEM);
            int v = input[i];
            output[--counts[v]] = v;
        }

        trace.add(CP.EXIT);
        return new TraceResult(output, Collections.unmodifiableList(trace));
    }
}


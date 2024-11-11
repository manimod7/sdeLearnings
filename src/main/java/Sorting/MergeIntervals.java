package Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        // Sort intervals by their start times
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();

        // Start with the first interval
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);

        for (int[] interval : intervals) {
            int currentEnd = currentInterval[1];

            if (interval[0] <= currentEnd) { // Overlapping intervals, so merge
                currentInterval[1] = Math.max(currentEnd, interval[1]);
            } else { // No overlap, add the current interval to the list
                currentInterval = interval;
                merged.add(currentInterval);
            }
        }
        HashMap<Integer,Integer> m = new HashMap<>();
        int x=5;

        // Convert merged list to an array and return
        return merged.toArray(new int[merged.size()][]);
    }
}

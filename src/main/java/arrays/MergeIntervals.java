package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.List;

public class MergeIntervals {
    public List<ArrayList<Integer>> insert(List<ArrayList<Integer>> intervals, ArrayList<Integer> newInterval) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        int i = 0;
        int start = newInterval.get(0);
        int end = newInterval.get(1);
        // Add all intervals ending before newInterval starts
        while (i < intervals.size() && intervals.get(i).get(1) < start) {
            result.add(intervals.get(i++));
        }
        // Merge all overlapping intervals to one considering newInterval
        while (i < intervals.size() && intervals.get(i).get(0) <= end) {
            start = Math.min(start, intervals.get(i).get(0));
            end = Math.max(end, intervals.get(i).get(1));
            i++;
        }
        result.add(new ArrayList<>(Arrays.asList(start, end)));
        // Add the rest
        while (i < intervals.size()) {
            result.add(intervals.get(i++));
        }
        return result;
    }

}

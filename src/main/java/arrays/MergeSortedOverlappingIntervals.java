package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortedOverlappingIntervals {
    public List<ArrayList<Integer>> solve(List<ArrayList<Integer>> nums) {
        // Early return if the input list is empty
        if (nums.isEmpty()) return nums;
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>(nums.size());
        int currStart = nums.get(0).get(0), currEnd = nums.get(0).get(1);
        for (int i = 1; i < nums.size(); i++) {
            int newStart = nums.get(i).get(0);
            int newEnd = nums.get(i).get(1);
            if (currEnd >= newStart) {
                // Overlapping intervals, extend the current interval
                currEnd = Math.max(currEnd, newEnd);
            } else {
                // Non-overlapping interval, add the current interval to the result
                ans.add(new ArrayList<>(Arrays.asList(currStart, currEnd)));
                currStart = newStart; // Start of a new current interval
                currEnd = newEnd; // End of a new current interval
            }
        }
        // Add the last interval
        ans.add(new ArrayList<>(Arrays.asList(currStart, currEnd)));
        return ans;
    }

}

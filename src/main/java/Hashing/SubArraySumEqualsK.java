package Hashing;

import java.util.ArrayList;
import java.util.HashMap;

public class SubArraySumEqualsK {
    public int solve(ArrayList<Integer> A, int B) {
        // Map to store frequency of prefix sums
        HashMap<Integer, Integer> map = new HashMap<>();
        // Base case: one way to have prefix sum = 0 before starting
        map.put(0, 1);

        int count = 0;
        int sum = 0;
        for (Integer integer : A) {
            sum += integer;
            // If there is a prefix sum that is (sum - B), then
            // the subarray between that prefix and current index sums to B
            if (map.containsKey(sum - B)) {
                count += map.get(sum - B);
            }
            // Record the current prefix sum in the map
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
}

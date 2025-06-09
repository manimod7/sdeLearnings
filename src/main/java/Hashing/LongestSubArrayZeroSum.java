package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LongestSubArrayZeroSum {
    public int solve(ArrayList<Integer> A) {
        int ans = 0;
        Map<Long,Integer> map = new HashMap<>();

        map.put(0L, -1);

        long currentSum = 0;
        for(int i = 0; i < A.size(); i++) {
            currentSum += A.get(i);

            if (map.containsKey(currentSum)) {

                ans = Math.max(ans, i - map.get(currentSum));
            } else {

                map.put(currentSum, i);
            }
        }
        return ans;
    }
}

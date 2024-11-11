package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntersectionOfTwoArrays {

    //Leetcode 350
    public int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> mp1 = new HashMap<>();

        // Count frequencies of elements in nums1
        for (int x : nums1) {
            mp1.put(x, mp1.getOrDefault(x, 0) + 1);
        }

        List<Integer> ans = new ArrayList<>();

        // Find intersections based on nums2 and mp1 frequencies
        for (int x : nums2) {
            if (mp1.containsKey(x) && mp1.get(x) > 0) {
                ans.add(x);
                mp1.put(x, mp1.get(x) - 1); // Decrease the count for mp1
            }
        }

        // Convert the list to an int array
        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }
}

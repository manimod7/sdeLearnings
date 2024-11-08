package arrays;

import java.util.HashSet;
import java.util.Set;

public class IntersectionOf2Arrays {
    public int[] intersection(int[] nums1, int[] nums2) {
        // Use a set to store unique elements of nums1
        Set<Integer> set1 = new HashSet<>();
        for (int num : nums1) {
            set1.add(num);
        }

        // Use another set to store the intersection result
        Set<Integer> resultSet = new HashSet<>();
        for (int num : nums2) {
            if (set1.contains(num)) {
                resultSet.add(num); // Add to result if it's in both arrays
            }
        }

        // Convert resultSet to an int[] array
        int[] result = new int[resultSet.size()];
        int i = 0;
        for (int num : resultSet) {
            result[i++] = num;
        }

        return result;
    }
}

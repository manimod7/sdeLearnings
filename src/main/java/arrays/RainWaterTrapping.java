package arrays;

import java.util.*;

public class RainWaterTrapping {
    public int trap(final List<Integer> A) {
        int n = A.size();
        if (n <= 2) {
            return 0; // No space to trap water
        }
        int left = 0, right = n - 1;
        int leftMax = 0, rightMax = 0;
        int trappedWater = 0;
        while (left < right) {
            if (A.get(left) < A.get(right)) {
                if (A.get(left) > leftMax) {
                    leftMax = A.get(left);
                } else {
                    trappedWater += leftMax - A.get(left);
                }
                left++;
            } else {
                if (A.get(right) > rightMax) {
                    rightMax = A.get(right);
                } else {
                    trappedWater += rightMax - A.get(right);
                }
                right--;
            }
        }
        return trappedWater;
    }

}

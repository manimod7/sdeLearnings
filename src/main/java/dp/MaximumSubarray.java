package dp;

/**
 * LeetCode 53 - Maximum Subarray (Kadane's Algorithm)
 *
 * Problem: Find the contiguous subarray with the largest sum.
 *
 * Solution: Kadane's algorithm to accumulate max ending here.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        int cur = nums[0], best = nums[0];
        for (int i=1;i<nums.length;i++) {
            cur = Math.max(nums[i], cur + nums[i]);
            best = Math.max(best, cur);
        }
        return best;
    }
}

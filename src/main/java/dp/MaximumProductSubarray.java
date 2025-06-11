package dp;

/**
 * LeetCode 152 - Maximum Product Subarray
 *
 * Problem: Find contiguous subarray within an array having largest product.
 *
 * Solution: Track current max and min product while scanning.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class MaximumProductSubarray {
    public int maxProduct(int[] nums) {
        int max = nums[0], min = nums[0], ans = nums[0];
        for (int i=1;i<nums.length;i++) {
            int cur = nums[i];
            if (cur<0) {int t=max; max=min; min=t;}
            max = Math.max(cur, max*cur);
            min = Math.min(cur, min*cur);
            ans = Math.max(ans, max);
        }
        return ans;
    }
}

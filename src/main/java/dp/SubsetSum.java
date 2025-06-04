package dp;

/*
 * Problem:
 * Given an array of integers nums[] and a target sum,
 * determine if there is a subset of the array whose sum is exactly equal to the target.
 *
 * Example:
 * nums = [2,3,7,8,10]
 * target = 11
 * Output = true
 * Explanation: {3,8} form a subset with sum 11.
 *
 * Approach:
 * - Use Dynamic Programming (DP).
 * - Create a 2D boolean DP table where:
 *   dp[i][j] = true if a subset of first i elements can sum up to j.
 *
 * Logic:
 * - If we don't include the current element, we carry over the value from above (dp[i-1][j]).
 * - If we include the current element, we check dp[i-1][j - nums[i-1]].
 * - Final answer is dp[n][target].
 *
 * Dry Run Example:
 * nums = [2,3,7,8,10], target = 11
 *
 * DP Table (True = reachable sum):
 * - Start with 0 sum reachable by empty subset.
 * - Fill table by either picking or not picking each element.
 *
 * Time Complexity: O(n * target)
 * - n = number of elements
 * - target = sum value
 *
 * Space Complexity: O(n * target)
 * - 2D DP table
 */

import java.util.Collections;

public class SubsetSum {

    public boolean isSubsetSum(int[] nums, int target) {
        int n = nums.length;

        // Create DP table of size (n+1) x (target+1)
        boolean[][] dp = new boolean[n + 1][target + 1];

        // Base Case: 0 sum is always possible (empty subset)
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        // Fill the DP table
        for (int i = 1; i <= n; i++) { // Loop over all elements
            for (int j = 1; j <= target; j++) { // Loop over all target sums
                if (j < nums[i - 1]) {
                    // If current element is greater than target sum, cannot pick it
                    dp[i][j] = dp[i - 1][j];
                } else {
                    // Else check:
                    // - not including current element (dp[i-1][j])
                    // - including current element (dp[i-1][j - nums[i-1]])
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        String s = "ABCD";
        StringBuilder s1 = new StringBuilder(s);
        s1.reverse();
        // Final answer is whether we can form the target sum using all elements
        return dp[n][target];
    }
}


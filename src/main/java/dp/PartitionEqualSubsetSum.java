package dp;

/**
 * LeetCode 416 - Partition Equal Subset Sum
 *
 * Problem: Determine if array nums can be partitioned into two subsets with
 * equal sum.
 *
 * Solution: Reduce to subset sum with target total/2 using DP boolean array.
 *
 * Time: O(n*sum)
 * Space: O(sum)
 */
public class PartitionEqualSubsetSum {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int x : nums) sum += x;
        if (sum % 2 != 0) return false;
        int target = sum / 2;
        boolean[] dp = new boolean[target+1];
        dp[0] = true;
        for (int num : nums) {
            for (int s = target; s >= num; s--) {
                dp[s] |= dp[s-num];
            }
        }
        return dp[target];
    }
}

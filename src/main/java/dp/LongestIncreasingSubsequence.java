package dp;

/**
 * LeetCode 300 - Longest Increasing Subsequence
 *
 * Problem: Given an integer array nums, return the length of the longest strictly
 * increasing subsequence.
 *
 * Solution: Classic DP where dp[i] is the length of LIS ending at i.
 * Also implement patience sorting approach with binary search for O(n log n).
 *
 * Time: O(n log n)
 * Space: O(n)
 */
public class LongestIncreasingSubsequence {
    public int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int i = 0, j = size;
            while (i < j) {
                int m = (i + j) / 2;
                if (tails[m] < x) i = m + 1; else j = m;
            }
            tails[i] = x;
            if (i == size) size++;
        }
        return size;
    }
}

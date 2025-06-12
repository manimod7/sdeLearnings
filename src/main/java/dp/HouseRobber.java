package dp;

/**
 * LeetCode 198 - House Robber
 *
 * Problem: Given non-negative integers representing amount of money in each
 * house, find max money without robbing adjacent houses.
 *
 * Solution: DP with rolling variables representing rob or skip previous house.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class HouseRobber {
    public int rob(int[] nums) {
        int robPrev = 0, skipPrev = 0;
        for (int n : nums) {
            int newRob = skipPrev + n;
            skipPrev = Math.max(skipPrev, robPrev);
            robPrev = newRob;
        }
        return Math.max(robPrev, skipPrev);
    }
}

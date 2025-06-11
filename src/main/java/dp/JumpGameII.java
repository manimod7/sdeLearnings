package dp;

/**
 * LeetCode 45 - Jump Game II
 *
 * Problem: Given an array of non-negative integers, find minimum number of
 * jumps to reach last index. Guaranteed reachable.
 *
 * Solution: Greedy BFS-like traversal using current range and next range.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class JumpGameII {
    public int jump(int[] nums) {
        int jumps = 0, curEnd = 0, curFarthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            curFarthest = Math.max(curFarthest, i + nums[i]);
            if (i == curEnd) {
                jumps++;
                curEnd = curFarthest;
            }
        }
        return jumps;
    }
}

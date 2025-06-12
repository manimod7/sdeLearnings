package dp;

/**
 * LeetCode 55 - Jump Game
 *
 * Problem: Given an array of non-negative integers where each element represents
 * your max jump length at that position, determine if you can reach the last
 * index.
 *
 * Solution: Greedy/dp where we keep track of farthest reachable index.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class JumpGame {
    public boolean canJump(int[] nums) {
        int reach = 0;
        for (int i = 0; i < nums.length && i <= reach; i++) {
            reach = Math.max(reach, i + nums[i]);
        }
        return reach >= nums.length - 1;
    }
}

package dp;

/**
 * LeetCode 213 - House Robber II
 *
 * Problem: Houses form a circle. Cannot rob adjacent houses. Maximize amount.
 *
 * Solution: Run House Robber on two ranges excluding first or last house.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class HouseRobberII {
    private int robLine(int[] nums, int l, int r) {
        int robPrev=0, skipPrev=0;
        for (int i=l;i<=r;i++) {
            int newRob = skipPrev + nums[i];
            skipPrev = Math.max(skipPrev, robPrev);
            robPrev = newRob;
        }
        return Math.max(robPrev, skipPrev);
    }
    public int rob(int[] nums) {
        if (nums.length==1) return nums[0];
        return Math.max(robLine(nums,0,nums.length-2), robLine(nums,1,nums.length-1));
    }
}

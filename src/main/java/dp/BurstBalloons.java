package dp;

/**
 * LeetCode 312 - Burst Balloons
 *
 * Problem: Given n balloons with numbers, burst them to maximize coins earned
 * where bursting balloon i gives nums[left]*nums[i]*nums[right].
 *
 * Solution: Interval DP. dp[l][r] is max coins for subarray l..r.
 *
 * Time: O(n^3)
 * Space: O(n^2)
 */
public class BurstBalloons {
    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] arr = new int[n+2];
        System.arraycopy(nums,0,arr,1,n);
        arr[0]=arr[n+1]=1;
        int[][] dp=new int[n+2][n+2];
        for(int len=1;len<=n;len++)
            for(int l=1;l+len-1<=n;l++){
                int r=l+len-1;
                for(int k=l;k<=r;k++)
                    dp[l][r]=Math.max(dp[l][r],arr[l-1]*arr[k]*arr[r+1]+dp[l][k-1]+dp[k+1][r]);
            }
        return dp[1][n];
    }
}

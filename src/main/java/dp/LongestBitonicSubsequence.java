package dp;

/**
 * Longest Bitonic Subsequence
 *
 * Problem: Find length of the longest subsequence which is increasing and then
 * decreasing.
 *
 * Solution: Compute LIS ending at each index and LDS starting from index, then
 * combine.
 *
 * Time: O(n^2)
 * Space: O(n)
 */
public class LongestBitonicSubsequence {
    public int longestBitonic(int[] nums){
        int n=nums.length;int[] lis=new int[n];int[] lds=new int[n];
        for(int i=0;i<n;i++){lis[i]=1;for(int j=0;j<i;j++) if(nums[j]<nums[i]) lis[i]=Math.max(lis[i],lis[j]+1);} 
        for(int i=n-1;i>=0;i--){lds[i]=1;for(int j=n-1;j>i;j--) if(nums[j]<nums[i]) lds[i]=Math.max(lds[i],lds[j]+1);} 
        int ans=0;for(int i=0;i<n;i++) ans=Math.max(ans,lis[i]+lds[i]-1);return ans;
    }
}

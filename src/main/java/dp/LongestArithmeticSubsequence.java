package dp;

import java.util.*;

/**
 * LeetCode 1027 - Longest Arithmetic Subsequence
 *
 * Problem: Given an array, return the length of the longest arithmetic subsequence.
 *
 * Solution: DP using a map of difference to length for each index.
 *
 * Time: O(n^2)
 * Space: O(n^2)
 */
public class LongestArithmeticSubsequence {
    public int longestArithSeqLength(int[] A) {
        int n=A.length, ans=0;
        Map<Integer,Integer>[] dp=new HashMap[n];
        for(int i=0;i<n;i++){dp[i]=new HashMap<>();
            for(int j=0;j<i;j++){
                int d=A[i]-A[j];
                int len=dp[j].getOrDefault(d,1)+1;
                dp[i].put(d,len);
                ans=Math.max(ans,len);
            }
        }
        return ans;
    }
}

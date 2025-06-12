package dp;

/**
 * LeetCode 647 - Palindromic Substrings
 *
 * Problem: Given a string s, return the number of palindromic substrings in it.
 *
 * Solution: DP expanding around centers or dp[i][j] approach. Here dp table.
 *
 * Time: O(n^2)
 * Space: O(n^2)
 */
public class PalindromicSubstrings {
    public int countSubstrings(String s){
        int n=s.length(),count=0;boolean[][] dp=new boolean[n][n];
        for(int i=n-1;i>=0;i--)
            for(int j=i;j<n;j++){
                dp[i][j]=s.charAt(i)==s.charAt(j)&&(j-i<2||dp[i+1][j-1]);
                if(dp[i][j]) count++;}
        return count;
    }
}

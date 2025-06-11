package dp;

/**
 * LeetCode 5 - Longest Palindromic Substring
 *
 * Problem: Given a string s, return the longest palindromic substring in s.
 *
 * Solution: DP where dp[i][j] is true if substring i..j is palindrome.
 *
 * Time: O(n^2)
 * Space: O(n^2)
 */
public class LongestPalindromicSubstring {
    public String longestPalindrome(String s) {
        int n = s.length(), start=0,len=1;
        boolean[][] dp = new boolean[n][n];
        for (int i=n-1;i>=0;i--) {
            dp[i][i]=true;
            for (int j=i+1;j<n;j++) {
                dp[i][j]=s.charAt(i)==s.charAt(j) && (j-i<2 || dp[i+1][j-1]);
                if (dp[i][j] && j-i+1>len) {start=i; len=j-i+1;}
            }
        }
        return s.substring(start,start+len);
    }
}

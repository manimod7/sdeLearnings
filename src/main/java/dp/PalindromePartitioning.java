package dp;

import java.util.*;

/**
 * LeetCode 131 - Palindrome Partitioning
 *
 * Problem: Partition string s into all possible palindrome partitions.
 *
 * Solution: DFS with DP memoization for palindrome check.
 *
 * Time: O(n*2^n)
 * Space: O(n^2)
 */
public class PalindromePartitioning {
    public List<List<String>> partition(String s) {
        int n=s.length();
        boolean[][] dp=new boolean[n][n];
        for(int i=n-1;i>=0;i--)
            for(int j=i;j<n;j++)
                dp[i][j]=s.charAt(i)==s.charAt(j)&&(j-i<2||dp[i+1][j-1]);
        List<List<String>> res=new ArrayList<>();
        backtrack(s,0,dp,new ArrayList<>(),res);
        return res;
    }
    private void backtrack(String s,int start,boolean[][] dp,List<String> cur,List<List<String>> res){
        if(start==s.length()){res.add(new ArrayList<>(cur));return;}
        for(int end=start;end<s.length();end++)
            if(dp[start][end]){cur.add(s.substring(start,end+1));
                backtrack(s,end+1,dp,cur,res);cur.remove(cur.size()-1);}
    }
}

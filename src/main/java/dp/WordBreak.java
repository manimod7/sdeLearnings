package dp;

import java.util.*;

/**
 * LeetCode 139 - Word Break
 *
 * Problem: Determine if s can be segmented into space-separated sequence of one
 * or more dictionary words.
 *
 * Solution: DP where dp[i] is true if prefix s[0..i) can be segmented.
 *
 * Time: O(n^2)
 * Space: O(n)
 */
public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length()+1];
        dp[0] = true;
        for (int i=1;i<=s.length();i++)
            for (int j=0;j<i;j++)
                if (dp[j] && set.contains(s.substring(j,i))) {
                    dp[i]=true; break;
                }
        return dp[s.length()];
    }
}

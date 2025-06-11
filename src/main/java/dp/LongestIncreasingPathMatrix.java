package dp;

/**
 * LeetCode 329 - Longest Increasing Path in a Matrix
 *
 * Problem: Given an integer matrix, find the length of the longest increasing
 * path in matrix. Move in 4 directions.
 *
 * Solution: DFS + memoization (DP) from each cell.
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class LongestIncreasingPathMatrix {
    private static final int[][] DIR={{1,0},{-1,0},{0,1},{0,-1}};
    public int longestIncreasingPath(int[][] mat){
        int m=mat.length,n=mat[0].length;int[][] dp=new int[m][n];int ans=0;
        for(int i=0;i<m;i++) for(int j=0;j<n;j++) ans=Math.max(ans,dfs(mat,i,j,dp));
        return ans;
    }
    private int dfs(int[][] A,int r,int c,int[][] dp){
        if(dp[r][c]>0) return dp[r][c];
        dp[r][c]=1;
        for(int[] d:DIR){int nr=r+d[0],nc=c+d[1];
            if(nr>=0&&nr<A.length&&nc>=0&&nc<A[0].length&&A[nr][nc]>A[r][c])
                dp[r][c]=Math.max(dp[r][c],1+dfs(A,nr,nc,dp));}
        return dp[r][c];
    }
}

package graphs;

/**
 * LeetCode 695 - Max Area of Island
 *
 * Problem: Given a binary grid, return the size of the largest island (connected
 * group of 1s). Islands are connected 4-directionally.
 *
 * Solution: DFS through each cell, computing area by exploring neighbors. Keep
 * track of the maximum area encountered.
 */
public class MaxAreaOfIsland {
    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length, n = grid[0].length, max = 0;
        boolean[][] vis = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 && !vis[i][j]) {
                    max = Math.max(max, dfs(grid, vis, i, j, m, n));
                }
            }
        }
        return max;
    }
    private int dfs(int[][] grid, boolean[][] vis, int i, int j, int m, int n) {
        if (i<0||i>=m||j<0||j>=n||vis[i][j]||grid[i][j]==0) return 0;
        vis[i][j] = true;
        return 1 + dfs(grid,vis,i+1,j,m,n) + dfs(grid,vis,i-1,j,m,n)
                 + dfs(grid,vis,i,j+1,m,n) + dfs(grid,vis,i,j-1,m,n);
    }
}

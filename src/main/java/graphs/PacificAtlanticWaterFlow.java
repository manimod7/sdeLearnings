package graphs;

import java.util.*;

/**
 * LeetCode 417 - Pacific Atlantic Water Flow
 *
 * Problem: Given an m x n matrix of heights, find all coordinates where water
 * can flow to both the Pacific and Atlantic oceans. Water can flow from a cell
 * to another if the next cell's height is less or equal.
 *
 * Solution: Perform reverse DFS/BFS from the ocean borders to mark reachable
 * cells. Intersect the cells reachable from both oceans.
 */
public class PacificAtlanticWaterFlow {
    public List<int[]> pacificAtlantic(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        boolean[][] pac = new boolean[m][n];
        boolean[][] atl = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            dfs(heights, pac, i, 0, heights[i][0]);
            dfs(heights, atl, i, n - 1, heights[i][n - 1]);
        }
        for (int j = 0; j < n; j++) {
            dfs(heights, pac, 0, j, heights[0][j]);
            dfs(heights, atl, m - 1, j, heights[m - 1][j]);
        }
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pac[i][j] && atl[i][j]) res.add(new int[]{i, j});
            }
        }
        return res;
    }
    private void dfs(int[][] h, boolean[][] vis, int r, int c, int prev) {
        int m = h.length, n = h[0].length;
        if (r<0||r>=m||c<0||c>=n||vis[r][c]||h[r][c]<prev) return;
        vis[r][c] = true;
        dfs(h, vis, r+1,c,h[r][c]); dfs(h,vis,r-1,c,h[r][c]);
        dfs(h, vis, r,c+1,h[r][c]); dfs(h,vis,r,c-1,h[r][c]);
    }
}

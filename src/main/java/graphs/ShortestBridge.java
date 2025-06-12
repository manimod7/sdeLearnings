package graphs;

import java.util.*;

/**
 * LeetCode 934 - Shortest Bridge
 *
 * Problem: Given a binary grid with exactly two islands, return the length of
 * the shortest bridge (number of 0s to flip) to connect them.
 *
 * Solution: Use DFS to mark one island. Then BFS expand outward until reaching
 * the second island, counting layers as distance.
 */
public class ShortestBridge {
    public int shortestBridge(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<int[]> q = new ArrayDeque<>();
        boolean found = false;
        for (int i = 0; i < m && !found; i++) {
            for (int j = 0; j < n && !found; j++) {
                if (grid[i][j] == 1) {
                    dfs(grid, i, j, q, m, n);
                    found = true;
                }
            }
        }
        int dist = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!q.isEmpty()) {
            int size = q.size();
            for (int s = 0; s < size; s++) {
                int[] cur = q.poll();
                for (int[] d : dirs) {
                    int x = cur[0] + d[0], y = cur[1] + d[1];
                    if (x<0||x>=m||y<0||y>=n||grid[x][y]==2) continue;
                    if (grid[x][y]==1) return dist;
                    grid[x][y]=2;
                    q.offer(new int[]{x,y});
                }
            }
            dist++;
        }
        return -1;
    }
    private void dfs(int[][] g, int i, int j, Queue<int[]> q, int m, int n) {
        if (i<0||i>=m||j<0||j>=n||g[i][j]!=1) return;
        g[i][j]=2; q.offer(new int[]{i,j});
        dfs(g,i+1,j,q,m,n); dfs(g,i-1,j,q,m,n); dfs(g,i,j+1,q,m,n); dfs(g,i,j-1,q,m,n);
    }
}

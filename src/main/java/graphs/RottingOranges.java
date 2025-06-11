package graphs;

import java.util.*;

/**
 * LeetCode 994 - Rotting Oranges
 *
 * Problem: Given a grid of oranges where 0 = empty, 1 = fresh, 2 = rotten,
 * each minute any fresh orange next to a rotten one becomes rotten. Return the
 * minimum number of minutes until no fresh orange remains, or -1 if impossible.
 *
 * Solution: Perform BFS from all initially rotten oranges simultaneously,
 * spreading rot minute by minute. Track time via queue size.
 */
public class RottingOranges {
    public int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<int[]> q = new ArrayDeque<>();
        int fresh = 0, time = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) q.offer(new int[]{i, j});
                else if (grid[i][j] == 1) fresh++;
            }
        }
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (fresh > 0 && !q.isEmpty()) {
            int size = q.size();
            for (int s = 0; s < size; s++) {
                int[] cur = q.poll();
                for (int[] d : dirs) {
                    int x = cur[0] + d[0], y = cur[1] + d[1];
                    if (x>=0 && x<m && y>=0 && y<n && grid[x][y]==1) {
                        grid[x][y]=2;
                        fresh--; q.offer(new int[]{x,y});
                    }
                }
            }
            time++;
        }
        return fresh==0 ? time : -1;
    }
}

package graphs;

import java.util.*;

/**
 * LeetCode 1631 - Path With Minimum Effort
 *
 * Problem: Given a matrix of heights, return the minimum effort required to go
 * from top-left to bottom-right, where effort is the maximum absolute difference
 * in heights between consecutive cells along the path.
 *
 * Solution: Use Dijkstra-like BFS where edge weights are the absolute height
 * differences. The state effort is the max difference along the path so far.
 */
public class PathWithMinimumEffort {
    public int minimumEffortPath(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        int[][] dist = new int[m][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a->a[2]));
        pq.offer(new int[]{0,0,0});
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int r = cur[0], c = cur[1], e = cur[2];
            if (r==m-1 && c==n-1) return e;
            if (e > dist[r][c]) continue;
            for (int[] d : dirs) {
                int nr=r+d[0], nc=c+d[1];
                if (nr>=0&&nr<m&&nc>=0&&nc<n) {
                    int nEff = Math.max(e, Math.abs(heights[r][c]-heights[nr][nc]));
                    if (nEff < dist[nr][nc]) {
                        dist[nr][nc] = nEff;
                        pq.offer(new int[]{nr,nc,nEff});
                    }
                }
            }
        }
        return 0;
    }
}

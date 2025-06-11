package graphs;

import java.util.*;

/**
 * LeetCode 286 - Walls and Gates
 *
 * Problem: Given a grid with walls (-1), gates (0), and empty rooms (INF), fill
 * each empty room with the distance to its nearest gate.
 *
 * Solution: Multi-source BFS from all gates simultaneously to fill distances.
 */
public class WallsAndGates {
    private static final int INF = Integer.MAX_VALUE;
    public void wallsAndGates(int[][] rooms) {
        int m = rooms.length, n = rooms[0].length;
        Queue<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) q.offer(new int[]{i, j});
            }
        }
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] d : dirs) {
                int x = cur[0]+d[0], y = cur[1]+d[1];
                if (x>=0&&x<m&&y>=0&&y<n&&rooms[x][y]==INF) {
                    rooms[x][y] = rooms[cur[0]][cur[1]] + 1;
                    q.offer(new int[]{x,y});
                }
            }
        }
    }
}

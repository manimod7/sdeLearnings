package graphs;

import java.util.*;

/**
 * LeetCode 1926 - Nearest Exit from Entrance in Maze
 *
 * Problem: Given a maze with walls '+' and empty cells '.', and an entrance,
 * find the nearest exit cell using shortest path steps. Return -1 if no exit.
 *
 * Solution: BFS from the entrance, track steps until we reach any boundary cell
 * that is not the entrance itself.
 */
public class NearestExitFromEntranceInMaze {
    public int nearestExit(char[][] maze, int[] entrance) {
        int m = maze.length, n = maze[0].length;
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{entrance[0], entrance[1], 0});
        boolean[][] vis = new boolean[m][n];
        vis[entrance[0]][entrance[1]] = true;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1], d = cur[2];
            if ((r==0||c==0||r==m-1||c==n-1) && (r!=entrance[0]||c!=entrance[1]))
                return d;
            for (int[] dir : dirs) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr>=0 && nr<m && nc>=0 && nc<n && maze[nr][nc]=='.' && !vis[nr][nc]) {
                    vis[nr][nc]=true; q.offer(new int[]{nr,nc,d+1});
                }
            }
        }
        return -1;
    }
}

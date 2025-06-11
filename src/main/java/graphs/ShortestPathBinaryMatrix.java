package graphs;

import java.util.*;

/**
 * LeetCode 1091 - Shortest Path in Binary Matrix
 *
 * Problem: In an n x n binary matrix, return the length of the shortest clear
 * path from top-left to bottom-right using 8-directional moves. Return -1 if no
 * such path.
 *
 * Solution: BFS from (0,0) exploring 8 directions.
 */
public class ShortestPathBinaryMatrix {
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;
        if (grid[0][0]==1 || grid[n-1][n-1]==1) return -1;
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] vis = new boolean[n][n];
        q.offer(new int[]{0,0,1});
        vis[0][0]=true;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int r=cur[0],c=cur[1],d=cur[2];
            if (r==n-1 && c==n-1) return d;
            for (int[] di:dirs){
                int nr=r+di[0], nc=c+di[1];
                if(nr>=0&&nr<n&&nc>=0&&nc<n&&!vis[nr][nc]&&grid[nr][nc]==0){
                    vis[nr][nc]=true; q.offer(new int[]{nr,nc,d+1});
                }
            }
        }
        return -1;
    }
}

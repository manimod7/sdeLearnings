package graphs;

import java.util.*;

/**
 * LeetCode 886 - Possible Bipartition
 *
 * Problem: Given N people numbered 1..N and a list of dislike pairs, determine
 * if it is possible to split everyone into two groups so that no pair of people
 * in the same group dislike each other.
 *
 * Solution: Build a graph from the dislike pairs and attempt to 2-color it using
 * BFS.
 */
public class PossibleBipartition {
    public boolean possibleBipartition(int n, int[][] dislikes) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
        for (int[] d : dislikes) { g.get(d[0]).add(d[1]); g.get(d[1]).add(d[0]); }
        int[] color = new int[n+1];
        for (int i = 1; i <= n; i++) {
            if (color[i] != 0) continue;
            Queue<Integer> q = new ArrayDeque<>();
            q.offer(i); color[i] = 1;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : g.get(u)) {
                    if (color[v] == 0) { color[v] = -color[u]; q.offer(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }
}

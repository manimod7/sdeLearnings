package graphs;

import java.util.*;

/**
 * LeetCode 1192 - Critical Connections in a Network
 *
 * Problem: Given a network of n servers numbered 0..n-1 and a list of connections,
 * find all critical connections that would disconnect the network if removed.
 *
 * Solution: Use Tarjan's algorithm for bridges using DFS timestamps and low-link values.
 */
public class CriticalConnections {
    private int time = 0;
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (List<Integer> c : connections) {
            int u = c.get(0), v = c.get(1);
            graph.get(u).add(v); graph.get(v).add(u);
        }
        int[] disc = new int[n];
        int[] low = new int[n];
        Arrays.fill(disc, -1);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) if (disc[i] == -1) dfs(i, -1, graph, disc, low, res);
        return res;
    }
    private void dfs(int u, int parent, List<List<Integer>> g, int[] disc, int[] low, List<List<Integer>> res) {
        disc[u] = low[u] = ++time;
        for (int v : g.get(u)) {
            if (v == parent) continue;
            if (disc[v] == -1) {
                dfs(v, u, g, disc, low, res);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u]) res.add(Arrays.asList(u, v));
            } else {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
}

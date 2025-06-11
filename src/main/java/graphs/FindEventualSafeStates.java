package graphs;

import java.util.*;

/**
 * LeetCode 802 - Find Eventual Safe States
 *
 * Problem: In a directed graph, a node is eventually safe if every possible
 * path starting from that node leads to a terminal node (a node with no
 * outgoing edges). Return all the eventual safe nodes in sorted order.
 *
 * Solution: Reverse the graph and perform a topological sort-like process.
 * Nodes with out-degree zero are safe. Repeatedly remove such nodes and
 * decrease the out-degree of their predecessors.
 */
public class FindEventualSafeStates {
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> rev = new ArrayList<>();
        int[] out = new int[n];
        for (int i = 0; i < n; i++) rev.add(new ArrayList<>());
        for (int i = 0; i < n; i++) {
            out[i] = graph[i].length;
            for (int v : graph[i]) rev.get(v).add(i);
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (out[i] == 0) q.offer(i);
        boolean[] safe = new boolean[n];
        while (!q.isEmpty()) {
            int u = q.poll();
            safe[u] = true;
            for (int p : rev.get(u)) {
                if (--out[p] == 0) q.offer(p);
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) if (safe[i]) res.add(i);
        return res;
    }
}

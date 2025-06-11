package graphs;

import java.util.*;

/**
 * LeetCode 797 - All Paths From Source to Target
 *
 * Problem: Given a directed acyclic graph, return all possible paths from node 0
 * to node n-1.
 *
 * Solution: Simple DFS backtracking collecting paths.
 */
public class AllPathsFromSourceToTarget {
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        path.add(0);
        dfs(0, graph, path, res);
        return res;
    }
    private void dfs(int node, int[][] g, List<Integer> path, List<List<Integer>> res) {
        if (node == g.length - 1) { res.add(new ArrayList<>(path)); return; }
        for (int nei : g[node]) {
            path.add(nei); dfs(nei, g, path, res); path.remove(path.size()-1);
        }
    }
}

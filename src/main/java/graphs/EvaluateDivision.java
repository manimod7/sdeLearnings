package graphs;

import java.util.*;

/**
 * LeetCode 399 - Evaluate Division
 *
 * Problem: Given equations like a/b = 2.0 and queries, compute each query or -1
 * if impossible.
 *
 * Solution: Build a graph with edges representing ratios and run DFS for each
 * query to compute reachable ratios.
 */
public class EvaluateDivision {
    public double[] calcEquation(List<List<String>> equations, double[] values,
                                 List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0), b = equations.get(i).get(1);
            double v = values[i];
            graph.computeIfAbsent(a,x->new HashMap<>()).put(b,v);
            graph.computeIfAbsent(b,x->new HashMap<>()).put(a,1/v);
        }
        double[] res = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            res[i] = dfs(queries.get(i).get(0), queries.get(i).get(1), graph, new HashSet<>());
        }
        return res;
    }
    private double dfs(String a, String b, Map<String, Map<String, Double>> g, Set<String> seen) {
        if (!g.containsKey(a) || !seen.add(a)) return -1.0;
        if (a.equals(b)) return 1.0;
        for (Map.Entry<String,Double> e : g.get(a).entrySet()) {
            double sub = dfs(e.getKey(), b, g, seen);
            if (sub != -1.0) return e.getValue()*sub;
        }
        return -1.0;
    }
}

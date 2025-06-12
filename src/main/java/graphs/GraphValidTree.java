package graphs;

import java.util.*;

/**
 * LeetCode 261 - Graph Valid Tree
 *
 * Problem: Given n nodes labeled 0..n-1 and a list of undirected edges, check
 * if they form a valid tree (connected and acyclic).
 *
 * Solution: Use Union-Find to detect cycles and ensure exactly n-1 edges.
 */
public class GraphValidTree {
    public boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) return false;
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        for (int[] e : edges) {
            int a = find(parent, e[0]);
            int b = find(parent, e[1]);
            if (a == b) return false;
            parent[a] = b;
        }
        return true; // edges == n-1 ensures connected
    }
    private int find(int[] p, int x) {
        if (p[x] != x) p[x] = find(p, p[x]);
        return p[x];
    }
}

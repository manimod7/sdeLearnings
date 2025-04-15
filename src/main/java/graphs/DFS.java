package graphs;

import java.util.*;

public class DFS{

    public static void dfsRecursive(int node, boolean[] visited, Map<Integer, List<Integer>> adj) {
        visited[node] = true;
        System.out.print(node + " ");

        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfsRecursive(neighbor, visited, adj);
            }
        }
    }

    public static void main(String[] args) {
        int V = 5;
        Map<Integer, List<Integer>> adj = new HashMap<>();
        for (int i = 0; i < V; i++) adj.put(i, new ArrayList<>());

        adj.get(0).addAll(Arrays.asList(1, 2));
        adj.get(1).addAll(Arrays.asList(0, 3));
        adj.get(2).addAll(Arrays.asList(0, 4));
        adj.get(3).add(1);
        adj.get(4).add(2);

        boolean[] visited = new boolean[V];
        System.out.print("DFS (Recursive) from 0: ");
        dfsRecursive(0, visited, adj);
    }
}


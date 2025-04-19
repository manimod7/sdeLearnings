package graphs;

import java.util.*;

public class DFSTopologicalSort {

    public static List<Integer> topoSort(int[][] edges, int numVertices) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) adj.add(new ArrayList<>());

        // Build adjacency list
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            adj.get(from).add(to);
        }

        boolean[] visited = new boolean[numVertices];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                dfs(i, adj, visited, stack);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!stack.isEmpty()) {
            topoOrder.add(stack.pop());
        }

        return topoOrder;
    }

    private static void dfs(int node, List<List<Integer>> adj, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;

        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adj, visited, stack);
            }
        }

        // Push to stack after processing all neighbors
        stack.push(node);
    }

    public static void main(String[] args) {
        int[][] edges = {
                {5, 0},
                {5, 2},
                {4, 0},
                {4, 1},
                {2, 3},
                {3, 1}
        };

        int numVertices = 6;  // vertices are 0 to 5

        List<Integer> topo = topoSort(edges, numVertices);
        System.out.println("Topological Sort (DFS): " + topo);
    }
}


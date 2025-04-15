package graphs;

import java.util.*;

public class BFS {

    public static void bfs(int start, Map<Integer, List<Integer>> adj, int V) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            System.out.print(curr + " ");

            for (int neighbor : adj.get(curr)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
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

        System.out.print("BFS Traversal from 0: ");
        bfs(0, adj, V);
    }
}


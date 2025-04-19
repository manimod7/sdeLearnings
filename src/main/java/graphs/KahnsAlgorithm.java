package graphs;

import java.util.*;

public class KahnsAlgorithm{

    public static List<Integer> kahnsTopologicalSort(int numVertices, List<List<Integer>> graph) {
        int[] inDegree = new int[numVertices];
        List<Integer> topoOrder = new ArrayList<>();

        // Step 1: Compute in-degree of each node
        for (int u = 0; u < numVertices; u++) {
            for (int v : graph.get(u)) {
                inDegree[v]++;
            }
        }

        // Step 2: Add all nodes with in-degree 0 to the queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 3: Process the queue
        while (!queue.isEmpty()) {
            int node = queue.poll();
            topoOrder.add(node);

            for (int neighbor : graph.get(node)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Step 4: Check if topological sort is possible (i.e., no cycle)
        if (topoOrder.size() != numVertices) {
            System.out.println("Cycle detected! Topological sort not possible.");
            return new ArrayList<>();
        }

        return topoOrder;
    }

    public static void main(String[] args) {
        int numVertices = 6;

        // Create graph as adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            graph.add(new ArrayList<>());
        }

        // Example graph:
        // 5 → 2, 0
        // 4 → 0, 1
        // 2 → 3
        // 3 → 1

        graph.get(5).add(2);
        graph.get(5).add(0);
        graph.get(4).add(0);
        graph.get(4).add(1);
        graph.get(2).add(3);
        graph.get(3).add(1);

        List<Integer> topoSort = kahnsTopologicalSort(numVertices, graph);

        System.out.println("Topological Sort: " + topoSort);
    }
}


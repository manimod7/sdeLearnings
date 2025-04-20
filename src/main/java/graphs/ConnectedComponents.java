package graphs;

import java.util.*;

//Undirected Graph
public class ConnectedComponents {

    public static List<List<Integer>> findConnectedComponents(int numNodes, List<List<Integer>> graph) {
        boolean[] visited = new boolean[numNodes];
        List<List<Integer>> components = new ArrayList<>();

        for (int node = 0; node < numNodes; node++) {
            if (!visited[node]) {
                List<Integer> component = new ArrayList<>();
                dfs(node, graph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfs(int node, List<List<Integer>> graph, boolean[] visited, List<Integer> component) {
        visited[node] = true;
        component.add(node);

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, graph, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        int numNodes = 6;
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < numNodes; i++) {
            graph.add(new ArrayList<>());
        }

        // Add edges (undirected)
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 3, 4);

        // Expected: 3 components => [0, 1, 2], [3, 4], [5]
        List<List<Integer>> components = findConnectedComponents(numNodes, graph);

        System.out.println("Connected Components:");
        for (List<Integer> component : components) {
            System.out.println(component);
        }
    }

    private static void addEdge(List<List<Integer>> graph, int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u); // undirected edge
    }
}

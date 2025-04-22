package graphs;

import java.util.*;

public class KosarajuAlgorithm {

    // Step 1: Do DFS and fill nodes in stack according to their finishing times
    private static void dfs1(int node, boolean[] visited, Stack<Integer> stack, List<List<Integer>> graph) {
        visited[node] = true;
        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                dfs1(neighbor, visited, stack, graph);
            }
        }
        stack.push(node);
    }

    // Step 2 - Function to reverse the graph
    private static List<List<Integer>> reverseGraph(List<List<Integer>> graph) {
        int n = graph.size();
        List<List<Integer>> reversed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            reversed.add(new ArrayList<>());
        }

        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                reversed.get(v).add(u);
            }
        }
        return reversed;
    }

    // Step 3: Do DFS on reversed graph to collect one SCC
    private static void dfs2(int node, boolean[] visited, List<Integer> component, List<List<Integer>> reversedGraph) {
        visited[node] = true;
        component.add(node);
        for (int neighbor : reversedGraph.get(node)) {
            if (!visited[neighbor]) {
                dfs2(neighbor, visited, component, reversedGraph);
            }
        }
    }

    // Main function to find SCCs
    public static List<List<Integer>> kosaraju(int numNodes, List<List<Integer>> graph) {
        boolean[] visited = new boolean[numNodes];
        Stack<Integer> stack = new Stack<>();

        // Step 1: Do DFS and fill finish order
        for (int i = 0; i < numNodes; i++) {
            if (!visited[i]) {
                dfs1(i, visited, stack, graph);
            }
        }

        // Step 2: Reverse the graph
        List<List<Integer>> reversedGraph = reverseGraph(graph);

        // Step 3: DFS on reversed graph in stack order
        Arrays.fill(visited, false);
        List<List<Integer>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited[node]) {
                List<Integer> component = new ArrayList<>();
                dfs2(node, visited, component, reversedGraph);
                sccList.add(component);
            }
        }

        return sccList;
    }

    public static void main(String[] args) {
        int numNodes = 8;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            graph.add(new ArrayList<>());
        }

        // Example Graph
        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(0);
        graph.get(2).add(3);
        graph.get(3).add(4);
        graph.get(4).add(5);
        graph.get(5).add(6);
        graph.get(6).add(4);
        graph.get(6).add(7);
        graph.get(6).add(3);

        List<List<Integer>> sccs = kosaraju(numNodes, graph);
        System.out.println("Strongly Connected Components:");
        for (List<Integer> scc : sccs) {
            System.out.println(scc);
        }
    }
}


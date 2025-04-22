package graphs;

import java.util.*;

public class BridgeEdges {
    private int V, time;
    private List<List<Integer>> adj;
    private int[] disc, low;
    private List<List<Integer>> bridges;

    public BridgeEdges(int V) {
        this.V = V;
        this.adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        this.time = 0;
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u); // Undirected graph
    }

    public List<List<Integer>> findBridges() {
        disc = new int[V];
        low = new int[V];
        bridges = new ArrayList<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) {
                dfs(i, -1);
            }
        }

        return bridges;
    }

    private void dfs(int u, int parent) {
        disc[u] = low[u] = time++;

        for (int v : adj.get(u)) {
            if (v == parent) continue; // Don't go back through the parent

            if (disc[v] == -1) {
                dfs(v, u);
                low[u] = Math.min(low[u], low[v]);

                // Check if (u,v) is a bridge
                if (low[v] > disc[u]) {
                    bridges.add(Arrays.asList(u, v));
                }
            } else {
                // Back edge
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    public static void main(String[] args) {
        BridgeEdges g = new BridgeEdges(5);

        g.addEdge(1, 0);
        g.addEdge(0, 2);
        g.addEdge(2, 1);
        g.addEdge(0, 3);
        g.addEdge(3, 4);

        List<List<Integer>> bridges = g.findBridges();
        System.out.println("Bridge Edges:");
        for (List<Integer> bridge : bridges) {
            System.out.println(bridge.get(0) + " - " + bridge.get(1));
        }
    }
}


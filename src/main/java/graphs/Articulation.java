package graphs;

import java.util.*;

public class Articulation {
    private int V, time;
    private List<List<Integer>> adj;
    private int[] disc, low;
    private boolean[] visited, isArticulation;

    public Articulation(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        disc = new int[V];
        low = new int[V];
        visited = new boolean[V];
        isArticulation = new boolean[V];
        time = 0;
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u); // undirected graph
    }

    public List<Integer> findArticulationPoints() {
        for (int u = 0; u < V; u++) {
            if (!visited[u]) {
                dfs(u, -1);
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (isArticulation[i]) {
                result.add(i);
            }
        }
        return result;
    }

    private void dfs(int u, int parent) {
        visited[u] = true;
        disc[u] = low[u] = time++;
        int children = 0;

        for (int v : adj.get(u)) {
            if (v == parent) continue;

            if (!visited[v]) {
                children++;
                dfs(v, u);
                low[u] = Math.min(low[u], low[v]);

                // Case 1: root with two or more children
                if (parent == -1 && children > 1) {
                    isArticulation[u] = true;
                }

                // Case 2: non-root and low[v] >= disc[u]
                if (parent != -1 && low[v] >= disc[u]) {
                    isArticulation[u] = true;
                }
            } else {
                low[u] = Math.min(low[u], disc[v]); // back edge
            }
        }
    }

    public static void main(String[] args) {
        Articulation g = new Articulation(5);
        g.addEdge(1, 0);
        g.addEdge(0, 2);
        g.addEdge(2, 1);
        g.addEdge(0, 3);
        g.addEdge(3, 4);

        List<Integer> articulationPoints = g.findArticulationPoints();
        System.out.println("Articulation Points:");
        for (int point : articulationPoints) {
            System.out.println(point);
        }
    }
}


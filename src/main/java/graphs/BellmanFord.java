package graphs;

import java.util.*;

class BellmanFord {

    static class Edge {
        int src, dest, weight;
        Edge(int s, int d, int w) {
            src = s;
            dest = d;
            weight = w;
        }
    }

    public static void bellmanFord(int V, List<Edge> edges, int source) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Relax edges V - 1 times
        for (int i = 0; i < V - 1; i++) {
            for (Edge edge : edges) {
                int u = edge.src;
                int v = edge.dest;
                int w = edge.weight;
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        // Detect negative weight cycle
        for (Edge edge : edges) {
            if (dist[edge.src] != Integer.MAX_VALUE &&
                    dist[edge.src] + edge.weight < dist[edge.dest]) {
                System.out.println("Graph contains a negative weight cycle!");
                return;
            }
        }

        // Output result
        System.out.println("Shortest distances from source " + source + ":");
        for (int i = 0; i < V; i++) {
            System.out.println("To " + i + ": " + (dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]));
        }
    }

    public static void main(String[] args) {
        int V = 5;
        List<Edge> edges = new ArrayList<>();

        // Graph edges: u -> v (weight)
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(1, 2, -1));
        edges.add(new Edge(1, 3, 2));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(3, 4, 2));
        //edges.add(new Edge(4, 1, -5));  // Negative weight cycle if looped back

        bellmanFord(V, edges, 0);
    }
}


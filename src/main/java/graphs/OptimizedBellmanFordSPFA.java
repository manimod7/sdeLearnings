package graphs;

import java.util.*;

class Edge {
    int to, weight;
    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

public class OptimizedBellmanFordSPFA {
    static int INF = Integer.MAX_VALUE;

    public static int[] spfa(int V, List<List<Edge>> adj, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        boolean[] inQueue = new boolean[V];
        Queue<Integer> q = new LinkedList<>();
        q.offer(src);
        inQueue[src] = true;

        while (!q.isEmpty()) {
            int u = q.poll();
            inQueue[u] = false;

            for (Edge e : adj.get(u)) {
                if (dist[u] != INF && dist[e.to] > dist[u] + e.weight) {
                    dist[e.to] = dist[u] + e.weight;
                    if (!inQueue[e.to]) {
                        q.offer(e.to);
                        inQueue[e.to] = true;
                    }
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        int V = 5;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());

        adj.get(0).add(new Edge(1, -1));
        adj.get(0).add(new Edge(2, 4));
        adj.get(1).add(new Edge(2, 3));
        adj.get(1).add(new Edge(3, 2));
        adj.get(1).add(new Edge(4, 2));
        adj.get(3).add(new Edge(2, 5));
        adj.get(3).add(new Edge(1, 1));
        adj.get(4).add(new Edge(3, -3));

        int[] dist = spfa(V, adj, 0);

        System.out.println("Shortest distances from source:");
        for (int i = 0; i < V; i++) {
            System.out.println("0 → " + i + " = " + dist[i]);
        }
    }
}


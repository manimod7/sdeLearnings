package graphs;

import java.util.*;

class Dijkstra {

    static class Pair {
        int vertex, dist;
        Pair(int v, int d) {
            vertex = v;
            dist = d;
        }
    }

    public static int[] dijkstra(int V, List<List<Pair>> adj, int source) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.dist));
        pq.offer(new Pair(source, 0));

        while (!pq.isEmpty()) {
            Pair current = pq.poll();
            int u = current.vertex;
            int d = current.dist;

            // Skip if we've already found a shorter path
            if (d > dist[u]) continue;

            for (Pair neighbor : adj.get(u)) {
                int v = neighbor.vertex;
                int weight = neighbor.dist;

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new Pair(v, dist[v]));
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        int V = 5;
        List<List<Pair>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());

        // Graph edges: (u, v, weight)
        adj.get(0).add(new Pair(1, 2));
        adj.get(0).add(new Pair(4, 1));
        adj.get(1).add(new Pair(2, 3));
        adj.get(2).add(new Pair(3, 6));
        adj.get(4).add(new Pair(2, 2));
        adj.get(4).add(new Pair(1, 1));

        int[] dist = dijkstra(V, adj, 0);

        System.out.println("Shortest distances from source 0:");
        for (int i = 0; i < V; i++) {
            System.out.println("To " + i + ": " + dist[i]);
        }
    }
}


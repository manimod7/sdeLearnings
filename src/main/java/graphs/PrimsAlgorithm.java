package graphs;

import java.util.*;

public class PrimsAlgorithm {

    public static class Edge {
        int src;
        int dest;
        int weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + src + " -- " + weight + " -- " + dest + ")";
        }
    }

    public static List<Edge> primMST(int numVertices, List<List<Edge>> adj) {
        List<Edge> mstEdges = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        // Start with the first vertex (index 0)
        visited[0] = true;

        // Add all edges connected to the starting vertex to the min-heap
        for (Edge edge : adj.get(0)) {
            minHeap.offer(edge);
        }

        while (mstEdges.size() < numVertices - 1 && !minHeap.isEmpty()) {
            Edge currentEdge = minHeap.poll();
            int u = currentEdge.src;
            int v = currentEdge.dest;
            int weight = currentEdge.weight;

            // We might have added edges that lead to already visited nodes.
            // We only care about edges that connect a visited node to an unvisited one.
            int nextNode = -1;
            if (visited[u] && !visited[v]) {
                nextNode = v;
            } else if (!visited[u] && visited[v]) {
                nextNode = u;
            }

            // If we found an edge to an unvisited node, add it to the MST
            if (nextNode != -1) {
                mstEdges.add(currentEdge);
                visited[nextNode] = true;

                // Add all edges connected to the newly visited node to the min-heap
                for (Edge edge : adj.get(nextNode)) {
                    if (!visited[edge.dest] || !visited[edge.src]) {
                        minHeap.offer(edge);
                    }
                }
            }
        }

        return mstEdges;
    }

    public static void main(String[] args) {
        int numVertices = 5;
        List<List<Edge>> adj = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adj.add(new ArrayList<>());
        }

        // Example graph:
        adj.get(0).add(new Edge(0, 1, 2));
        adj.get(0).add(new Edge(0, 2, 6));
        adj.get(1).add(new Edge(1, 0, 2));
        adj.get(1).add(new Edge(1, 3, 3));
        adj.get(2).add(new Edge(2, 0, 6));
        adj.get(2).add(new Edge(2, 3, 8));
        adj.get(2).add(new Edge(2, 4, 5));
        adj.get(3).add(new Edge(3, 1, 3));
        adj.get(3).add(new Edge(3, 2, 8));
        adj.get(4).add(new Edge(4, 2, 5));

        List<Edge> mst = primMST(numVertices, adj);

        System.out.println("Edges in the Minimum Spanning Tree:");
        int totalWeight = 0;
        for (Edge edge : mst) {
            System.out.println(edge);
            totalWeight += edge.weight;
        }
        System.out.println("Total weight of MST: " + totalWeight);
    }
}

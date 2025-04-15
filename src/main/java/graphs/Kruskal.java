package graphs;

import java.util.*;

public class Kruskal {

    static class Edge implements Comparable<Edge> {
        int src, dest, weight;
        public Edge(int s, int d, int w) {
            src = s;
            dest = d;
            weight = w;
        }

        public int compareTo(Edge other) {
            return this.weight - other.weight; // ascending
        }
    }

    static class DSU {
        int[] parent, rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++)
                parent[i] = i;
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // path compression
            return parent[x];
        }

        public boolean union(int x, int y) {
            int px = find(x);
            int py = find(y);
            if (px == py) return false; // already in same set

            if (rank[px] < rank[py])
                parent[px] = py;
            else if (rank[px] > rank[py])
                parent[py] = px;
            else {
                parent[py] = px;
                rank[px]++;
            }
            return true;
        }
    }

    public static int kruskalMST(int V, List<Edge> edges) {
        Collections.sort(edges);
        DSU dsu = new DSU(V);
        int mstWeight = 0;
        int edgeCount = 0;

        for (Edge edge : edges) {
            if (dsu.union(edge.src, edge.dest)) {
                mstWeight += edge.weight;
                edgeCount++;
                System.out.println("Adding edge: " + edge.src + " - " + edge.dest + " : " + edge.weight);
                if (edgeCount == V - 1) break;
            }
        }

        return mstWeight;
    }

    public static void main(String[] args) {
        int V = 4;
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 10));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 3, 15));
        edges.add(new Edge(2, 3, 4));

        int totalWeight = kruskalMST(V, edges);
        System.out.println("Total weight of MST: " + totalWeight);
    }
}


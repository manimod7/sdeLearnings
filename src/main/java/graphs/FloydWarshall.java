package graphs;

public class FloydWarshall {
    static final int INF = 1000000000;

    public static void floydWarshall(int[][] graph, int V) {
        int[][] dist = new int[V][V];

        // Initialize distances
        for (int i = 0; i < V; i++)
            for (int j = 0; j < V; j++)
                dist[i][j] = graph[i][j];

        // Main DP logic
        for (int k = 0; k < V; k++)
            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (dist[i][k] < INF && dist[k][j] < INF)
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);

        // Print result
        System.out.println("All-pairs shortest distances:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.print((dist[i][j] == INF ? "INF" : dist[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int V = 4;
        int INF = 1000000000;
        int[][] graph = {
                {0, 5, INF, 10},
                {INF, 0, 3, INF},
                {INF, INF, 0, 1},
                {INF, INF, INF, 0}
        };

        floydWarshall(graph, V);
    }
}


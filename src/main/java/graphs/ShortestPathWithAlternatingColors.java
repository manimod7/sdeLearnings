package graphs;

import java.util.*;

public class ShortestPathWithAlternatingColors {
    private static final int RED = 0;
    private static final int BLUE = 1;

    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        // Adjacency lists by color
        List<Integer>[] redGraph = new ArrayList[n];
        List<Integer>[] blueGraph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            redGraph[i] = new ArrayList<>();
            blueGraph[i] = new ArrayList<>();
        }

        for (int[] edge : redEdges) {
            redGraph[edge[0]].add(edge[1]);
        }

        for (int[] edge : blueEdges) {
            blueGraph[edge[0]].add(edge[1]);
        }

        // Initialize result with -1
        int[] result = new int[n];
        Arrays.fill(result, -1);

        // visited[node][color] → if node visited with last edge color RED or BLUE
        boolean[][] visited = new boolean[n][2];

        // BFS queue: [node, lastColor]
        Queue<int[]> queue = new LinkedList<>();

        // Push both RED and BLUE starts from node 0
        queue.offer(new int[]{0, RED});
        queue.offer(new int[]{0, BLUE});
        visited[0][RED] = true;
        visited[0][BLUE] = true;

        int distance = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int node = current[0];
                int color = current[1];

                // First time reaching this node, set shortest path
                if (result[node] == -1) {
                    result[node] = distance;
                }

                // Alternate color for next hop
                if (color == RED) {
                    for (int neighbor : blueGraph[node]) {
                        if (!visited[neighbor][BLUE]) {
                            visited[neighbor][BLUE] = true;
                            queue.offer(new int[]{neighbor, BLUE});
                        }
                    }
                } else {
                    for (int neighbor : redGraph[node]) {
                        if (!visited[neighbor][RED]) {
                            visited[neighbor][RED] = true;
                            queue.offer(new int[]{neighbor, RED});
                        }
                    }
                }
            }

            distance++;
        }

        return result;
    }
}


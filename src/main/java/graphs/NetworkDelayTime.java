package graphs;

import java.util.*;

/**
 * LeetCode 743 - Network Delay Time
 *
 * Problem: You are given a network of n nodes, labeled 1 to n.
 * Each edge times[i] = (u, v, w) represents that the time taken
 * for a signal to travel from node u to node v is w. We send a
 * signal from a given source node k. Return the time it takes for
 * all the nodes to receive the signal. If it is impossible, return -1.
 *
 * Solution: Build a graph and use Dijkstra's algorithm starting from
 * the source node. The answer is the maximum distance in the resulting
 * distance array, provided all nodes are reachable.
 */
public class NetworkDelayTime {
    public int networkDelayTime(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] t : times) {
            graph.computeIfAbsent(t[0], x -> new ArrayList<>()).add(new int[]{t[1], t[2]});
        }
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{k, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];
            if (d > dist[u]) continue;
            List<int[]> edges = graph.getOrDefault(u, Collections.emptyList());
            for (int[] e : edges) {
                int v = e[0];
                int w = e[1];
                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1;
            ans = Math.max(ans, dist[i]);
        }
        return ans;
    }
}

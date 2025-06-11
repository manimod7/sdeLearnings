package graphs;

import java.util.*;

/**
 * LeetCode 332 - Reconstruct Itinerary
 *
 * Problem: Given a list of airline tickets where [from, to], reconstruct the
 * itinerary starting from "JFK" that uses all tickets exactly once and is
 * lexicographically smallest.
 *
 * Solution: Hierholzer's algorithm (DFS) for Eulerian path using a priority
 * queue to maintain lexical order.
 */
public class ReconstructItinerary {
    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> t : tickets) {
            graph.computeIfAbsent(t.get(0), x -> new PriorityQueue<>()).offer(t.get(1));
        }
        LinkedList<String> res = new LinkedList<>();
        dfs("JFK", graph, res);
        return res;
    }
    private void dfs(String from, Map<String, PriorityQueue<String>> g, LinkedList<String> res) {
        PriorityQueue<String> pq = g.get(from);
        while (pq != null && !pq.isEmpty()) {
            dfs(pq.poll(), g, res);
        }
        res.addFirst(from);
    }
}

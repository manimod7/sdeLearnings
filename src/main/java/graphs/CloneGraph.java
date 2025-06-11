package graphs;

import java.util.*;

/**
 * LeetCode 133 - Clone Graph
 *
 * Problem: Given a reference of a node in a connected undirected graph,
 * return a deep copy (clone) of the graph. Each node contains a value
 * and a list of its neighbors.
 *
 * Solution: Use BFS (or DFS) starting from the given node and
 * create a cloned node for every original node. Maintain a map from
 * original nodes to their clones to avoid creating duplicates.
 */
public class CloneGraph {
    static class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    public Node cloneGraph(Node node) {
        if (node == null) return null;
        Map<Node, Node> map = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);
        map.put(node, new Node(node.val));

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Node nei : curr.neighbors) {
                if (!map.containsKey(nei)) {
                    map.put(nei, new Node(nei.val));
                    queue.offer(nei);
                }
                map.get(curr).neighbors.add(map.get(nei));
            }
        }
        return map.get(node);
    }
}

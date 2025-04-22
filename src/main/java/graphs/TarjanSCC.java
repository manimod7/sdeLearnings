package graphs;

import java.util.*;

public class TarjanSCC {
    static final int V = 7;
    static Map<Integer, List<Integer>> adj = new HashMap<>();
    static int time = 0;

    public static void DFS(int u, int[] disc, int[] low, Stack<Integer> stack, boolean[] presentInStack) {
        disc[u] = low[u] = time++;
        stack.push(u);
        presentInStack[u] = true;

        for (int v : adj.getOrDefault(u, new ArrayList<>())) {
            if (disc[v] == -1) {
                DFS(v, disc, low, stack, presentInStack);
                low[u] = Math.min(low[u], low[v]);
            } else if (presentInStack[v]) { // back-edge
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            System.out.print("SCC is: ");
            while (stack.peek() != u) {
                int top = stack.pop();
                System.out.print(top + " ");
                presentInStack[top] = false;
            }
            int top = stack.pop();
            System.out.println(top);
            presentInStack[top] = false;
        }
    }

    public static void findSCCsTarjan() {
        int[] disc = new int[V];
        int[] low = new int[V];
        boolean[] presentInStack = new boolean[V];
        Stack<Integer> stack = new Stack<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) {
                DFS(i, disc, low, stack, presentInStack);
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the graph
        for (int i = 0; i < V; i++) {
            adj.put(i, new ArrayList<>());
        }

        adj.get(0).add(1);
        adj.get(1).add(2);
        adj.get(1).add(3);
        adj.get(3).add(4);
        adj.get(4).add(0);
        adj.get(4).add(5);
        adj.get(4).add(6);
        adj.get(5).add(6);
        adj.get(6).add(5);

        findSCCsTarjan();
    }
}


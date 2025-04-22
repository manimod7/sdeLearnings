package graphs;

import java.util.*;

public class Tarjan {
    private final int V;
    private final List<List<Integer>> adj;
    private int time;
    private int[] disc, low;
    private boolean[] inStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccList;

    public Tarjan(int vertices) {
        this.V = vertices;
        this.adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    public List<List<Integer>> findSCCs() {
        time = 0;
        disc = new int[V];
        low = new int[V];
        inStack = new boolean[V];
        stack = new Stack<>();
        sccList = new ArrayList<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) {
                dfs(i);
            }
        }
        return sccList;
    }

    private void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        inStack[u] = true;

        for (int v : adj.get(u)) {
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // If u is head of an SCC
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            while (stack.peek() != u) {
                int node = stack.pop();
                inStack[node] = false;
                scc.add(node);
            }
            int node = stack.pop();
            inStack[node] = false;
            scc.add(node);
            sccList.add(scc);
        }
    }

    public static void main(String[] args) {
        Tarjan g = new Tarjan(7);

        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 0);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        g.addEdge(5, 6);
        g.addEdge(6, 5);

        List<List<Integer>> sccs = g.findSCCs();

        System.out.println("Strongly Connected Components:");
        for (List<Integer> scc : sccs) {
            System.out.println(scc);
        }
    }
}


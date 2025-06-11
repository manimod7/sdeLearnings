package graphs;

import java.util.*;

/**
 * LeetCode 1202 - Smallest String With Swaps
 *
 * Problem: Given a string s and a list of index pairs allowed to swap, return
 * the lexicographically smallest string after performing any number of swaps.
 *
 * Solution: Treat pairs as edges of an undirected graph. Connected components
 * can have their characters sorted independently.
 */
public class SmallestStringWithSwaps {
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        UnionFind uf = new UnionFind(n);
        for (List<Integer> p : pairs) uf.union(p.get(0), p.get(1));
        Map<Integer, PriorityQueue<Character>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = uf.find(i);
            map.computeIfAbsent(root, x -> new PriorityQueue<>()).offer(s.charAt(i));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(map.get(uf.find(i)).poll());
        }
        return sb.toString();
    }
    static class UnionFind {
        int[] parent;
        UnionFind(int n){parent=new int[n]; for(int i=0;i<n;i++) parent[i]=i;}
        int find(int x){return parent[x]==x?x:(parent[x]=find(parent[x]));}
        void union(int a,int b){parent[find(a)] = find(b);}    }
}

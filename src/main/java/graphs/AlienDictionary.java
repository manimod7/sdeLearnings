package graphs;

import java.util.*;

/**
 * LeetCode 269 - Alien Dictionary
 *
 * Problem: Given a list of words sorted lexicographically by an unknown alien
 * language, derive the order of characters in that language.
 *
 * Solution: Build a directed graph of precedence edges between adjacent words
 * and perform topological sort (Kahn's algorithm) to produce the ordering.
 */
public class AlienDictionary {
    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> indeg = new HashMap<>();
        for (String w : words) for (char c : w.toCharArray()) {
            graph.putIfAbsent(c, new HashSet<>()); indeg.putIfAbsent(c, 0);
        }
        for (int i = 0; i < words.length - 1; i++) {
            String a = words[i], b = words[i+1];
            int len = Math.min(a.length(), b.length());
            for (int j = 0; j < len; j++) {
                char ca = a.charAt(j), cb = b.charAt(j);
                if (ca != cb) {
                    if (graph.get(ca).add(cb)) indeg.put(cb, indeg.get(cb)+1);
                    break;
                }
                if (j==len-1 && a.length()>b.length()) return "";
            }
        }
        Queue<Character> q = new ArrayDeque<>();
        for (char c : indeg.keySet()) if (indeg.get(c)==0) q.offer(c);
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll(); sb.append(c);
            for (char nei : graph.get(c)) {
                indeg.put(nei, indeg.get(nei)-1);
                if (indeg.get(nei)==0) q.offer(nei);
            }
        }
        return sb.length()==indeg.size()? sb.toString() : "";
    }
}

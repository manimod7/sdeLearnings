package graphs;

import java.util.*;

/**
 * LeetCode 127 - Word Ladder
 *
 * Problem: Given beginWord, endWord and a word list, find the length of the
 * shortest transformation sequence to endWord where each step changes one letter
 * and each intermediate word must exist in the word list.
 *
 * Solution: BFS from beginWord generating all valid next words by changing one
 * letter at a time.
 */
public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return 0;
        Queue<String> q = new ArrayDeque<>();
        q.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        int steps = 1;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int s = 0; s < size; s++) {
                String word = q.poll();
                if (word.equals(endWord)) return steps;
                char[] arr = word.toCharArray();
                for (int i = 0; i < arr.length; i++) {
                    char orig = arr[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        arr[i] = c;
                        String next = new String(arr);
                        if (dict.contains(next) && !visited.contains(next)) {
                            visited.add(next); q.offer(next);
                        }
                    }
                    arr[i] = orig;
                }
            }
            steps++;
        }
        return 0;
    }
}

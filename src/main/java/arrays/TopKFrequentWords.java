package arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKFrequentWords {
    public List<String> topKFrequent(String[] words, int k) {
        // Step 1: Count frequencies
        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }

        // Step 2: Sort the words
        List<String> candidates = new ArrayList<>(count.keySet());
        candidates.sort((a, b) -> {
            if (count.get(a).equals(count.get(b))) {
                return a.compareTo(b);  // Alphabetical order
            } else {
                return count.get(b) - count.get(a);  // Higher frequency first
            }
        });

        // Step 3: Return top k
        return candidates.subList(0, k);
    }
}

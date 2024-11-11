package Sorting;

import java.util.*;

public class TopKFrequentWords {

    public List<String> topKFrequent(String[] words, int k) {

        Map<String,Integer> map = new HashMap<>();
        List<String> uniqueElements = new ArrayList<>();

        for (String word : words) {
            if (map.get(word) == null) {
                uniqueElements.add(word);
            }
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        uniqueElements.sort((a, b) -> {
            if (!Objects.equals(map.get(a), map.get(b)))
                return map.get(b) - map.get(a);
            else {
                return a.compareTo(b);
            }
        });
        return uniqueElements.subList(0,k);

    }
}

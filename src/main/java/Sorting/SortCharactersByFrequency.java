package Sorting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SortCharactersByFrequency {

    public String frequencySort(String s) {

        // Step 1: Build frequency map
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Step 2: Convert String to Character array for sorting
        Character[] sCharArray = new Character[s.length()];
        for (int i = 0; i < s.length(); i++) {
            sCharArray[i] = s.charAt(i);
        }

        // Step 3: Sort the array by frequency in descending order
        Arrays.sort(sCharArray, (a, b) -> {
            int freqA = frequencyMap.get(a);
            int freqB = frequencyMap.get(b);

            // If frequencies are equal, maintain original order
            if (freqA == freqB) {
                return a - b;  // Maintain order by lexicographical comparison for stability
            }

            // Sort by frequency in descending order
            return freqB - freqA;
        });

        // Step 4: Build result string from sorted characters
        StringBuilder result = new StringBuilder();
        for (char c : sCharArray) {
            result.append(c);
        }

        return result.toString();
    }
}

package Sorting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomSortString {
    public String customSortString(String order, String s) {
        // Map each character in 'order' to its priority
        Map<Character, Integer> priorityMap = new HashMap<>();
        int priority = 0;
        for (Character c : order.toCharArray()) {
            priorityMap.put(c, priority++);
        }

        // Convert 's' to a character array to sort it
        Character[] sChars = new Character[s.length()];
        for (int i = 0; i < s.length(); i++) {
            sChars[i] = s.charAt(i);
        }

        // Sort characters in 's' using custom comparator
        Arrays.sort(sChars, (a, b) -> {
            // Use the priority in the map, default to Integer.MAX_VALUE for characters not in 'order'
            int priorityA = priorityMap.getOrDefault(a, Integer.MAX_VALUE);
            int priorityB = priorityMap.getOrDefault(b, Integer.MAX_VALUE);
            return priorityA - priorityB;
        });

        // Convert sorted characters back to a string
        StringBuilder result = new StringBuilder();
        for (char c : sChars) {
            result.append(c);
        }

        return result.toString();
    }
}

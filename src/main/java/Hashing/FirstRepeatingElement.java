package Hashing;

import java.util.HashSet;
import java.util.Set;

public class FirstRepeatingElement {
    Set<Integer> set = new HashSet<>();

    public int solve(int[] A) {
        for (int j : A) {
            if (set.contains(j)) {
                return j;
            } else {
                set.add(j);
            }
        }
        return -1; // Return -1 if no repeating element is found
    }
}

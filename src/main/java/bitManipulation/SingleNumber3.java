package bitManipulation;

import java.util.ArrayList;
import java.util.List;

public class SingleNumber3 {
    public List<Integer> solve(List<Integer> numbers) {
        // Initial XOR to find XOR of two unique numbers as all others cancel each other out
        int xorOfTwoUniques = 0;
        for (int num : numbers) {
            xorOfTwoUniques ^= num;
        }
        // Find the rightmost set bit in xorOfTwoUniques (indicative of differing bit in the two unique numbers)
        int setBitIndicator = xorOfTwoUniques & (-xorOfTwoUniques);
        int unique1 = 0, unique2 = 0;
        for (int num : numbers) {
            if ((num & setBitIndicator) == 0) {
                unique1 ^= num; // Group where bit is not set
            } else {
                unique2 ^= num; // Group where bit is set
            }
        }
        ArrayList<Integer> result = new ArrayList<>(2);
        result.add(Math.min(unique1, unique2));
        result.add(Math.max(unique1, unique2));
        return result;
    }

}

package Hashing;

import java.util.ArrayList;
import java.util.HashSet;

public class SubArrayWithSumZero {
    public int solve(ArrayList<Integer> A) {
        // Just write your code below to complete the function. Required input is available to you as the function arguments.
        // Do not print the result or any output. Just return the result via this function.
        long sum = 0;
        HashSet<Long> seen = new HashSet<>();

        for (int x : A) {
            sum += x;

            if (sum == 0) {
                return 1;
            }

            if (seen.contains(sum)) {
                return 1;
            }
            seen.add(sum);
        }

        return 0;
    }
}

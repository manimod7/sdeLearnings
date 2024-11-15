package maths;

import java.util.ArrayList;

public class MaxGCD {
    // Method to calculate the GCD of two numbers
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public int solve(ArrayList<Integer> A) {
        int n = A.size();

        // Edge case: if there are only two elements, removing any one will leave GCD as the other one
        if (n == 2) return Math.max(A.get(0), A.get(1));

        int[] prefixGcd = new int[n];
        int[] suffixGcd = new int[n];

        // Initialize prefix and suffix arrays
        prefixGcd[0] = A.get(0);
        for (int i = 1; i < n; i++) {
            prefixGcd[i] = gcd(prefixGcd[i - 1], A.get(i));
        }

        suffixGcd[n - 1] = A.get(n - 1);
        for (int i = n - 2; i >= 0; i--) {
            suffixGcd[i] = gcd(suffixGcd[i + 1], A.get(i));
        }

        // Compute the maximum GCD possible after removing one element
        int maxGcd = suffixGcd[1];  // Max GCD when the first element is removed
        maxGcd = Math.max(maxGcd, prefixGcd[n - 2]);  // Max GCD when the last element is removed

        for (int i = 1; i < n - 1; i++) {
            int currentGcd = gcd(prefixGcd[i - 1], suffixGcd[i + 1]);
            maxGcd = Math.max(maxGcd, currentGcd);
        }

        return maxGcd;
    }
}

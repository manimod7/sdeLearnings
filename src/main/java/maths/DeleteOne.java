package maths;

import java.util.ArrayList;

public class DeleteOne {
    public int solve(ArrayList<Integer> A) {
        int n = A.size();
        if (n == 2) {
            // If you remove either element, only one remains, so GCD = that element
            return Math.max(A.get(0), A.get(1));
        }

        // build prefix GCDs
        int[] prefix = new int[n];
        prefix[0] = A.get(0);
        for (int i = 1; i < n; i++) {
            prefix[i] = gcd(prefix[i - 1], A.get(i));
        }

        // build suffix GCDs
        int[] suffix = new int[n];
        suffix[n - 1] = A.get(n - 1);
        for (int i = n - 2; i >= 0; i--) {
            suffix[i] = gcd(suffix[i + 1], A.get(i));
        }

        // consider removing each element
        int maxG = suffix[1];              // remove index 0
        maxG = Math.max(maxG, prefix[n-2]); // remove index n-1

        for (int i = 1; i < n - 1; i++) {
            // GCD of everything except A[i]
            int g = gcd(prefix[i - 1], suffix[i + 1]);
            if (g > maxG) maxG = g;
        }

        return maxG;
    }

    // Euclidean algorithm
    private int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return Math.abs(a);
    }
}

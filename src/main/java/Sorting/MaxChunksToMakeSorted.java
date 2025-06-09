package Sorting;

import java.util.ArrayList;

public class MaxChunksToMakeSorted {
    public int solve(ArrayList<Integer> A) {
        int chunks    = 0;  // count of valid cuts
        int maxSoFar  = 0;  // max element seen up to current index

        for (int i = 0; i < A.size(); i++) {
            maxSoFar = Math.max(maxSoFar, A.get(i));
            // Whenever the max equals the index,
            // we know A[0..i] == {0,1,...,i}
            if (maxSoFar == i) {
                chunks++;
            }
        }
        return chunks;
    }
}

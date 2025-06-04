package arrays;

import java.util.ArrayList;

public class RangeSumBeggars {

    public ArrayList<Integer> solve(int A, ArrayList<ArrayList<Integer>> B) {
        // difference array
        long[] diff = new long[A];
        for (ArrayList<Integer> donation : B) {
            int L = donation.get(0);
            int R = donation.get(1);
            int P = donation.get(2);
            // add P at L–1
            diff[L - 1] += P;
            // subtract P right after R, if in bounds
            if (R < A) {
                diff[R] -= P;
            }
        }
        // build final pots via prefix sum
        ArrayList<Integer> result = new ArrayList<>(A);
        long running = 0;
        for (int i = 0; i < A; i++) {
            running += diff[i];
            result.add((int)running);
        }
        return result;
    }
}

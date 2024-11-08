package arrays;
import java.util.ArrayList;

public class RangeSumQuery {
    public ArrayList<Long> rangeSum(ArrayList<Integer> A, ArrayList<ArrayList<Integer>> B) {
        int N = A.size();
        ArrayList<Long> result = new ArrayList<>();

        // Step 1: Calculate the prefix sum array
        long[] prefixSum = new long[N];
        prefixSum[0] = A.get(0);
        for (int i = 1; i < N; i++) {
            prefixSum[i] = prefixSum[i - 1] + A.get(i);
        }

        // Step 2: Process each query [L, R]
        for (ArrayList<Integer> query : B) {
            int L = query.get(0);
            int R = query.get(1);

            // Calculate the sum for the current query
            long sum = prefixSum[R];
            if (L > 0) {
                sum -= prefixSum[L - 1];
            }

            // Add the sum to the result list
            result.add(sum);
        }

        return result;
    }
}

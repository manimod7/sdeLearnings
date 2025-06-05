package slidingWindow;

import java.util.ArrayList;

public class SubArrayWithGivenSum {
    public ArrayList<Integer> solve(ArrayList<Integer> A, int B) {
        ArrayList<Integer> result = new ArrayList<>();
        int n = A.size();
        long currentSum = 0;
        int start = 0;

        for (int end = 0; end < n; end++) {
            // Add the new element into the window
            currentSum += A.get(end);

            // While sum exceeds B, shrink window from the left
            while (currentSum > B && start <= end) {
                currentSum -= A.get(start);
                start++;
            }

            // If we hit exactly B, build the answer list from start..end
            if (currentSum == B) {
                for (int idx = start; idx <= end; idx++) {
                    result.add(A.get(idx));
                }
                return result;
            }
        }

        // If no subarray matched, return [-1]
        result.clear();
        result.add(-1);
        return result;
    }
}

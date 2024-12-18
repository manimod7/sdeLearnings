package arrays;

import java.util.ArrayList;
import java.util.Collections;

public class AddOrNot {
    public ArrayList<Integer> solve(ArrayList<Integer> A, int B) {
        // Sort the array
        Collections.sort(A);
        int n = A.size();

        // Sliding window variables
        int maxFrequency = 1;
        int minValue = A.get(0);
        long sum = 0;
        int start = 0;

        for (int end = 0; end < n; end++) {
            // Update the sum for the current window
            sum += A.get(end);

            // Calculate the cost to make all elements in the window equal to A[end]
            while ((long) A.get(end) * (end - start + 1) - sum > B) {
                // If the cost exceeds B, shrink the window from the left
                sum -= A.get(start);
                start++;
            }

            // Update the maximum frequency and corresponding value
            int frequency = end - start + 1;
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                minValue = A.get(end);
            } else if (frequency == maxFrequency) {
                minValue = Math.min(minValue, A.get(end));
            }
        }

        // Prepare the result
        ArrayList<Integer> result = new ArrayList<>();
        result.add(maxFrequency);
        result.add(minValue);
        return result;
    }
}

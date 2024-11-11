package slidingWindow;

import java.util.ArrayList;
import java.util.Collections;

public class AddOrNot {
    //Given an array of integers A of size N and an integer B.
    //
    //In a single operation, any one element of the array can be increased by 1. You are allowed to do at most B such operations.
    //
    //Find the number with the maximum number of occurrences and return an array C of size 2, where C[0] is the number of occurrences, and C[1] is the number with maximum occurrence.
    //If there are several such numbers, your task is to find the minimum one.

    public ArrayList<Integer> solve(ArrayList<Integer> A, int B) {
        // Sort the array
        Collections.sort(A);

        int n = A.size();
        int maxFrequency = 1;  // At least one element will have a frequency of 1
        int maxFrequencyNumber = A.get(0); // Initial number with maximum frequency

        long currentSum = 0; // Sum of elements in the sliding window
        int left = 0; // Left pointer of the window

        // Sliding window
        for (int right = 0; right < n; right++) {
            currentSum += A.get(right);

            // Calculate the cost to make all elements in the window equal to A[right]
            long targetSum = (long) A.get(right) * (right - left + 1);
            long cost = targetSum - currentSum;

            // If the cost exceeds B, slide the window from the left
            while (cost > B) {
                currentSum -= A.get(left);
                left++;
                targetSum = (long) A.get(right) * (right - left + 1);
                cost = targetSum - currentSum;
            }

            // Update maxFrequency and maxFrequencyNumber if we found a better solution
            int frequency = right - left + 1;
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                maxFrequencyNumber = A.get(right);
            }
        }

        // Prepare the result as required
        ArrayList<Integer> result = new ArrayList<>();
        result.add(maxFrequency);
        result.add(maxFrequencyNumber);

        return result;
    }
}

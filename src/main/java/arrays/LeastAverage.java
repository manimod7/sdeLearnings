package arrays;

import java.util.ArrayList;

public class LeastAverage {
    public int solve(ArrayList<Integer> A, int B) {

        int minSum;
        int currWindowSum = 0;
        int n = A.size();
        int startIndex = 0; // This will hold the starting index of the subarray with the least average

        // Calculate the sum of the first B elements
        for (int i = 0; i < B; i++) {
            currWindowSum += A.get(i);
        }

        // Initialize minSum with the sum of the first window
        minSum = currWindowSum;

        // Traverse the array starting from the Bth element
        for (int i = B; i < n; i++) {
            // Slide the window forward by one, adding the next element and removing the first element of the current window
            currWindowSum = currWindowSum + A.get(i) - A.get(i - B);

            // Update minSum and startIndex if a new minimum is found
            if (currWindowSum < minSum) {
                minSum = currWindowSum;
                startIndex = i - B + 1; // Update to the new start index of the window with the least sum
            }
        }

        return startIndex;

    }
}

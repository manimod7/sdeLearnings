package arrays;
import java.util.*;
public class PickFromBothSides {
    public int solve(ArrayList<Integer> A, int B) {
        int n = A.size();
        int totalSum = 0;

        // Calculate the total sum of the array
        for (int i = 0; i < n; i++) {
            totalSum += A.get(i);
        }

        // If B is equal to the size of the array, simply return the total sum
        if (B == n) {
            return totalSum;
        }
        // Find the sum of the first window of size n-B
        int sumOfWindow = 0;
        for (int i = 0; i < n - B; i++) {
            sumOfWindow += A.get(i);
        }

        int minSumOfWindow = sumOfWindow;
        // Slide the window of size n-B across the array to find the minimum sum of such window
        for (int i = 1; i <= B; i++) {
            sumOfWindow = sumOfWindow - A.get(i - 1) + A.get(i + n - B - 1);
            minSumOfWindow = Math.min(minSumOfWindow, sumOfWindow);
        }

        // The result is the total sum minus the minimum sum of the window of size n-B
        return totalSum - minSumOfWindow;
    }

}

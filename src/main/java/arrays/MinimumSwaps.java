package arrays;

import java.util.ArrayList;

public class MinimumSwaps {
    public int solve(ArrayList<Integer> A, int B) {
        int numberOfElementsLessThanB = 0;
        int n = A.size();
        for (Integer integer : A) {
            if (integer <= B) // <= B, as per problem statement
                numberOfElementsLessThanB++;
        }

        if(numberOfElementsLessThanB == n || numberOfElementsLessThanB == 0)
            return 0;

        int numberOfElementsLessThanBInCurrentWindow = 0;
        for(int i = 0; i < numberOfElementsLessThanB; i++) {
            if(A.get(i) <= B)
                numberOfElementsLessThanBInCurrentWindow++;
        }

        int ans = numberOfElementsLessThanBInCurrentWindow; // This represents the max in the first window
        for(int i = numberOfElementsLessThanB; i < n; i++) {
            if(A.get(i - numberOfElementsLessThanB) <= B)
                numberOfElementsLessThanBInCurrentWindow--;
            if(A.get(i) <= B)
                numberOfElementsLessThanBInCurrentWindow++;

            ans = Math.max(ans, numberOfElementsLessThanBInCurrentWindow);
        }
        // The minimum swaps needed is count - max found in any window
        return numberOfElementsLessThanB - ans;
    }

}

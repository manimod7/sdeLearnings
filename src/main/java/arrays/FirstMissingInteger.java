package arrays;

import java.util.ArrayList;

public class FirstMissingInteger {
    public int firstMissingPositive(ArrayList<Integer> A) {
        int n = A.size();
        for (int i = 0; i < n; ) {
            int correctPos = A.get(i) - 1;
            if (A.get(i) > 0 && A.get(i) <= n && !A.get(i).equals(A.get(correctPos))) {
                // Swap to place A.get(i) in its correct position
                int temp = A.get(correctPos);
                A.set(correctPos, A.get(i));
                A.set(i, temp);
            } else {
                // Move to the next element only if no swap is needed
                i++;
            }
        }
        for (int i = 0; i < n; i++) {
            if (!A.get(i).equals(i + 1))
                return i + 1;
        }
        return n + 1;
    }

}

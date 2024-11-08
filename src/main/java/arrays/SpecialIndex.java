package arrays;

import java.util.ArrayList;

public class SpecialIndex {
    public int solve(ArrayList<Integer> A) {
        int n = A.size();
        ArrayList<Integer> evenPrefix = new ArrayList<>();
        ArrayList<Integer> oddPrefix = new ArrayList<>();
        ArrayList<Integer> evenSuffix = new ArrayList<>();
        ArrayList<Integer> oddSuffix = new ArrayList<>();

        evenPrefix.add(0);
        oddPrefix.add(0);
        for (int i = 0; i < n; i++) {
            evenPrefix.add(evenPrefix.get(i) + (i % 2 == 0 ? A.get(i) : 0));
            oddPrefix.add(oddPrefix.get(i) + (i % 2 != 0 ? A.get(i) : 0));
        }

        evenSuffix.add(0); // Initial fill to ensure size is n + 1
        oddSuffix.add(0);
        for (int i = 0; i < n + 1; i++) {
            evenSuffix.add(0);
            oddSuffix.add(0);
        }

        for (int i = n - 1; i >= 0; i--) {
            evenSuffix.set(i, evenSuffix.get(i + 1) + (i % 2 == 0 ? A.get(i) : 0));
            oddSuffix.set(i, oddSuffix.get(i + 1) + (i % 2 != 0 ? A.get(i) : 0));
        }

        int count = 0;
        for (int i = 0; i < n; i++) {
            int evenSum = evenPrefix.get(i) + oddSuffix.get(i + 1);
            int oddSum = oddPrefix.get(i) + evenSuffix.get(i + 1);
            if (evenSum == oddSum) {
                count++;
            }
        }

        return count;
    }

}

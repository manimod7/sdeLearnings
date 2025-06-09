package Sorting;

import java.util.ArrayList;

public class Sort01 {
    public ArrayList<Integer> sort01(ArrayList<Integer> A) {
        int left = 0, right = A.size() - 1;

        while (left < right) {
            // advance left until we see a 1
            while (left < right && A.get(left) == 0) {
                left++;
            }
            // move right down until we see a 0
            while (left < right && A.get(right) == 1) {
                right--;
            }
            // now A[left] == 1 and A[right] == 0, so swap them
            if (left < right) {
                A.set(left, 0);
                A.set(right, 1);
                left++;
                right--;
            }
        }
        return A;
    }
}

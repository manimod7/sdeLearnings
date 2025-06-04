package Sorting;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public ArrayList<Integer> solve(ArrayList<Integer> A) {
        if (A == null || A.size() <= 1) {
            return A;
        }
        mergeSort(A, 0, A.size() - 1);
        return A;
    }

    private void mergeSort(List<Integer> A, int left, int right) {
        if (left >= right) {
            return; // Single element is already sorted
        }
        int mid = left + (right - left) / 2;
        mergeSort(A, left, mid);
        mergeSort(A, mid + 1, right);
        merge(A, left, mid, right);
    }

    private void merge(List<Integer> A, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Copy left half into temp1, right half into temp2
        int[] temp1 = new int[n1];
        int[] temp2 = new int[n2];
        for (int i = 0; i < n1; i++) {
            temp1[i] = A.get(left + i);
        }
        for (int j = 0; j < n2; j++) {
            temp2[j] = A.get(mid + 1 + j);
        }

        int i = 0, j = 0, k = left;
        // Merge back into A[left..right]
        while (i < n1 && j < n2) {
            if (temp1[i] <= temp2[j]) {
                A.set(k++, temp1[i++]);
            } else {
                A.set(k++, temp2[j++]);
            }
        }
        // Copy any remaining elements from temp1
        while (i < n1) {
            A.set(k++, temp1[i++]);
        }
        // Copy any remaining elements from temp2
        while (j < n2) {
            A.set(k++, temp2[j++]);
        }
    }
}

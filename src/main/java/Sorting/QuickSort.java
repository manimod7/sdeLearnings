package Sorting;

import java.util.ArrayList;

public class QuickSort {
    public ArrayList<Integer> solve(ArrayList<Integer> A) {
        if (A == null || A.size() <= 1) {
            return A;
        }
        quickSort(A, 0, A.size() - 1);
        return A;
    }

    private void quickSort(ArrayList<Integer> arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private int partition(ArrayList<Integer> arr, int low, int high) {
        int pivot = arr.get(high);
        int i = low - 1; // place for swapping

        for (int j = low; j < high; j++) {
            if (arr.get(j) <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        // place pivot in its correct sorted position
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(ArrayList<Integer> arr, int i, int j) {
        int tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }
}

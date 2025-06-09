package Sorting;

import java.util.ArrayList;

public class PartitionIndex {

    public int partition(ArrayList<Integer> A) {
        // Code Here
        if (A == null || A.size() <= 1) {
            return 0;
        }
        return quickSort(A, 0, A.size() - 1);
    }

    private int quickSort(ArrayList<Integer> arr, int low, int high) {

        int pivot = partition(arr, low, high);
        //quickSort(arr, low, pivotIndex - 1);
        //quickSort(arr, pivotIndex + 1, high);
        return pivot;

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

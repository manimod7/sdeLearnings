package Sorting;

import java.util.ArrayList;
import java.util.Collections;

public class CountSort {
    public static int[] countSort(int[] inputArray) {
        int n = inputArray.length;
        int max = 0;

        for (int j : inputArray) {
            max = Math.max(max, j);
        }

        int[] countArray = new int[max + 1];

        for (int i = 0; i <= max; i++) {
            countArray[inputArray[i]]++;
        }
        ArrayList <Integer> arr = new ArrayList<Integer>();
        Collections.sort(arr);

        int[] outputArray = new int[n];
        int cnt = 0;
        for(int i=0;i<n;i++) {
            while(countArray[i]>0 && cnt<n) {
                outputArray[cnt++] = inputArray[i];
            }
        }

        return outputArray;
    }

    public static void main(String[] args) {
        int[] inputArray = {4, 3, 12, 1, 5, 5, 3, 9};
        int[] outputArray = countSort(inputArray);

        for (int i = 0; i < inputArray.length; i++) {
            System.out.print(outputArray[i] + " ");
        }
        ArrayList<Integer> arr = new ArrayList<>();
    }
}
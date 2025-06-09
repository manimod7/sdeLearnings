package Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LargestNumber {
    public String largestNumber(ArrayList<Integer> A) {
        // Convert the integers to strings
        ArrayList<String> strNums = new ArrayList<>();
        for (Integer num : A) {
            strNums.add(String.valueOf(num));
        }

        // Define a custom comparator for sorting
        strNums.sort((a, b) -> {
            // Compare the concatenated strings in both possible orders
            String order1 = a + b;
            String order2 = b + a;
            return order2.compareTo(order1); // Sort in descending order
        });

        // Edge case: If the largest number is "0", return "0"
        if (!strNums.isEmpty() && strNums.get(0).equals("0")) {
            return "0";
        }
        // Build the largest number by concatenating the sorted strings
        StringBuilder largestNumber = new StringBuilder();
        for (String num : strNums) {
            largestNumber.append(num);
        }
        return largestNumber.toString();

    }

    public String largestNumber2(int[] nums) {
        // Convert int[] to String[] for custom sorting
        String[] numStrs = Arrays.stream(nums).mapToObj(String::valueOf).toArray(String[]::new);

        // Sort with custom comparator
        Arrays.sort(numStrs, (a, b) -> (b + a).compareTo(a + b));

        // Edge case: if the largest number is "0", return "0"
        if (numStrs[0].equals("0")) {
            return "0";
        }

        // Build the result from the sorted array
        StringBuilder result = new StringBuilder();
        for (String numStr : numStrs) {
            result.append(numStr);
        }

        return result.toString();
    }

    //Method 2 -> Quick Sort


    public ArrayList<Integer> solve(ArrayList<Integer> A) {
        if (A == null || A.size() <= 1) return A;
        quickSort(A, 0, A.size() - 1);
        return A;
    }

    private void quickSort(ArrayList<Integer> arr, int low, int high) {
        if (low < high) {
            int p = partition(arr, low, high);
            quickSort(arr, low, p - 1);
            quickSort(arr, p + 1, high);
        }
    }

    private int partition(ArrayList<Integer> arr, int low, int high) {
        int pivot = arr.get(high);
        int pivotCount = countFactors(pivot);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            int v = arr.get(j);
            int vCount = countFactors(v);
            // compare (vCount, v) vs (pivotCount, pivot)
            if (vCount < pivotCount || (vCount == pivotCount && v < pivot)) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(ArrayList<Integer> arr, int i, int j) {
        int tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }

    // Counts distinct divisors of x in O(sqrt(x))
    private int countFactors(int x) {
        int cnt = 0;
        int root = (int) Math.sqrt(x);
        for (int d = 1; d <= root; d++) {
            if (x % d == 0) {
                if (d * d == x) {
                    cnt += 1;
                } else {
                    cnt += 2;
                }
            }
        }
        return cnt;
    }
}

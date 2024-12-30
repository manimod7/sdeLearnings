package twoPointers;

public class SegregateEvenAndOdd {
    public static void segregateEvenOdd(int[] arr) {
        int lo = 0, hi = arr.length - 1;

        while (lo < hi) {
            while (arr[lo] % 2 == 0 && lo < hi)
                lo++;
            while (arr[hi] % 2 == 1 && lo < hi)
                hi--;
            if (lo < hi) {
                int temp = arr[lo];
                arr[lo] = arr[hi];
                arr[hi] = temp;
                lo++;
                hi--;
            }
        }
    }
}

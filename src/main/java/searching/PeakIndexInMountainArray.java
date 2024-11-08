package searching;

public class PeakIndexInMountainArray {
    /*
    * https://leetcode.com/problems/peak-index-in-a-mountain-array/description/
    * */
    public int peakIndexInMountainArray(int[] arr) {
        int low = 0, high = arr.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < arr[mid + 1]) {
                low = mid + 1; // Peak is in the right half
            } else {
                high = mid; // Peak is in the left half or at mid
            }
        }
        return low; // or high, both are equal here
    }
}

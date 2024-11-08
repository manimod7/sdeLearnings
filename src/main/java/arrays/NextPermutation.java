package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NextPermutation {
    public static List<Integer> nextPermutation(List<Integer> nums) {

        int n = nums.size();

        // Find the first element from the end that is smaller than the element next to it
        int inversionPoint = findInversionPoint(nums);

        // If the entire array is in descending order, simply reverse it to get the lowest possible permutation
        if (inversionPoint == -1) {
            Collections.reverse(nums);
            return nums;
        }

        // Find the smallest element greater than A[inversionPoint], starting from the end
        int swapWith = findNextGreater(nums, inversionPoint);

        // Swap the elements at inversionPoint and swapWith
        Collections.swap(nums, inversionPoint, swapWith);

        // Reverse the subarray after the inversion point to get the next permutation
        reverseSubarray(nums, inversionPoint + 1, n-1);
        return nums;
    }

    private static int findInversionPoint(List<Integer> nums) {

        for (int i = nums.size() - 2; i >= 0; i--) {

            if (nums.get(i) < nums.get(i + 1)) {
                return i;

            }

        }
        return -1; // No inversion point found, array is in descending order
    }

    private static int findNextGreater(List<Integer> nums, int inversionPoint) {

        for (int i = nums.size() - 1; i > inversionPoint; i--) {
            if (nums.get(i) > nums.get(inversionPoint)) {
                return i;
            }
        }

        return inversionPoint; // This should never happen if inversionPoint is correctly found
    }


    private static void reverseSubarray(List<Integer> nums, int start, int end) {
        Collections.sort(nums.subList(start, end+1));
    }

    public static void main(String[] args) {
        ArrayList<Integer> nums = new ArrayList<>(Arrays.asList(1,7,9,2,3,0,9,8,7,6,5,5,4));
        nextPermutation(nums);
        System.out.println(nums);
    }

}

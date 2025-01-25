package Interview;

public class RotatedArraySearch {
    public static int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            }

            // Check which half is sorted
            if (nums[left] <= nums[mid]) {
                // Left half is sorted
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // Right half is sorted
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1; // not found
    }

    public static void main(String[] args) {
        int[] arr = {4,5,6,7,0,1,2};
        System.out.println(search(arr, 0)); // Output: 4 (index of 0)
        System.out.println(search(arr, 3)); // Output: -1 (not found)
    }
}

package searching;

public class SquareRootBinarySearch {
    /**
     * Computes the integer square root of a non-negative integer x.
     *
     * @param x The non-negative integer whose square root is to be computed.
     * @return The truncated integer square root of x.
     */
    public static int mySqrt(int x) {
        // Edge cases
        if (x == 0 || x == 1) {
            return x;
        }

        // Initialize binary search boundaries
        int left = 1;
        int right = x / 2;
        int result = 0;

        // Binary search loop
        while (left <= right) {
            // Prevents overflow
            int mid = left + (right - left) / 2;
            // To prevent overflow when mid is large, use long for multiplication
            long midSquared = (long) mid * mid;

            if (midSquared == x) {
                return mid; // Exact square root found
            } else if (midSquared < x) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // 'right' is the integer square root of x
        return right;
    }

    // Example usage and test cases
    public static void main(String[] args) {
        int[] testCases = {0, 1, 4, 8, 9, 15, 16, 2147395599};
        for (int x : testCases) {
            System.out.println("Square root of " + x + " is: " + mySqrt(x));
        }
    }
}

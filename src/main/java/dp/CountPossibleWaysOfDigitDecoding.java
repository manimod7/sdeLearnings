package dp;

public class CountPossibleWaysOfDigitDecoding {
    // Helper function to recursively calculate decoding ways
    // with memoization.
    static int decodeHelper(String digits,
                            int index, int[] memo) {

        //https://www.geeksforgeeks.org/count-possible-decodings-given-digit-sequence/

        int n = digits.length();

        // Base case: If we reach the end of the string,
        // return 1 as it signifies a valid decoding.
        if (index >= n) {
            return 1;
        }

        // If the value is already calculated for this
        // index, return it.
        if (memo[index] != -1) {
            return memo[index];
        }

        int ways = 0;

        // Single-digit decoding: check if current digit is not '0'.
        if (digits.charAt(index) != '0') {
            ways = decodeHelper(digits, index + 1, memo);
        }

        // Two-digit decoding: check if next two digits are valid.
        if ((index + 1 < n) && ((digits.charAt(index) == '1' &&
                digits.charAt(index + 1) <= '9') ||
                (digits.charAt(index) == '2' &&
                        digits.charAt(index + 1) <= '6'))) {

            ways += decodeHelper(digits, index + 2, memo);
        }

        // Memoize the result for the current index.
        memo[index] = ways;
        return ways;
    }
}

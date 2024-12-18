package searching;

public class AthMagicalNumber {
    public int solve(int A, int B, int C) {
        // Define modulo constant
        int MOD = 1000000007;
        long left = 1, right = (long) A * Math.min(B, C), result = 0;
        long lcmBC = lcm(B, C);

        while (left <= right) {
            long mid = left + (right - left) / 2;
            if (countMagical(mid, B, C, lcmBC) >= A) {
                result = mid; // Candidate for the A-th magical number
                right = mid - 1; // Try smaller
            } else {
                left = mid + 1; // Try larger
            }
        }

        return (int) (result % MOD);

    }

    long gcd(long x, long y) {
        if (y == 0)
            return x;
        return gcd(y, x % y);
    }

    // Helper function to compute lcm
    long lcm(long x, long y) {
        return (x * y) / gcd(x, y);
    }

    // Helper function to count numbers <= X divisible by B or C
    long countMagical(long X, long B, long C, long LCM) {
        return (X / B) + (X / C) - (X / LCM);
    }
}

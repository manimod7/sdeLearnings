package recursion;

public class PowerCalculator {
    public int pow(int A, int B, int C) {
        // Just write your code below to complete the function. Required input is available to you as the function arguments.
        // Do not print the result or any output. Just return the result via this function.

        if (A == 0 && B != 0) return 0;  // 0^B = 0 for B > 0
        long result = recursivePower(positiveModulo(A, C), B, C);
        return (int) positiveModulo(result, C);  // Ensure the final result is non-negative
    }

    private long positiveModulo(long value, long mod) {
        return (value % mod + mod) % mod;
    }

    // Recursive function to compute (A^B) % C
    private long recursivePower(long A, long B, long C) {
        // Base cases
        if (B == 0) return 1;  // A^0 = 1
        if (B == 1) return positiveModulo(A, C);  // A^1 return A % C

        // Recursive step
        long halfPower = recursivePower(A, B / 2, C);
        long fullPower = positiveModulo(halfPower * halfPower, C);  // (A^(B/2))^2

        // If B is odd, multiply by A one more time
        if (B % 2 != 0) {
            fullPower = positiveModulo(fullPower * A, C);
        }

        return fullPower;
    }
}

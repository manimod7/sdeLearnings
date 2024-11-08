package arrays;

public class MaximumHeightOfStairCase {
    public int solve(int A) {
        if(A==1)
            return 1;
        int currentSum = 0;
        for(int i=1;i<A;i++) {
            currentSum = currentSum+i;
            if(currentSum==A)
                return i;
            if(currentSum>A) {
                return i-1;
            }

        }
        return A;
    }
    /**
     * Finds the largest integer k such that the sum of the first k natural numbers
     * is less than or equal to A.
     *
     * @param A The target integer.
     * @return The integer k as per the problem statement.
     */
    public int solveAnotherMethod(int A) {
        // Handle edge cases
        if (A < 0) {
            throw new IllegalArgumentException("Input A must be a non-negative integer.");
        }
        if (A == 0) {
            return 0;
        }
        if (A == 1) {
            return 1;
        }

        // Compute discriminant for the quadratic equation k^2 + k - 2A = 0
        double discriminant = 1 + 8.0 * A;

        // Compute the positive root of the quadratic equation
        double k = (-1 + Math.sqrt(discriminant)) / 2.0;

        // Floor the result to get the largest integer k such that k(k + 1)/2 <= A

        return (int) Math.floor(k);
    }
}

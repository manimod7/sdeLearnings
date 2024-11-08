package bitManipulation;

public class PowerOfFive {
    /*
    Given an integer A, find and return the Ath magic number.
    A magic number is defined as a number that can be expressed as a power of 5 or a sum of unique powers of 5.
    First few magic numbers are 5, 25, 30(5 + 25), 125, 130(125 + 5), ….

    */
    public int solve(int A) {
        int ans = 0;
        int base = 5;
        while (A > 0) {
            // Check if the last bit is set
            if ((A & 1) == 1) {
                ans += base;
            }
            A >>= 1; // Move to the next bit
            base *= 5; // Increase the power of 5
        }

        return ans;
    }
}

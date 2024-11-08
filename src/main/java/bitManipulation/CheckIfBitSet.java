package bitManipulation;

public class CheckIfBitSet {
    /* You are given two integers A and B.
    • Return 1 if B-th bit in A is set
    • Return 0 if B-th bit in A is unset
     */
    public int solve(int A, int B) {
        int bBitSet =1<<B;
        int ans = A & bBitSet;
        return ans == 0 ? 0 : 1;
    }

}

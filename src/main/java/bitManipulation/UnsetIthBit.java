package bitManipulation;

public class UnsetIthBit {
    public int solve(int A, int B) {
        int setBitB = 1 << B;
        int unsetBitB = ~setBitB; // Use bitwise NOT here
        return A & unsetBitB;
    }

}

package arrays;

public class NumberOfOneBits {
    public int numOneBits(int n) {
        int count = 0;
        while (n != 0) {
            // Increment count if the least significant bit is 1
            count += (n & 1);
            // Unsigned right shift the bits of n to the right
            n >>>= 1;
        }
        return count;
    }

    public static void main(String[] args) {
        NumberOfOneBits numberOfOneBits = new NumberOfOneBits();
        int number = 11; // Binary representation: 1011, which has three '1' bits
        int result = numberOfOneBits.numOneBits(number);
        System.out.println("The number of '1' bits in the binary representation of " + number + " is: " + result);
    }

}

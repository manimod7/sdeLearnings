package bitManipulation;

/*
Problem Description

Given an integer A.
Two numbers, X and Y, are defined as follows:

X is the greatest number smaller than A such that the XOR sum of X and A is the same as the sum of X and A.
Y is the smallest number greater than A, such that the XOR sum of Y and A is the same as the sum of Y and A.
Find and return the XOR of X and Y.

NOTE 1: XOR of X and Y is defined as X ^ Y where '^' is the BITWISE XOR operator.

NOTE 2: Your code will be run against a maximum of 100000 Test Cases.
*/


public class StrangeEquality {
    public int solve(int A) {

        int maxBit = 0;
        for (int i = 31; i >= 0; i--) {
            int maskBit = 1 << i;
            if ((A & maskBit) != 0) {
                maxBit = i;
                break;
            }


        }
        int x =0;
        int y = 1 << (maxBit + 1);

        for (int i = 0; i < maxBit; i++) {
            int maskBit = 1 << i;
            int bitCheck = A & maskBit;
            if (bitCheck > 0) {

                continue;
            } else {

                x |= (1 << i);
            }
        }


        return x ^ y;
    }
}

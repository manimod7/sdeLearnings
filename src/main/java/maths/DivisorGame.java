package maths;

public class DivisorGame {

    // Problem Description
    //Scooby has 3 three integers A, B, and C.
    //Scooby calls a positive integer special if it is divisible by B and it is divisible by C.
    // You need to tell the number of special integers less than or equal to A.

    int gcd(int a, int b) {
        if(b==0)
            return a;
        return gcd(b, a%b);
    }
    int lcm(int a, int b) {
        return (a*b)/gcd(a,b);
    }
    public int solve(int A, int B, int C) {
        int lcmOfBandC = lcm(B,C);
        return A/lcmOfBandC;
    }
}

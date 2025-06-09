package maths;

public class GCD {
    public int gcd(int A, int B) {
       //A = x1q+r1, B = x2q+r2
        if(B==0)
            return A;
        return gcd(B, A%B);
    }
}


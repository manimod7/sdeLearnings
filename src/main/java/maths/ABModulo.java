package maths;

public class ABModulo {
    // Problem Description
    //
    //Given two integers A and B, find the greatest possible positive integer M, such that A % M = B % M.

    public int solve(int A, int B) {

        //since A%M==B%M we can say that
        //A = kM +r
        //B = jM +r
        //A-B = (k-j)M, now we need to find greatest M whic is A-B|
        // Calculate the absolute difference between A and B
        int diff = Math.abs(A - B);
        // The greatest possible M is the absolute difference itself
        return diff;
    }
}

package maths;

import java.util.ArrayList;

public class LargestCoprimeDivisor {
    public int gcd (int A, int B) {
        if(B==0)
            return A;
        return gcd(B, A%B);
    }

    public int cpFact(int A, int B) {
        ArrayList<Integer> factors = new ArrayList();
        int rootA = (int)Math.pow(A,0.5);
        for(int i =1;i<=rootA;i++) {
            if(A%i==0){
                factors.add(i);
                if(i*i!=A)
                    factors.add(A/i);
            }
        }
        int ans = 1;
        for(int x : factors) {
            if(gcd(x,B)==1) {
                ans = Math.max(ans, x);
            }
        }
        return ans;
    }
}

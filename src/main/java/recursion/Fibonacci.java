package recursion;

import java.util.ArrayList;

public class Fibonacci {
    public int helper(int A) {
        if(A==0)
            return 0;
        if(A==1)
            return 1;
        return helper(A-1) +helper(A-2);
    }
    public int findAthFibonacci(int A) {
        return helper(A);
    }
}

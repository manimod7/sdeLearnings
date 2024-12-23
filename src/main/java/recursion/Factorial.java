package recursion;

public class Factorial {
    public int helper(int x) {
        if(x==0 || x==1)
            return 1;
        return x * helper(x-1);
    }
    public int solve(int A) {
        return helper(A);
    }
}

package recursion;

public class SumOfDigits {
    int helper(int x) {
        if(x/10 == 0)
            return x;
        else
            return x%10 + helper(x/10);
    }
    public int solve(int A) {
        return helper(A);
    }
}

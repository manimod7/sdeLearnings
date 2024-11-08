package bitManipulation;

public class SetBit {
    public int solve(int A, int B) {
        int ans = 0;
        ans = 1<<A;
        if(A==B)
            return ans;
        int temp = 1<<B;
        return ans + temp;
    }
}

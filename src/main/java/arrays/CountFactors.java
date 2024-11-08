package arrays;

public class CountFactors {
    public int solve(int A) {
        int sqrtA = (int)Math.pow(A, 0.5);
        int cnt=0;
        for(int i=1;i<=sqrtA;i++){
            if(A%i==0) {
                if(i == A/i)
                    cnt++;
                else
                    cnt+=2;
            }
        }
        return cnt;
    }
}

package recursion;

public class MagicNumber {
    public int helper(int currNum) {
        if(currNum==1)
            return 1;
        if(currNum<=9)
            return 0;
        int newNum = 0;
        while(currNum>0) {
            newNum = newNum + currNum%10;
            currNum = currNum/10;
        }
        return helper(newNum);
    }
    public int solve(int A) {
        return helper(A);
    }
}

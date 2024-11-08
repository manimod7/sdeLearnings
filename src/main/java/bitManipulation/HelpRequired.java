package bitManipulation;

public class HelpRequired {
    /*
    * Alex and Sam are good friends. Alex is doing a lot of programming these days.
    * He has set a target score of A for himself. Initially, Alex's score was zero.
    * Alex can double his score by doing a question,
    * or Alex can seek help from Sam for doing questions that will contribute 1 to Alex's score.
    * Alex wants his score to be precisely A.
    * Also, he does not want to take much help from Sam.
    */
    public int solve(int n) {
        int ans = 0;
        while(n>0) {
            int temp = n-1;
            n= n & temp;
            ans++;
        }
        return ans;
    }
}

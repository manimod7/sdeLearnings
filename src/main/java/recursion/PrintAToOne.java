package recursion;

public class PrintAToOne {
    public void helper(int A) {
        if(A <1){
            return;
        } else{
            System.out.print(A+" ");
            helper(A-1);
        }
    }
    public void solve(int A) {
        helper(A);
        System.out.println("");
    }
}

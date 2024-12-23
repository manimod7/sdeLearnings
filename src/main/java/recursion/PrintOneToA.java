package recursion;

public class PrintOneToA {
    public void helper(int A) {
        if(A <1){
            return;
        } else{
            helper(A-1);
        }
        System.out.print(A+" ");
    }
    public void solve(int A) {
        helper(A);
        System.out.println("");
    }
}

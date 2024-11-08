package arrays;
import java.util.*;
public class RowWithMaximumOnes {
    public int solve(ArrayList<ArrayList<Integer>> A) {
        int n = A.size();
        int row=0, col = n-1;
        int mx = 0, ans = 0;
        while(row<n && col>=0) {
            if(A.get(row).get(col)==1){
                if(n-col+1>mx) {
                    mx = n-col+1;
                    ans = row;
                }

                col--;
            }
            else{
                row++;
            }
        }
        return ans;
    }

}

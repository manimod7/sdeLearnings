package arrays;

import java.util.ArrayList;

public class SumOfAllSubmatrices {
    public int solve(ArrayList<ArrayList<Integer>> A) {
        int n = A.size();
        int ans = 0;
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++){
                int contri = A.get(i).get(j) *(i+1)*(n-i)*(j+1)*(n-j);
                ans+=contri;
            }
        }
        return ans;
    }

}

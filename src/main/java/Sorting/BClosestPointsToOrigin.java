package Sorting;

import java.util.ArrayList;
import java.util.Collections;

public class BClosestPointsToOrigin {
    public ArrayList<ArrayList<Integer>> solve(ArrayList<ArrayList<Integer>> A, int B) {
        int n = A.size();
        if(B>n ||n==0)
            return A;
        Collections.sort(A,(a, b)->{
            int x1= a.get(0);
            int y1 = a.get(1);
            int x2 = b.get(0);
            int y2 = b.get(1);
            return x1*x1 + y1*y1 - x2*x2 -y2*y2;
        });
        ArrayList<ArrayList<Integer>> ans = new ArrayList(A.subList(0, B));
        return ans;
    }
}

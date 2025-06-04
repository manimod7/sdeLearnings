package bitManipulation;

import java.util.ArrayList;
import java.util.Collections;

public class MinXor {
    public int findMinXor(ArrayList<Integer> A) {
        Collections.sort(A);
        int minXor = Integer.MAX_VALUE;

        for(int i=1;i<A.size();i++) {
            minXor = Math.min(minXor, (A.get(i)^A.get(i-1)));
        }
        return minXor;
    }
}

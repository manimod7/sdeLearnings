package bitManipulation;

import java.util.List;

public class SingleNumber {
    public int singleNumber(final List<Integer> A) {
        int ans = 0;
        for(int i=0;i<A.size();i++) {
            ans = ans ^ A.get(i);
        }
        return ans;
    }
}

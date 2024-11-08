package arrays;

import java.util.ArrayList;

public class AllSubarraySum {
    public Long subarraySum(ArrayList<Integer> A) {
        Long ans = 0L;
        int n = A.size();
        for(int i=0; i<n; i++) {
            // Cast at least one operand to Long to ensure the multiplication is done using Long arithmetic
            Long contribution = (Long.valueOf(A.get(i)) * (i+1) * (n-i));
            ans += contribution;
        }
        return ans;
    }

}

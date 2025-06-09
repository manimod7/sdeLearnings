package Sorting;

import java.util.ArrayList;
import java.util.Collections;

public class TenDigitSorting {
    public ArrayList<Integer> solve(ArrayList<Integer> nums) {
        Collections.sort(nums,(a, b)->{
            int A=a;
            int B=b;
            B=B/10;
            A=A/10;
            A=A%10;
            B=B%10;
            if(A<B)
                return -1;
            if(A>B)
                return 1;
            else
                return b-a;
        });
        return nums;
    }
}

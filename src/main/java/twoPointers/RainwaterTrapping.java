package twoPointers;

import java.util.ArrayList;

public class RainwaterTrapping {
    public int maxArea(ArrayList<Integer> A) {
        int left =0, right = A.size() - 1;
        int area = 0;
        while(left<=right) {
            area = Math.max(area, (right-left)* Math.min(A.get(left), A.get(right)));
            if(A.get(left)<A.get(right))
                left++;
            else
                right--;
        }
        return area;
    }
}

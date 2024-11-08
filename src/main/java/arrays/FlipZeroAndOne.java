package arrays;

import java.util.ArrayList;

public class FlipZeroAndOne {
    public ArrayList<Integer> flip(String A) {
        ArrayList<Integer> ans = new ArrayList();
        ans.add(0);
        ans.add(0);
        int currSum = 0, maxSum = 0, l =0, r =0;
        for(int i=0;i<A.length();i++) {
            if(A.charAt(i)=='0')
                currSum+=1;
            else
                currSum-=1;
            if(currSum> maxSum){
                maxSum = currSum;
                ans.set(0, l+1);
                ans.set(1, r+1);
            }
            if(currSum<0){
                currSum = 0;
                l = i+1;
                r= i+1;
            }
            else{
                r+=1;
            }
        }
        if(maxSum==0)
            return new ArrayList<Integer>();
        return ans;
    }

}

package bitManipulation;

import java.util.ArrayList;

public class TwoMissingElements {
    /*
    Given an array A of length N where all the elements are distinct and are in the range [1, N+2].
    Two numbers from the range [1, N+2] are missing from the array A. Find the two missing numbers.
    */
    public ArrayList<Integer> solve(ArrayList<Integer> A) {
        int n = A.size();
        ArrayList<Integer> ans = new ArrayList();
        int xorOfMissingElements = 0;
        for(int i=0;i<n;i++) {
            xorOfMissingElements ^= A.get(i);
        }
        for(int i=1;i<=n+2;i++) {
            xorOfMissingElements^=i;
        }
        // Now xorOfMissingElements = x^y (x and y are missing elements)
        int x = 0, y = 0;
        int rightMostSetBit = xorOfMissingElements & (-xorOfMissingElements);
        for(int num : A) {
            if((num & rightMostSetBit)!=0) {
                x = x ^ num;
            }
            else {
                y = y ^ num;
            }
        }
        for(int i = 1; i <= n + 2; i++) {
            if((i & rightMostSetBit) != 0) {
                x ^= i;
            } else {
                y ^= i;
            }
        }
        ans.add(Math.min(x,y));
        ans.add(Math.max(x,y));
        return ans;
    }

    public ArrayList<Integer> solveMethod2(ArrayList<Integer> A) {
        int n = A.size();
        int[] arr = new int[n+2];
        for (Integer integer : A) {
            if (integer > 0 && integer <= n + 2)
                arr[integer - 1] = integer;
        }
        ArrayList<Integer> ans = new ArrayList();
        for(int i=0;i<n+2;i++) {
            if(arr[i]==0)
                ans.add(i+1);
        }
        return ans;
    }


}

package Sorting;

import java.util.*;

public class TopKFrequentElements {

    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer,Integer> map = new HashMap<>();
        ArrayList<Integer> uniqueElements = new ArrayList<>();
        for(int x : nums) {
            if(map.get(x) == null) {
                uniqueElements.add(x);
            }
            map.put(x, map.getOrDefault(x,0) +1);
        }
        uniqueElements.sort((a, b) -> map.get(b) - map.get(a));
        int[] ans = new int[k];
        for(int i=0;i<k;i++) {
            ans[i] = uniqueElements.get(i);
        }
        return ans;
    }

}

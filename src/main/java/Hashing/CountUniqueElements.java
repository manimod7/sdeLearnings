package Hashing;

import java.util.*;

public class CountUniqueElements {
    public int solve(ArrayList<Integer> A) {
        Map<Integer, Integer> map = new HashMap();
        for(int x:A) {
            map.put(x, map.getOrDefault(x,0)+1);
        }
        int count = 0;
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getValue() == 1) {
                count++;
            }
        }
        return count;
    }

    public int solve2(ArrayList<Integer> A) {
        // 'seen' tracks all elements we've encountered at least once
        Set<Integer> seen = new HashSet<>();
        // 'once' will end up containing exactly those elements that appear only once
        Set<Integer> once = new HashSet<>();

        for (int x : A) {
            if (!seen.contains(x)) {
                // first time seeing x
                seen.add(x);
                once.add(x);
            } else {
                // x has appeared before → it can no longer be "exactly once"
                once.remove(x);
            }
        }

        return once.size();
    }
}

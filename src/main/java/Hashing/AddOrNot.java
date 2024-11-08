package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrNot {

    public List<Integer> solve(List<Integer> A, int B) {
        Map<Integer, Integer> map = new HashMap<>();
        String s ="ABC";
        s.compareTo("DEF");
        for (int x : A) {
            if (map.get(x) == null) {
                map.put(x, 1);
            } else {
                map.put(x, map.get(x) + 1);
            }
        }
        return new ArrayList<>();


    }
}

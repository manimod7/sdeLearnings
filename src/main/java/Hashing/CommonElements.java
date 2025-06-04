package Hashing;

import java.util.ArrayList;
import java.util.HashMap;

public class CommonElements {
    public ArrayList<Integer> solve(ArrayList<Integer> A, ArrayList<Integer> B) {

        HashMap<Integer, Integer> freqA = new HashMap<>();
        for (int x : A) {
            freqA.put(x, freqA.getOrDefault(x, 0) + 1);
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int x : B) {
            int count = freqA.getOrDefault(x, 0);
            if (count > 0) {
                result.add(x);
                freqA.put(x, count - 1);
            }
        }

        return result;

    }
}

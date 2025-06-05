package slidingWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DistinctNumbers {

    public ArrayList<Integer> dNums(ArrayList<Integer> A, int B) {
        int n = A.size();
        // If window size is larger than array, return empty list
        if (B > n) {
            return new ArrayList<>();
        }

        Map<Integer, Integer> map = new HashMap<>();
        // Build frequency map for the first window [0 .. B-1]
        for (int i = 0; i < B; i++) {
            int x = A.get(i);
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        ArrayList<Integer> ans = new ArrayList<>();
        // Record distinct count for the first window
        ans.add(map.size());

        // Slide the window from i = B .. n-1
        for (int i = B; i < n; i++) {
            int outgoing = A.get(i - B);
            int incoming = A.get(i);

            // 1) Remove/decrement the outgoing element
            int freqOut = map.get(outgoing);
            if (freqOut == 1) {
                map.remove(outgoing);
            } else {
                map.put(outgoing, freqOut - 1);
            }

            // 2) Add/increment the incoming element
            map.put(incoming, map.getOrDefault(incoming, 0) + 1);

            // 3) Number of distinct keys after the shift
            ans.add(map.size());
        }

        return ans;
    }
}

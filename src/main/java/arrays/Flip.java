package arrays;

import java.util.ArrayList;

public class Flip {
    public ArrayList<Integer> flip(String A) {
        int n = A.length();
        // best = best sum seen so far (max number of 1's gained)
        int best = 0;
        // cur = current running sum; curStart = start index of this run
        int cur = 0, curStart = 0;
        // bestL, bestR = best flip interval in 0-based indexing
        int bestL = 0, bestR = -1;

        for (int i = 0; i < n; i++) {
            // delta = +1 for '0', -1 for '1'
            int delta = (A.charAt(i) == '0') ? 1 : -1;

            // If starting fresh here gives a strictly better sum, restart
            if (cur + delta < delta) {
                cur = delta;
                curStart = i;
            } else {
                cur += delta;
            }

            // If we improve on the best, or tie but get a lexicographically smaller interval
            if (cur > best ||
                    (cur == best &&
                            (curStart < bestL ||
                                    (curStart == bestL && i < bestR)))) {
                best = cur;
                bestL = curStart;
                bestR = i;
            }
        }

        ArrayList<Integer> ans = new ArrayList<>();
        // If bestR stayed -1 (or best == 0), no positive gain possible
        if (bestR == -1) {
            return ans;
        }
        // Convert to 1-based indexing
        ans.add(bestL + 1);
        ans.add(bestR + 1);
        return ans;
    }
}

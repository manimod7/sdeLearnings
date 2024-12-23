package recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSum1 {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        // Not strictly required to sort, but it's often helpful
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
                           List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            // Found a valid combination
            result.add(new ArrayList<>(current));
            return;
        }
        if (remaining < 0) {
            // Exceeded the sum
            return;
        }

        // Explore candidates from 'start' to end
        for (int i = start; i < candidates.length; i++) {
            current.add(candidates[i]);
            // Since we can reuse the same candidate, we call backtrack with i (not i + 1)
            backtrack(candidates, remaining - candidates[i], i, current, result);
            current.remove(current.size() - 1); // backtrack
        }
    }
}

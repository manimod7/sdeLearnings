package stack;

import java.util.ArrayList;
import java.util.Stack;

public class MaxAndMinArrayContribution {
    public int solve(ArrayList<Integer> A) {
        int n = A.size();
        long MOD = 1000000007;

        // Arrays to store distances to nearest greater/smaller on left and right
        int[] leftMax = new int[n], rightMax = new int[n];
        int[] leftMin = new int[n], rightMin = new int[n];

        // Monotonic stack for max contribution
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && A.get(stack.peek()) <= A.get(i)) {
                stack.pop();
            }
            leftMax[i] = stack.isEmpty() ? (i + 1) : (i - stack.peek());
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && A.get(stack.peek()) < A.get(i)) {
                stack.pop();
            }
            rightMax[i] = stack.isEmpty() ? (n - i) : (stack.peek() - i);
            stack.push(i);
        }

        // Monotonic stack for min contribution
        stack.clear();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && A.get(stack.peek()) >= A.get(i)) {
                stack.pop();
            }
            leftMin[i] = stack.isEmpty() ? (i + 1) : (i - stack.peek());
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && A.get(stack.peek()) > A.get(i)) {
                stack.pop();
            }
            rightMin[i] = stack.isEmpty() ? (n - i) : (stack.peek() - i);
            stack.push(i);
        }

        // Calculate the total contribution
        long result = 0;
        for (int i = 0; i < n; i++) {
            long maxContribution = (long) A.get(i) * leftMax[i] * rightMax[i];
            long minContribution = (long) A.get(i) * leftMin[i] * rightMin[i];
            result = (result + (maxContribution - minContribution) % MOD) % MOD;
        }

        return (int) ((result + MOD) % MOD);
    }
}

package stack;

import java.util.ArrayList;
import java.util.Stack;

public class LargestRectangleArea {
    public int largestRectangleArea(ArrayList<Integer> A) {
        int n = A.size();

        // Arrays to store the nearest smaller indices
        int[] left = new int[n];  // Nearest Smaller to Left (NSL)
        int[] right = new int[n]; // Nearest Smaller to Right (NSR)

        // Monotonic stack for NSL
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && A.get(stack.peek()) >= A.get(i)) {
                stack.pop();
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        // Clear the stack for NSR
        stack.clear();

        // Monotonic stack for NSR
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && A.get(stack.peek()) >= A.get(i)) {
                stack.pop();
            }
            right[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        // Calculate the maximum area
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int height = A.get(i);
            int width = right[i] - left[i] - 1;
            int area = height * width;
            maxArea = Math.max(maxArea, area);
        }

        return maxArea;
    }
}

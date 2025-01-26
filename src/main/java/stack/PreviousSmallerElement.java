package stack;

import java.util.ArrayList;
import java.util.Stack;

public class PreviousSmallerElement {
    public ArrayList<Integer> prevSmaller(ArrayList<Integer> A) {
        int n = A.size();
        ArrayList<Integer> result = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();

        for (Integer integer : A) {
            // Pop elements from the stack until we find a smaller element
            while (!stack.isEmpty() && stack.peek() >= integer) {
                stack.pop();
            }

            // If the stack is empty, no smaller element exists
            if (stack.isEmpty()) {
                result.add(-1);
            } else {
                result.add(stack.peek());
            }

            // Push the current element onto the stack
            stack.push(integer);
        }

        return result;
    }
}

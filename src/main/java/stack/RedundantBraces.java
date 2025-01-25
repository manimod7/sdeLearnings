package stack;

import java.util.Stack;

public class RedundantBraces {
    public class Solution {
        public int braces(String A) {
            Stack<Character> stack = new Stack<>();

            for (char ch : A.toCharArray()) {
                if (ch == ')') {
                    boolean hasOperator = false;

                    // Pop until '(' is found
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        char top = stack.pop();
                        if (top == '+' || top == '-' || top == '*' || top == '/') {
                            hasOperator = true;
                        }
                    }

                    // Pop the opening parenthesis '('
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }

                    // If no operator was found, the braces are redundant
                    if (!hasOperator) {
                        return 1;
                    }
                } else {
                    // Push current character to the stack
                    stack.push(ch);
                }
            }

            // If no redundant braces are found
            return 0;

        }
    }

}

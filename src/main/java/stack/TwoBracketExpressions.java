package stack;

import java.util.Stack;

public class TwoBracketExpressions {
    private static final int MAX_CHAR = 26; // Only 26 lowercase English letters

    public int solve(String A, String B) {
        // If the two expressions evaluate to the same contribution, return 1; otherwise, return 0
        return areSame(A, B) ? 1 : 0;
    }

    private boolean areSame(String expr1, String expr2) {
        // Array to store contributions of all variables
        int[] contributions = new int[MAX_CHAR];
        // Evaluate the first expression (positive contribution)
        evaluateExpression(expr1, contributions, true);
        // Evaluate the second expression (negative contribution)
        evaluateExpression(expr2, contributions, false);

        // If all contributions are zero, the expressions are equivalent
        for (int i = 0; i < MAX_CHAR; i++) {
            if (contributions[i] != 0) {
                return false;
            }
        }
        return true;
    }

    private void evaluateExpression(String expression, int[] contributions, boolean isPositive) {
        Stack<Boolean> signStack = new Stack<>();
        signStack.push(true); // Base sign is positive

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (c == '(') {
                // Push the current sign context to the stack
                if (isAdjacentSignPositive(expression, i)) {
                    signStack.push(signStack.peek());
                } else {
                    signStack.push(!signStack.peek());
                }
            } else if (c == ')') {
                // Pop the last sign context
                signStack.pop();
            } else if (Character.isLetter(c)) {
                // Adjust contribution of the variable
                int index = c - 'a'; // Map 'a' to 0, 'b' to 1, ..., 'z' to 25
                boolean isSignPositive = isAdjacentSignPositive(expression, i);

                // Contribution calculation with explicit if-else
                if (isSignPositive) {
                    if (signStack.peek()) {
                        if (isPositive) {
                            contributions[index] = contributions[index] + 1;
                        } else {
                            contributions[index] = contributions[index] - 1;
                        }
                    } else {
                        if (isPositive) {
                            contributions[index] = contributions[index] - 1;
                        } else {
                            contributions[index] = contributions[index] + 1;
                        }
                    }
                } else {
                    if (signStack.peek()) {
                        if (isPositive) {
                            contributions[index] = contributions[index] - 1;
                        } else {
                            contributions[index] = contributions[index] + 1;
                        }
                    } else {
                        if (isPositive) {
                            contributions[index] = contributions[index] + 1;
                        } else {
                            contributions[index] = contributions[index] - 1;
                        }
                    }
                }
            }
            i = i + 1;
        }
    }

    private boolean isAdjacentSignPositive(String expression, int index) {
        // If the current character is the first one or preceded by a '+', the sign is positive
        if (index == 0) {
            return true;
        }
        return expression.charAt(index - 1) != '-';
    }
}

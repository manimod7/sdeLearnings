package stack;

import java.util.ArrayList;
import java.util.Stack;

public class EvaluatePostfixExpression {
    public int evalRPN(ArrayList<String> A) {
        Stack<Integer> stack = new Stack<>();

        for (String token : A) {

            // Check if the token is an operator
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                // Pop two operands from the stack
                int b = stack.pop(); // Second operand
                int a = stack.pop(); // First operand

                // Perform the operation
                int result = 0;
                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        result = a / b; // Integer division
                        break;
                }

                // Push the result back onto the stack
                stack.push(result);
            } else {
                // If it's a number, push it onto the stack
                stack.push(Integer.parseInt(token));
            }
        }

        // The final result is the only element left in the stack
        return stack.pop();
    }
}

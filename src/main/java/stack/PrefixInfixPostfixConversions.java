package stack;

import java.util.Stack;

public class PrefixInfixPostfixConversions {

    // Function to convert prefix to infix
    public static String prefixToInfix(String prefix) {
        Stack<String> stack = new Stack<>();

        // Start reading the prefix expression from right to left
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);

            // If the character is an operand, push it to the stack
            if (Character.isLetterOrDigit(ch)) {
                stack.push(ch + "");
            }
            // If the character is an operator
            else if (isOperator(ch)) {
                // Pop two operands from the stack
                String operand1 = stack.pop();
                String operand2 = stack.pop();

                // Form the infix expression and push it back to the stack
                String infix = "(" + operand1 + ch + operand2 + ")";
                stack.push(infix);
            }
        }

        // The result will be in the top of the stack
        return stack.pop();
    }

    public static String prefixToPostfix(String prefix) {
        Stack<String> stack = new Stack<>();

        // Traverse prefix from right to left
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);

            if (Character.isLetterOrDigit(ch)) {
                stack.push(ch + ""); // Push operands
            } else {
                // Pop two operands
                String operand1 = stack.pop();
                String operand2 = stack.pop();

                // Combine in postfix order and push
                String postfix = operand1 + operand2 + ch;
                stack.push(postfix);
            }
        }

        return stack.pop(); // Final postfix expression
    }


    // Helper method to check if a character is an operator
    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    // Function to convert postfix to infix
    public static String postfixToInfix(String postfix) {
        Stack<String> stack = new Stack<>();

        // Traverse each character in the postfix expression
        for (int i = 0; i < postfix.length(); i++) {
            char ch = postfix.charAt(i);

            // If the character is an operand, push it to the stack
            if (Character.isLetterOrDigit(ch)) {
                stack.push(ch + "");
            }
            // If the character is an operator
            else if (isOperator(ch)) {
                // Pop two operands from the stack
                String operand2 = stack.pop();
                String operand1 = stack.pop();

                // Form the infix expression and push it back to the stack
                String infix = "(" + operand1 + ch + operand2 + ")";
                stack.push(infix);
            }
        }

        // The result will be in the top of the stack
        return stack.pop();
    }

    public static String postfixToPrefix(String postfix) {
        Stack<String> stack = new Stack<>();

        // Traverse postfix from left to right
        for (char ch : postfix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                stack.push(ch + ""); // Push operands
            } else {
                // Pop two operands
                String operand2 = stack.pop();
                String operand1 = stack.pop();

                // Combine in prefix order and push
                String prefix = ch + operand1 + operand2;
                stack.push(prefix);
            }
        }

        return stack.pop(); // Final prefix expression
    }


}

package trees;

import java.util.Stack;

public class Inorder {
    public void inorderRecursive(TreeNode root) {
        if (root == null) return;
        inorderRecursive(root.left);
        System.out.print(root.val + " ");
        inorderRecursive(root.right);
    }

    public void inorderIterative(TreeNode root) {
        if (root == null) return;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            // Reach the leftmost node of the current subtree
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            // Pop the top node from stack
            current = stack.pop();
            System.out.print(current.val + " ");
            // Move to the right subtree
            current = current.right;
        }
    }

}

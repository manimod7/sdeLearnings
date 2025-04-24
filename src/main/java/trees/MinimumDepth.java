package trees;

public class MinimumDepth {

    public int minDepth(TreeNode root) {
        // Base case: if the tree is empty
        if (root == null) return 0;

        // If left subtree is null, recurse only on right
        if (root.left == null) return 1 + minDepth(root.right);

        // If right subtree is null, recurse only on left
        if (root.right == null) return 1 + minDepth(root.left);

        // If both left and right exist, take the minimum
        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
    }
}

package trees;

public class HasPathSum {
    public boolean hasPathSum(TreeNode root, int targetSum) {
        // Base case: empty tree
        if (root == null) return false;

        // If it's a leaf, check if path sum matches
        if (root.left == null && root.right == null)
            return targetSum == root.val;

        // Recur left and right with reduced targetSum
        int remaining = targetSum - root.val;
        return hasPathSum(root.left, remaining) || hasPathSum(root.right, remaining);
    }
}

package trees;


public class CurrentPathSum {
    public int sumNumbers(TreeNode root) {
        return dfs(root, 0);
    }

    private int dfs(TreeNode node, int currNumber) {
        if (node == null) return 0;

        currNumber = currNumber * 10 + node.val;

        // If it's a leaf, return the current number
        if (node.left == null && node.right == null) {
            return currNumber;
        }

        // Recurse left and right
        return dfs(node.left, currNumber) + dfs(node.right, currNumber);
    }
}


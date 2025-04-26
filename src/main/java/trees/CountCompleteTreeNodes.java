package trees;

/*
 * Problem:
 * Given the root of a complete binary tree, return the number of nodes in the tree.
 *
 * Complete Binary Tree Properties:
 * - Every level, except possibly the last, is completely filled.
 * - All nodes in the last level are as far left as possible.
 *
 * Approach:
 * - Compute the leftmost and rightmost heights of the tree.
 * - If left height == right height, the tree is a perfect binary tree:
 *   --> Number of nodes = (2^height) - 1
 * - Otherwise, recursively count nodes in the left and right subtree.
 *
 * Why is this faster?
 * - Perfect subtrees can be counted in O(1) instead of traversing all nodes.
 * - Recursion happens only where the tree is not perfect.
 *
 * Time Complexity: O(log² n)
 * - Each level we calculate height (O(log n))
 * - We do it for each recursive level (log n levels)
 * - Total = O(log n * log n) = O(log² n)
 *
 * Space Complexity: O(log n)
 * - Due to recursion depth for a balanced tree.
 */


public class CountCompleteTreeNodes {

    /**
     * Main function to count nodes in a complete binary tree
     */
    public int countNodes(TreeNode root) {
        if (root == null) return 0;

        int leftHeight = getLeftHeight(root);   // height by going only left
        int rightHeight = getRightHeight(root); // height by going only right

        if (leftHeight == rightHeight) {
            // Perfect binary tree: 2^height - 1 nodes
            return (1 << leftHeight) - 1;
        }

        // Otherwise, recurse for left and right subtrees
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    /**
     * Helper function to compute leftmost height of a tree
     */
    private int getLeftHeight(TreeNode node) {
        int height = 0;
        while (node != null) {
            height++;
            node = node.left; // only move left
        }
        return height;
    }

    /**
     * Helper function to compute rightmost height of a tree
     */
    private int getRightHeight(TreeNode node) {
        int height = 0;
        while (node != null) {
            height++;
            node = node.right; // only move right
        }
        return height;
    }
}


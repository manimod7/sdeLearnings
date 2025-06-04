package trees;

/*
 * Problem:
 * Two elements of a Binary Search Tree (BST) are swapped by mistake.
 * Recover the tree **without changing its structure**.
 *
 * Example:
 * Input Tree (Incorrect):
 *        3
 *       / \
 *      1   4
 *         /
 *        2
 * Here, nodes 2 and 3 are swapped.
 * Correct BST:
 *        2
 *       / \
 *      1   4
 *         /
 *        3
 *
 * Approach:
 * - In a BST, **inorder traversal** should produce a **sorted ascending sequence**.
 * - If two nodes are swapped, then during inorder traversal, we will detect violations:
 *   → Normally node.val > previousNode.val
 *   → When we find where current.val < previousNode.val, it means disorder.
 * - There can be **two violations**:
 *   1. First violation: first incorrect node (larger) and second incorrect node (smaller).
 *   2. Second violation (optional if not adjacent): update the second node again.
 * - After identifying the two wrong nodes, swap their values to fix the tree.
 *
 * Dry Run:
 * Inorder traversal of incorrect tree: [1, 3, 2, 4]
 * → 3 > 2 => first violation (first=3, second=2)
 * After fix, inorder will be [1, 2, 3, 4].
 *
 * Time Complexity: O(n)
 * - Visit each node once during inorder traversal.
 *
 * Space Complexity:
 * - O(h) → h = height of the tree (because of recursion stack in DFS)
 * - O(log n) for balanced tree, O(n) for skewed tree.
 */


public class RecoverBST {

    private TreeNode first = null;   // First wrong node
    private TreeNode second = null;  // Second wrong node
    private TreeNode prev = new TreeNode(Integer.MIN_VALUE); // Previous node during inorder traversal

    /**
     * Main function to recover the BST
     */
    public void recoverTree(TreeNode root) {
        inorder(root);

        // After traversal, first and second nodes are known
        // Swap their values to fix the BST
        if (first != null && second != null) {
            int temp = first.val;
            first.val = second.val;
            second.val = temp;
        }
    }

    /**
     * Inorder traversal to detect swapped nodes
     */
    private void inorder(TreeNode root) {
        if (root == null) return;

        // Visit left subtree
        inorder(root.left);

        // Process current node
        if (prev != null && root.val < prev.val) {
            // Found a violation
            if (first == null) {
                first = prev; // first time: previous is larger node
            }
            second = root;    // second time or same time: current smaller node
        }
        prev = root; // update previous node

        // Visit right subtree
        inorder(root.right);
    }
}


package trees;

/*
 * Problem:
 * Given two integer arrays inorder and postorder representing the inorder and postorder
 * traversal of a binary tree, construct and return the binary tree.
 *
 * Definitions:
 * - Inorder Traversal (Left → Root → Right)
 * - Postorder Traversal (Left → Right → Root)
 *
 * Approach:
 * - The last element in postorder array is always the root of the tree.
 * - Find the root's index in inorder array.
 * - Elements left of root in inorder array form the left subtree.
 * - Elements right of root in inorder array form the right subtree.
 * - Recursively build right subtree first, then left subtree because in postorder,
 *   right child comes before root while going backwards.
 *
 * Dry Run:
 * Example:
 * inorder = [9,3,15,20,7]
 * postorder = [9,15,7,20,3]
 *
 * Step 1: postorder[last]=3 → root
 * In inorder, 3 at index 1 → split into left=[9], right=[15,20,7]
 *
 * Step 2: Right Subtree:
 * postorder[last-1]=20 → root of right subtree
 * inorder: 20 at index 3 → left=[15], right=[7]
 *
 * Step 3: Build right and left subtree recursively...
 *
 * Time Complexity: O(n)
 * - Each node is processed once.
 * - HashMap gives O(1) lookup for root index.
 *
 * Space Complexity: O(n)
 * - HashMap stores n elements.
 * - Recursion stack up to height of tree (O(n) worst case).
 */

import java.util.HashMap;
import java.util.Map;

public class BuildTreeFromPostorderInorder {
    private Map<Integer, Integer> inorderIndexMap; // Maps value to its index in inorder
    private int postorderIndex; // Pointer to track current root index in postorder

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        inorderIndexMap = new HashMap<>();

        // Build value → index map for inorder traversal
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }

        postorderIndex = postorder.length - 1; // Start from last element in postorder

        // Start building the tree recursively
        return build(postorder, 0, inorder.length - 1);
    }

    /**
     * Recursive helper function to build the tree
     * @param postorder - postorder traversal array
     * @param inStart - start index in inorder array
     * @param inEnd - end index in inorder array
     * @return root node of subtree
     */
    private TreeNode build(int[] postorder, int inStart, int inEnd) {
        if (inStart > inEnd) {
            return null; // base case: invalid range
        }

        // Pick current root from postorder traversal
        int rootVal = postorder[postorderIndex--];
        TreeNode root = new TreeNode(rootVal); // create the root node

        // Find the root's index in inorder array
        int rootIndex = inorderIndexMap.get(rootVal);

        // IMPORTANT: Build right subtree first (since postorder is Left → Right → Root)
        root.right = build(postorder, rootIndex + 1, inEnd);

        // Build left subtree
        root.left = build(postorder, inStart, rootIndex - 1);

        return root;
    }
}


package trees;

/*
 * Problem:
 * Given two integer arrays `preorder` and `inorder` representing the preorder and inorder
 * traversal of a binary tree, construct and return the binary tree.
 *
 * Definitions:
 * - Preorder Traversal (Root → Left → Right): Root is visited before its subtrees.
 * - Inorder Traversal (Left → Root → Right): Left subtree is visited before root and right subtree.
 *
 * Approach:
 * - The first element of preorder array is always the root node.
 * - Find the root element in the inorder array.
 * - Elements left to the root in inorder form the left subtree.
 * - Elements right to the root in inorder form the right subtree.
 * - Recursively build left and right subtrees.
 *
 * Optimization:
 * - Use a HashMap to store inorder element → index for O(1) lookup instead of scanning array every time.
 *
 * Dry Run:
 * Example:
 * preorder = [3,9,20,15,7]
 * inorder = [9,3,15,20,7]
 *
 * Step 1: preorder[0]=3 → root
 * In inorder, 3 is at index 1 → split into left=[9], right=[15,20,7]
 *
 * Step 2: Left Subtree:
 * preorder[1]=9 → root of left subtree
 * In inorder, 9 is at index 0 → no left/right subtree (leaf node)
 *
 * Step 3: Right Subtree:
 * preorder[2]=20 → root of right subtree
 * In inorder, 20 is at index 3 → left=[15], right=[7]
 *
 * Step 4: Continue recursively building left and right...
 *
 * Time Complexity: O(n)
 * - Every node is processed exactly once.
 * - Lookup of root index in inorder is O(1) using HashMap.
 *
 * Space Complexity: O(n)
 * - HashMap stores n elements.
 * - Recursion call stack up to height of tree (O(n) in worst case unbalanced tree).
 */

import java.util.HashMap;
import java.util.Map;

// Definition for a binary tree node

public class BuildTreeFromPreorderInorder {
    private Map<Integer, Integer> inorderIndexMap; // Maps value to its index in inorder traversal
    private int preorderIndex = 0; // Tracks current root index in preorder array

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        inorderIndexMap = new HashMap<>();

        // Build value → index map for inorder traversal
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }

        // Start building the tree recursively
        return build(preorder, 0, inorder.length - 1);
    }

    /**
     * Recursive helper function to build tree
     * @param preorder - preorder traversal array
     * @param inStart - start index in inorder array
     * @param inEnd - end index in inorder array
     * @return root node of subtree
     */
    private TreeNode build(int[] preorder, int inStart, int inEnd) {
        // Base case: if invalid range
        if (inStart > inEnd) {
            return null;
        }

        // Pick current root from preorder traversal
        int rootVal = preorder[preorderIndex++];
        TreeNode root = new TreeNode(rootVal); // create the root node

        // Find the root's index in inorder array
        int rootIndex = inorderIndexMap.get(rootVal);

        // Recursively build left subtree from inorder[inStart to rootIndex-1]
        root.left = build(preorder, inStart, rootIndex - 1);

        // Recursively build right subtree from inorder[rootIndex+1 to inEnd]
        root.right = build(preorder, rootIndex + 1, inEnd);

        return root;
    }
}


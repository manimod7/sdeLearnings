package trees;

/*
 * Problem:
 * Given the root of a binary tree, return the leftmost value in the last (deepest) row of the tree.
 *
 * Constraints:
 * - There will always be at least one node in the tree.
 *
 * Approach:
 * - Use **Level Order Traversal (BFS)**:
 *   → Traverse level by level using a queue.
 *   → At each level, update the leftmost value (i.e., the first node at each level).
 *   → At the end of the traversal, the last recorded leftmost value is the answer.
 *
 * Dry Run Example:
 * Tree:
 *        1
 *       / \
 *      2   3
 *     /   / \
 *    4   5   6
 *       /
 *      7
 *
 * BFS Levels:
 * - Level 0: 1 → leftmost = 1
 * - Level 1: 2, 3 → leftmost = 2
 * - Level 2: 4, 5, 6 → leftmost = 4
 * - Level 3: 7 → leftmost = 7 (deepest leftmost node)
 *
 * Final answer = 7
 *
 * Time Complexity: O(n)
 * - Every node is visited exactly once.
 *
 * Space Complexity: O(n)
 * - Queue stores up to a full level of nodes.
 */

import java.util.LinkedList;
import java.util.Queue;


public class BottomLeftMost {

    public int findBottomLeftValue(TreeNode root) {
        if (root == null) return -1; // edge case if input is null

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int leftmostValue = root.val; // initialize with root value

        while (!queue.isEmpty()) {
            int size = queue.size(); // number of nodes at current level

            // For each level, capture the first node's value
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();

                if (i == 0) {
                    leftmostValue = current.val; // first node at this level
                }

                // Add children to queue for next level
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
        }

        return leftmostValue;
    }
}


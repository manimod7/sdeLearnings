package trees;

/*
💡 Leetcode Link:
https://leetcode.com/problems/path-sum-ii/

🧾 Problem Description:
Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where the sum
of the node values equals targetSum. A leaf is a node with no children. Each path should be returned
as a list of integers.

🔍 Example:
Input:
      5
     / \
    4   8
   /   / \
 11  13  4
/  \     / \
7    2   5   1

targetSum = 22

Output:
[
  [5,4,11,2],
  [5,8,4,5]
]

🧠 Approach (DFS + Backtracking):
1. Start DFS from root.
2. Track the current path and remaining sum.
3. If a leaf is reached and the sum equals target, add a copy of path to result.
4. Backtrack after each path to explore all combinations.

📦 Time Complexity: O(N^2)
- Each path could be up to length N, and we may create up to N paths.

📦 Space Complexity: O(H)
- For the recursion stack and temporary path (H = height of tree).
*/

import java.util.*;

public class PathSumII {
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(root, targetSum, new ArrayList<>(), result);
        return result;
    }

    private void dfs(TreeNode node, int remainingSum, List<Integer> path, List<List<Integer>> result) {
        if (node == null) return;

        // Add current node to the path
        path.add(node.val);

        // If it's a leaf and the remaining sum equals node's value, save the path
        if (node.left == null && node.right == null && node.val == remainingSum) {
            result.add(new ArrayList<>(path)); // Clone the current path
        } else {
            // Recur left and right with reduced sum
            dfs(node.left, remainingSum - node.val, path, result);
            dfs(node.right, remainingSum - node.val, path, result);
        }

        // Backtrack: remove the last node before going back
        path.remove(path.size() - 1);
    }
}


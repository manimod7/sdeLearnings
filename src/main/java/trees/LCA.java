package trees;

public class LCA {
    public TreeNode findLCA(TreeNode node, TreeNode p, TreeNode q) {
        // Base case: If the current node is null or matches one of the nodes, return it
        if (node == null || node == p || node == q) {
            return node;
        }

        // Recur for the left and right subtrees
        TreeNode leftLCA = findLCA(node.left, p, q);
        TreeNode rightLCA = findLCA(node.right, p, q);

        // If both left and right subtrees return non-null values, current node is the LCA
        if (leftLCA != null && rightLCA != null) {
            return node;
        }

        // Otherwise, return the non-null result
        return (leftLCA != null) ? leftLCA : rightLCA;
    }
    //Explanation of the Code
    //
    //    TreeNode Class:
    //        Represents a node in the binary tree with fields for value, left, and right child nodes.
    //
    //    Base Case:
    //        If the current node is null, or it matches either of the nodes (p or q), return the current node. This serves as the stopping condition for recursion.
    //
    //    Recursive Calls:
    //        The function is called recursively for the left and right children of the current node.
    //
    //    LCA Decision:
    //        If both recursive calls return non-null results, the current node is the LCA because both p and q are located in different subtrees of this node.
    //
    //    Return Value:
    //        If only one recursive call returns a non-null value, it means both p and q are in the same subtree. The non-null result is propagated upward as the LCA.
    //Time and Space Complexity
    //
    //    Time Complexity: O(n), where n is the number of nodes in the binary tree. This is because we visit each node once.
    //    Space Complexity: O(h), where h is the height of the binary tree. This is due to the recursive call stack. For a balanced binary tree, this is O(log n); for a skewed tree, it is O(n).
}

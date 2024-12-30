package trees;

public class ShortestDistanceBetweenTwoNodes {
    // Find the LCA of two nodes
    public TreeNode findLCA(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null || node == p || node == q) {
            return node;
        }

        TreeNode leftLCA = findLCA(node.left, p, q);
        TreeNode rightLCA = findLCA(node.right, p, q);

        if (leftLCA != null && rightLCA != null) {
            return node;
        }

        return (leftLCA != null) ? leftLCA : rightLCA;
    }

    // Find the distance from a given node to a target node
    public int findDistance(TreeNode node, TreeNode target, int distance) {
        if (node == null) {
            return -1; // Target not found
        }
        if (node == target) {
            return distance;
        }

        // Check in the left and right subtrees
        int leftDistance = findDistance(node.left, target, distance + 1);
        if (leftDistance != -1) {
            return leftDistance; // Target found in the left subtree
        }

        return findDistance(node.right, target, distance + 1); // Target in the right subtree
    }

    // Find the shortest distance between two nodes
    public int findShortestDistance(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode lca = findLCA(root, p, q);
        if (lca == null) {
            return -1; // LCA doesn't exist, nodes are not in the tree
        }

        int distanceToP = findDistance(lca, p, 0);
        int distanceToQ = findDistance(lca, q, 0);

        return distanceToP + distanceToQ;
    }
}

package trees;

public class PreOrder {
    public void preorderRecursive(TreeNode root) {
        if (root == null) return;
        System.out.print(root.val + " ");  // Visit node first
        preorderRecursive(root.left);
        preorderRecursive(root.right);
    }


}

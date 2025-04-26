package trees;

import java.util.LinkedList;
import java.util.Queue;

public class MaxDepth {
    public int maxDepthDFS(TreeNode root) {
        if (root == null) return 0;
        int left = maxDepthDFS(root.left);
        int right = maxDepthDFS(root.right);
        return 1 + Math.max(left, right);
    }

    public int maxDepthBFS(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode curr = queue.poll();
                if (curr.left != null) queue.offer(curr.left);
                if (curr.right != null) queue.offer(curr.right);
            }
            depth++;
        }

        return depth;
    }
}

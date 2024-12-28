package trees;

import java.util.ArrayList;

class Node {
    int data;
    Node left, right;
    Node (int x) {
        data = x;
        left = null;
        right = null;
    }
}

public class SerializationAndDeserializationOfTree {
    static void serializePreOrder(Node root, ArrayList<Integer> arr) {



        // Push -1 if root is null.
        if (root == null) {
            arr.add(-1);
            return;
        }

        // Push the root into result.
        arr.add(root.data);

        // Recursively traverse the
        // left and right subtree.
        serializePreOrder(root.left, arr);
        serializePreOrder(root.right, arr);
    }
}

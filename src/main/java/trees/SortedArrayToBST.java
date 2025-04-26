package trees;

public class SortedArrayToBST {
    /*
     * Problem:
     * Given an integer array nums where the elements are sorted in ascending order,
     * convert it to a height-balanced Binary Search Tree (BST).
     *
     * A height-balanced BST is a binary tree in which the depth of the two subtrees
     * of every node never differs by more than 1.
     *
     * Solution:
     * Use Divide and Conquer approach:
     * - Choose the middle element of the array as the root.
     * - Recursively do the same for the left half and right half of the array.
     *
     * Time Complexity: O(n)
     * - Every element is visited exactly once to create a TreeNode.
     *
     * Space Complexity: O(log n)
     * - Due to recursion stack in case of balanced BST (log n height).
     */

        /**
         * Main method to be called to convert a sorted array to a BST
         */
        public TreeNode sortedArrayToBST(int[] nums) {
            return buildBST(nums, 0, nums.length - 1);
        }

        /**
         * Recursive helper to build the BST
         * @param nums - the sorted array
         * @param left - left boundary of current subarray
         * @param right - right boundary of current subarray
         * @return root node of constructed subtree
         */
        private TreeNode buildBST(int[] nums, int left, int right) {
            if (left > right) return null; // base case: no elements to process

            int mid = left + (right - left) / 2; // pick middle element to ensure balance
            TreeNode root = new TreeNode(nums[mid]); // create root node with mid value

            // recursively build left subtree with left half
            root.left = buildBST(nums, left, mid - 1);

            // recursively build right subtree with right half
            root.right = buildBST(nums, mid + 1, right);

            return root;
        }
    }

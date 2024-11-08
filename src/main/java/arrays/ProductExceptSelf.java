package arrays;

import java.util.ArrayList;
import java.util.Collections;

public class ProductExceptSelf {
    public ArrayList<Integer> solve(ArrayList<Integer> nums) {
        int n = nums.size();
        ArrayList<Integer> leftProducts = new ArrayList<>(Collections.nCopies(n, 0));
        ArrayList<Integer> rightProducts = new ArrayList<>(Collections.nCopies(n, 0));
        ArrayList<Integer> output = new ArrayList<>(Collections.nCopies(n, 0));
        // Initialize the first element of leftProducts to 1
        leftProducts.set(0, 1);
        for (int i = 1; i < n; i++) {
            leftProducts.set(i, nums.get(i - 1) * leftProducts.get(i - 1));
        }
        // Initialize the last element of rightProducts to 1
        rightProducts.set(n - 1, 1);
        for (int i = n - 2; i >= 0; i--) {
            rightProducts.set(i, nums.get(i + 1) * rightProducts.get(i + 1));
        }
        // Fill the output ArrayList
        for (int i = 0; i < n; i++) {
            output.set(i, leftProducts.get(i) * rightProducts.get(i));
        }
        return output;
    }

}

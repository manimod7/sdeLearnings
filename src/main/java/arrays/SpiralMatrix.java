package arrays;
import java.util.*;

public class SpiralMatrix {
    public ArrayList<ArrayList<Integer>> generateMatrix(int A) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < A; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < A; j++) {
                row.add(null);
            }
            result.add(row);
        }

        int num = 1;
        int top = 0, bottom = A - 1, left = 0, right = A - 1;
        while (left <= right && top <= bottom) {
            // Traverse from left to right
            for (int i = left; i <= right; i++) {
                result.get(top).set(i, num++);
            }
            top++;
            // Traverse downwards
            for (int i = top; i <= bottom; i++) {
                result.get(i).set(right, num++);
            }
            right--;
            // Traverse from right to left
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    result.get(bottom).set(i, num++);
                }
                bottom--;
            }
            // Traverse upwards
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result.get(i).set(left, num++);
                }
                left++;
            }
        }
        return result;
    }

}

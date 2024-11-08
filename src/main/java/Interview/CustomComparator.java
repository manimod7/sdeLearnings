package Interview;

import java.util.ArrayList;
import java.util.Arrays;


public class CustomComparator {
    public static ArrayList<Integer> solve(ArrayList<Integer> nums) {
        nums.sort((x, y) -> {
            int xTenDigit = (x % 100 - x % 10) / 10;
            int yTenDigit = (y % 100 - y % 10) / 10;
            if (xTenDigit == yTenDigit)
                return y - x;
            return xTenDigit - yTenDigit;
        });
        return nums;
    }

    public static void main (String [] args) {
        ArrayList<Integer> nums = new ArrayList<>(Arrays.asList(12, 54, 78, 109, 11, 30, 0, 5, 71, 99, 850));
        int [] [] numbers = {{1,2,3},{4,5,6}};
        // Print the 2D array
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                System.out.print(numbers[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(nums);
        solve(nums);
        System.out.println(nums);
    }


}

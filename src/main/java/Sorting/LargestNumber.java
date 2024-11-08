package Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LargestNumber {
    public String largestNumber(ArrayList<Integer> A) {
        // Convert the integers to strings
        ArrayList<String> strNums = new ArrayList<>();
        for (Integer num : A) {
            strNums.add(String.valueOf(num));
        }

        // Define a custom comparator for sorting
        strNums.sort((a, b) -> {
            // Compare the concatenated strings in both possible orders
            String order1 = a + b;
            String order2 = b + a;
            return order2.compareTo(order1); // Sort in descending order
        });

        // Edge case: If the largest number is "0", return "0"
        if (!strNums.isEmpty() && strNums.get(0).equals("0")) {
            return "0";
        }
        // Build the largest number by concatenating the sorted strings
        StringBuilder largestNumber = new StringBuilder();
        for (String num : strNums) {
            largestNumber.append(num);
        }
        return largestNumber.toString();

    }
}

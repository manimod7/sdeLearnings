package arrays;

import java.util.ArrayList;
import java.util.List;

public class PrefixSumArray {

    public static List<Integer> calculatePrefixSumList(List<Integer> inputList) {
        List<Integer> prefixSumList = new ArrayList<>();

        if (inputList.isEmpty()) {
            return prefixSumList; // Return an empty list if input is empty
        }

        // The first element of the prefix sum list is the same as the first element of the input list
        prefixSumList.add(inputList.get(0));

        // Iterate through the input list starting from the second element
        for (int i = 1; i < inputList.size(); i++) {
            // Add the current element of the input list to the last element of the prefix sum list and add to the prefix sum list
            int sum = prefixSumList.get(i - 1) + inputList.get(i);
            prefixSumList.add(sum);
        }

        return prefixSumList;
    }

    public static int sumOfRange(List<Integer> array, int start, int end) {
        if(start<0 || end >=array.size())
            return 0;
        List<Integer> prefixSumList = calculatePrefixSumList(array);
        if(start==0)
            return prefixSumList.get(end);
        return prefixSumList.get(end) - prefixSumList.get(start-1);
    }
}

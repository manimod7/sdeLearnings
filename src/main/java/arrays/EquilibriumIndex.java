package arrays;

import java.util.ArrayList;
import java.util.List;

public class EquilibriumIndex {

    public static List<Integer> findEquilibriumIndices(List<Integer> arrList) {
        int n = arrList.size();
        List<Integer> prefixSum = new ArrayList<>();
        List<Integer> suffixSum = new ArrayList<>(n);
        List<Integer> equilibriumIndices = new ArrayList<>();

        // Initialize suffixSum with zeros to allow setting values in reverse order
        for (int i = 0; i < n; i++) {
            suffixSum.add(0);
        }

        // Calculate prefix sum
        prefixSum.add(arrList.get(0));
        for (int i = 1; i < n; i++) {
            prefixSum.add(prefixSum.get(i - 1) + arrList.get(i));
        }

        // Calculate suffix sum
        suffixSum.set(n - 1, arrList.get(n - 1));
        for (int i = n - 2; i >= 0; i--) {
            suffixSum.set(i, suffixSum.get(i + 1) + arrList.get(i));
        }

        // Find equilibrium indices
        for (int i = 0; i < n; i++) {
            if (prefixSum.get(i).equals(suffixSum.get(i))) {
                equilibriumIndices.add(i);
            }
        }

        return equilibriumIndices;
    }
}

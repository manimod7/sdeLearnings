package bitManipulation;

import java.util.ArrayList;

public class SubarraysWithOr1 {
    public int subarraysWithOR1(ArrayList<Integer> A) {
        int n = A.size();

        long totalSubarrays = (long) n * (n + 1) / 2;


        long zeroRunsSubarrays = 0;
        int zeroCount = 0;
        for (int x : A) {
            if (x == 0) {
                zeroCount++;
            } else {

                zeroRunsSubarrays += (long) zeroCount * (zeroCount + 1) / 2;
                zeroCount = 0;
            }
        }

        zeroRunsSubarrays += (long) zeroCount * (zeroCount + 1) / 2;


        long answer = totalSubarrays - zeroRunsSubarrays;

        return (int) answer;
    }
}

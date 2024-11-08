package bitManipulation;

import java.util.List;

public class AllNumbersThriceOneNumberOnce {
    /*
    Given an array of integers, every element appears thrice except for one, which occurs once.
    Find that element that does not appear thrice.
    */
    public int singleNumber(final List<Integer> A) {

        int n = A.size();
        int ans = 0;

        for (int i = 0; i < 32; i++) {

            int currentBitCount = 0;

            for (int j = 0; j < n; j++) {

                int currentBitSet = A.get(j) & 1 << i;

                if (currentBitSet != 0)
                    currentBitCount++;
            }

            if (currentBitCount % 3 == 1) {
                ans = ans | 1 << i;
            }

        }

        return ans;
    }
}

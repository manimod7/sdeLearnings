package Hashing;

public class LongestSubstringWithoutRepeat {
    public int lengthOfLongestSubstring(String A) {
        if (A == null || A.length() == 0) return 0;


        int[] lastIdx = new int[256];
        for (int k = 0; k < 256; k++) {
            lastIdx[k] = -1;
        }

        int maxLen = 0;
        int start = 0;

        for (int i = 0; i < A.length(); i++) {
            char c = A.charAt(i);
            if (lastIdx[c] >= start) {
                start = lastIdx[c] + 1;
            }

            lastIdx[c] = i;

            int len = i - start + 1;
            if (len > maxLen) {
                maxLen = len;
            }
        }

        return maxLen;

    }
}

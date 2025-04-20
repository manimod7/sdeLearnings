package string;

public class LongestPrefixSuffix {

    public static int[] computeLPSArray(String pattern) {
        int length = pattern.length();
        int[] lps = new int[length];
        int prefix = 0; // length of the previous longest prefix suffix
        int suffix = 1;

        // lps[0] is always 0, so we start from i = 1
        lps[0] = 0;

        while (suffix < length) {
            if (pattern.charAt(suffix) == pattern.charAt(prefix)) {
                prefix++;
                lps[suffix] = prefix;
                suffix++;
            } else { // pattern.charAt(i) != pattern.charAt(length)
                if (prefix != 0) {
                    // This is tricky. Consider the example.
                    // AAACAAAA and i = 7. The idea is similar
                    // to search step in KMP
                    prefix = lps[prefix - 1];

                    // Also, note that we do not increment i here
                } else { // if length is 0, then there is no prefix
                    // that is also a suffix for pattern[0...i]
                    lps[suffix] = 0;
                    suffix++;
                }
            }
        }
        return lps;
    }

    public static void main(String[] args) {
        String txt1 = "ABABCABAB";
        int[] lps1 = computeLPSArray(txt1);
        System.out.print("LPS array for " + txt1 + " is: ");
        for (int value : lps1) {
            System.out.print(value + " ");
        }
        System.out.println(); // Output: LPS array for ABABCABAB is: 0 0 1 2 0 1 2 3 4

        String txt2 = "AAAA";
        int[] lps2 = computeLPSArray(txt2);
        System.out.print("LPS array for " + txt2 + " is: ");
        for (int value : lps2) {
            System.out.print(value + " ");
        }
        System.out.println(); // Output: LPS array for AAAA is: 0 1 2 3

        String txt3 = "ABCDE";
        int[] lps3 = computeLPSArray(txt3);
        System.out.print("LPS array for " + txt3 + " is: ");
        for (int value : lps3) {
            System.out.print(value + " ");
        }
        System.out.println(); // Output: LPS array for ABCDE is: 0 0 0 0 0

        String txt4 = "AAACAAAA";
        int[] lps4 = computeLPSArray(txt4);
        System.out.print("LPS array for " + txt4 + " is: ");
        for (int value : lps4) {
            System.out.print(value + " ");
        }
        System.out.println(); // Output: LPS array for AAACAAAA is: 0 1 2 0 1 2 3 1
    }
}

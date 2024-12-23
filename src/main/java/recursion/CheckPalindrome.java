package recursion;

public class CheckPalindrome {
    public static boolean isPalindrome(String s, int start, int end) {
        // Base case: if start crosses end, or if they are the same, it's a palindrome
        if (start >= end) {
            return true;
        }
        // Check if the current characters are the same
        if (s.charAt(start) != s.charAt(end)) {
            return false;
        }
        // Move towards the middle
        return isPalindrome(s, start + 1, end - 1);
    }
    public int solve(String s) {
        int n = s.length();
        boolean ans  = isPalindrome(s,0,n-1);
        if(ans)
            return 1;
        return 0;
    }
}

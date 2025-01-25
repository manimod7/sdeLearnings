package backtracking;

import java.util.ArrayList;

public class GenerateAllValidParanthesis {

    public void helper(ArrayList<String> answer, String current, int open, int close, int currentLength, int n) {
        if(currentLength == 2*n && open == close) {
            answer.add(current);
            return;
        }
        if(open<n) {
            helper(answer, current+"(", open+1, close, currentLength+1, n);
        }
        if(close<open) {
            helper(answer, current+")", open, close+1, currentLength+1, n);

        }
    }

    public ArrayList<String> generateParenthesis(int n) {
        ArrayList<String> answer = new ArrayList<>();
        int open =0, close =0, currentLength =0;
        String current = "";
        helper(answer, current,open, close, currentLength, n);
        return answer;
    }
}

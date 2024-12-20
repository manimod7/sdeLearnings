package stack;

import java.util.ArrayList;
import java.util.Stack;

public class PassingGame {
    public int solve(int A, int B, ArrayList<Integer> C) {
        Stack<Integer> stack = new Stack<>();
        stack.push(B);
        for(int i=0;i<=B;i++) {
            int nextPlayer = C.get(i);
            if(nextPlayer!=0) {
                stack.push(nextPlayer);
            }
            else {
                if(!stack.empty()) {
                    stack.pop();
                }
            }
        }
        if(!stack.empty())
            return stack.peek();
        return 0;
    }
}

package stack;

import java.util.Stack;

public class BalancedParanthesis {
    public int solve(String A) {
        Stack<Character> stck = new Stack<>();

        for(char c : A.toCharArray()) {
            if(c=='{' || c=='['|| c=='(') {
                stck.push(c);
            }
            else if( c=='}'){
                if(stck.empty())
                    return 0;
                if(stck.peek()!='{') {
                    return 0;
                }
                if(stck.peek()=='{'){
                    stck.pop();
                }
            }

            else if( c==')'){
                if(stck.empty())
                    return 0;
                if(stck.peek()!='(') {
                    return 0;
                }
                if(stck.peek()=='('){
                    stck.pop();
                }
            }

            else if( c==']'){
                if(stck.empty())
                    return 0;
                if(stck.peek()!='[') {
                    return 0;
                }
                if(stck.peek()=='['){
                    stck.pop();
                }
            }
        }
        if(stck.empty())
            return 1;
        return 0;
    }
}

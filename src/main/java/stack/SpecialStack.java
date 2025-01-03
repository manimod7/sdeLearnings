package stack;

import java.util.Stack;

//When we insert 18, both stacks change to following.
//Actual Stack
//18 <--- top
//Auxiliary Stack
//18 <---- top
//
//When 19 is inserted, both stacks change to following.
//Actual Stack
//19 <--- top
//18
//Auxiliary Stack
//18 <---- top
//18
//
//When 29 is inserted, both stacks change to following.
//Actual Stack
//29 <--- top
//19
//18
//Auxiliary Stack
//18 <---- top
//18
//18
//
//When 15 is inserted, both stacks change to following.
//Actual Stack
//15 <--- top
//29
//19
//18
//Auxiliary Stack
//15 <---- top
//18
//18
//18
//
//When 16 is inserted, both stacks change to following.
//Actual Stack
//16 <--- top
//15
//29
//19
//18
//Auxiliary Stack
//15 <---- top
//15
//18
//18
//18
public class SpecialStack {
    private Stack<Integer> realStack = new Stack();
    private Stack<Integer> minStack = new Stack();
    public void push(int x) {
        if(!minStack.empty() && minStack.peek()<x) {
            minStack.push(minStack.peek());
        }
        else {
            minStack.push(x);
        }
        realStack.push(x);
    }

    public void pop() {
        if(!realStack.empty()) {
            realStack.pop();
        }
        if(!minStack.empty()) {
            minStack.pop();
        }
    }

    public int top() {
        if(!realStack.empty())
            return realStack.peek();
        return -1;
    }

    public int getMin() {
        if(!minStack.empty())
            return minStack.peek();
        return -1;
    }
}

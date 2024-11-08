package arrays;

import java.util.ArrayList;
public class ArrayRotation {
    public ArrayList<Integer> solve(ArrayList<Integer> A, int B) {
        ArrayList<Integer> ans = new ArrayList<>();
        int n = A.size();
        for (int i = 0; i < n; i++) {
            ans.add(0);
        }
        for (int i = 0; i < n; i++) {
            int newIndex = (i + B) % n;
            ans.set(newIndex, A.get(i));
        }
        return ans;
    }

    }

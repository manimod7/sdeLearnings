package arrays;

import java.util.ArrayList;

public class AddOneToNumber {
    public ArrayList<Integer> plusOne(ArrayList<Integer> A) {
        int n = A.size();
        A.set(n-1, A.get(n-1)+1);
        int z=0;
        for(z=0;z<n;z++){
            if(A.get(z)!=0)
                break;
        }
        if (z > 0) {
            A.subList(0, z).clear();
        }
        n = A.size();
        int carry = 0;
        for(int i=n-1;i>=0;i--){
            A.set(i, A.get(i) + carry);
            if(A.get(i)>9){
                carry = A.get(i)/10;
                A.set(i, A.get(i)%10);
            }
            else{
                carry = 0;
            }
        }
        if(carry>0)
            A.add(0,carry);
        return A;
    }

}

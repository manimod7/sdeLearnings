package bitManipulation;

public class NumberOfSetBits {
    public int numSetBits(int A) {
        int ans = 0;
        for(int i=0;i<32;i++) {
            int currentBit = 1<<i;
            int andWithCurrentBit = A & currentBit;
            if(andWithCurrentBit!=0)
                ans++;
        }
        return ans;
    }

}

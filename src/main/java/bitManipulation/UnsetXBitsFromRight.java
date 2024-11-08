package bitManipulation;

public class UnsetXBitsFromRight {
    public Long solve(Long A, int B) {
        Long unsetBitB = 0L;
        unsetBitB = ~unsetBitB;
        for(int i=0;i<B;i++) {
            unsetBitB = unsetBitB<<1;
            A = A & unsetBitB;
        }
        return A;
    }

}

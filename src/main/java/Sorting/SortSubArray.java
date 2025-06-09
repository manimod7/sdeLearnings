package Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortSubArray {
    public ArrayList<Integer> sortSubArray(ArrayList<Integer> A, int B, int C) {
        List<Integer> slice = A.subList(B, C + 1);
        Collections.sort(slice);
        return A;
    }
}

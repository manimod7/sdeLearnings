package searching;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MedianOfTwoSortedArraysTest {

    @Test
    public void testSolveOneArrayEmpty() {
        MedianOfTwoSortedArrays medianFinder = new MedianOfTwoSortedArrays();
        ArrayList<Integer> A = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>(Collections.singletonList(5));
        int result = medianFinder.solve(A, B);
        assertEquals(5, result);
    }

    @Test
    public void testSolveSameSingleElementInBothArrays() {
        MedianOfTwoSortedArrays medianFinder = new MedianOfTwoSortedArrays();
        ArrayList<Integer> A = new ArrayList<>(Collections.singletonList(3));
        ArrayList<Integer> B = new ArrayList<>(Collections.singletonList(3));
        int result = medianFinder.solve(A, B);
        assertEquals(3, result);
    }
    
    @Test
    public void testSolveEqualSizeDistinctElements() {
        MedianOfTwoSortedArrays medianFinder = new MedianOfTwoSortedArrays();
        ArrayList<Integer> A = new ArrayList<>(Arrays.asList(1, 3, 5));
        ArrayList<Integer> B = new ArrayList<>(Arrays.asList(2, 4, 6));
        int result = medianFinder.solve(A, B);
        assertEquals(3, result);
    }
    
}

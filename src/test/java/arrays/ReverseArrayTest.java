package arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ReverseArrayTest {

    @Test
    void testReverseListUsingCollection() {
        List<Integer> originalList = Arrays.asList(1, 2, 3, 4, 5);
        ReverseArray reverseArray = new ReverseArray(new ArrayList<>(originalList));
        List<Integer> reversedList = reverseArray.reverseListUsingCollection(reverseArray.getList());

        assertEquals(Arrays.asList(5, 4, 3, 2, 1), reversedList);
    }

    @Test
    void testReverseListUsingSwapping() {
        List<Integer> originalList = Arrays.asList(1, 2, 3, 4, 5);
        ReverseArray reverseArray = new ReverseArray(new ArrayList<>(originalList));
        List<Integer> reversedList = reverseArray.reverseListUsingSwapping(reverseArray.getList());

        assertEquals(Arrays.asList(5, 4, 3, 2, 1), reversedList);
    }
}

package arrays;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrefixSumArrayTest {

    @Test
    public void testRangeOfSum(){
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5);
        int sum = PrefixSumArray.sumOfRange(inputList,0, 4);
        assertEquals(15, sum);
        int sum2= PrefixSumArray.sumOfRange(inputList,1,2);
        assertEquals(5, sum2);
    }
}

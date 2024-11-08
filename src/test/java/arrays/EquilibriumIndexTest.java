package arrays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquilibriumIndexTest {
    @Test
    public void testWithPositiveNumbers() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 10);
        List<Integer> expected = Collections.emptyList();
        assertEquals(expected, EquilibriumIndex.findEquilibriumIndices(input));
    }

    @Test
    public void testWithNegativeNumbers() {
        List<Integer> input = Arrays.asList(-10, -2, -3, -4, -14);
        List<Integer> expected = Collections.emptyList();
        assertEquals(expected, EquilibriumIndex.findEquilibriumIndices(input));
    }

    @Test
    public void testWithMixedNumbers() {
        List<Integer> input = Arrays.asList(-7, 1, 5, 2, -4, 3, 0);
        List<Integer> expected = Arrays.asList(3, 6);
        assertEquals(expected, EquilibriumIndex.findEquilibriumIndices(input));
    }

    @Test
    public void testWithSingleElement() {
        List<Integer> input = Arrays.asList(1);
        List<Integer> expected = Arrays.asList(0);
        assertEquals(expected, EquilibriumIndex.findEquilibriumIndices(input));
    }

    @Test
    public void testWithNoEquilibrium() {
        List<Integer> input = Arrays.asList(1, 2, 3);
        List<Integer> expected = Arrays.asList();
        assertEquals(expected, EquilibriumIndex.findEquilibriumIndices(input));
    }
}

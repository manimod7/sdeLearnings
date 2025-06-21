package threads.forkjoin;

import java.util.concurrent.RecursiveAction;

/**
 * Example of {@link RecursiveAction} that sums a portion of an array.
 * The result is stored back into the first element of the range.
 */
public class SumRecursiveAction extends RecursiveAction {
    private static final int THRESHOLD = 1000;
    private final int[] array;
    private final int start;
    private final int end;

    public SumRecursiveAction(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            // perform the computation directly
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            array[start] = sum;
        } else {
            int mid = start + length / 2;
            SumRecursiveAction left = new SumRecursiveAction(array, start, mid);
            SumRecursiveAction right = new SumRecursiveAction(array, mid, end);
            // fork both subtasks
            invokeAll(left, right);
            // combine results from subtasks
            array[start] = array[start] + array[mid];
        }
    }
}

package heaps;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class MinCostToHireKWorkers {
    public double minCostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;
        double[][] workers = new double[n][2];

        // workers[i][0] = ratio (wage/quality)
        // workers[i][1] = quality
        for (int i = 0; i < n; i++) {
            workers[i][0] = (double) wage[i] / quality[i];
            workers[i][1] = quality[i];
        }

        // Sort by ratio ascending
        Arrays.sort(workers, (a, b) -> Double.compare(a[0], b[0]));

        return getMinCost(k, workers);
    }

    private static double getMinCost(int k, double[][] workers) {
        PriorityQueue<Double> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        double qualitySum = 0.0;
        double minCost = Double.MAX_VALUE;

        for (double[] worker : workers) {
            double ratio = worker[0];
            double q = worker[1];

            qualitySum += q;
            maxHeap.add(q);

            // Maintain only k workers
            if (maxHeap.size() > k) {
                qualitySum -= maxHeap.poll(); // Remove largest quality
            }

            if (maxHeap.size() == k) {
                minCost = Math.min(minCost, qualitySum * ratio);
            }
        }
        return minCost;
    }
}

package heaps;

import java.util.Collections;
import java.util.PriorityQueue;

/*
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */

public class MedianFinder {

    private final PriorityQueue<Integer> maxHeap; // Max-Heap for the left half
    private final PriorityQueue<Integer> minHeap; // Min-Heap for the right half

    public MedianFinder() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // Max-Heap
        minHeap = new PriorityQueue<>(); // Min-Heap
    }

    public void addNum(int num) {
        // Add to max-heap by default
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }

        // Balance the heaps
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        // If maxHeap has more elements, it has the median
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        // Otherwise, the median is the average of the two roots
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}

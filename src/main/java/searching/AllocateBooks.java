package searching;

import java.util.ArrayList;
import java.util.Collections;

public class AllocateBooks {
    public int books(ArrayList<Integer> A, int B) {
        int n = A.size();

        // Edge case: If the number of books is less than students
        if (n < B) {
            return -1;
        }

        // Step 1: Find the bounds for binary search
        int low = Collections.max(A); // Minimum number of pages a student can get
        int high = 0; // Maximum number of pages
        for (int pages : A) {
            high += pages;
        }

        int result = -1;

        // Step 2: Perform binary search
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (isFeasible(A, B, mid)) {
                result = mid; // Update result to the current mid
                high = mid - 1; // Try for a smaller maximum
            } else {
                low = mid + 1; // Try for a larger maximum
            }
        }

        return result;
    }

    // Helper function to check if allocation is feasible
    private boolean isFeasible(ArrayList<Integer> A, int B, int maxPages) {
        int studentsRequired = 1; // Start with the first student
        int currentPages = 0;

        for (int pages : A) {
            if (currentPages + pages > maxPages) {
                // Allocate books to the next student
                studentsRequired++;
                currentPages = pages;

                // If more students are required than available, return false
                if (studentsRequired > B) {
                    return false;
                }
            } else {
                // Accumulate pages for the current student
                currentPages += pages;
            }
        }

        return true;
    }
}

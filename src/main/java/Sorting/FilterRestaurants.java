package Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterRestaurants {
    public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice, int maxDistance) {
        ArrayList<Integer> filteredIds = new ArrayList();

        for (int[] restaurant : restaurants) {
            if ((veganFriendly == 0 || restaurant[2] == 1) && restaurant[3] <= maxPrice && restaurant[4] <= maxDistance) {
                filteredIds.add(restaurant[0]);
            }
        }

        filteredIds.sort((a, b) -> {
            // Find ratings for IDs 'a' and 'b'
            int ratingA = 0, ratingB = 0;

            // Find ratings for IDs 'a' and 'b'
            for (int[] restaurant : restaurants) {
                if (restaurant[0] == a) ratingA = restaurant[1];
                if (restaurant[0] == b) ratingB = restaurant[1];
            }

            // Sort by rating first, then by ID if ratings are equal
            if (ratingA != ratingB) {
                return ratingB - ratingA;
            }
            return b - a;
        });
        return filteredIds;
    }
}

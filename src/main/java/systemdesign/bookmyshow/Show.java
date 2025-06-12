package systemdesign.bookmyshow;

import java.util.ArrayList;
import java.util.List;

/**
 * Show for a movie with available seats.
 */
public class Show {
    private final Movie movie;
    private final List<Seat> seats = new ArrayList<>();

    public Show(Movie movie, int seatCount) {
        this.movie = movie;
        for (int i = 1; i <= seatCount; i++) {
            seats.add(new Seat(i));
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }
}

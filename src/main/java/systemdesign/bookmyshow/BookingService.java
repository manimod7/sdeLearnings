package systemdesign.bookmyshow;

/**
 * Service to book seats for a show.
 */
public class BookingService {
    public boolean bookSeat(Show show, int seatNumber) {
        for (Seat seat : show.getSeats()) {
            if (seat.getNumber() == seatNumber && !seat.isBooked()) {
                seat.book();
                return true;
            }
        }
        return false;
    }
}

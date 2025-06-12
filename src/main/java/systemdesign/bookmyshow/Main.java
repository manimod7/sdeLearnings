package systemdesign.bookmyshow;

/**
 * Demo for booking a seat.
 */
public class Main {
    public static void main(String[] args) {
        Movie movie = new Movie("Movie");
        Show show = new Show(movie, 10);
        BookingService service = new BookingService();
        boolean booked = service.bookSeat(show, 1);
        System.out.println("Seat booked: " + booked);
    }
}

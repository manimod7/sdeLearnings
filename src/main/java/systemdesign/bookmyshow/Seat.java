package systemdesign.bookmyshow;

/**
 * Represents a seat in a theatre.
 */
public class Seat {
    private final int number;
    private boolean booked;

    public Seat(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isBooked() {
        return booked;
    }

    public void book() {
        booked = true;
    }
}

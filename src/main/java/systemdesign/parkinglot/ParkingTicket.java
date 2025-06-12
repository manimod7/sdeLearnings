package systemdesign.parkinglot;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ticket issued when a vehicle parks.
 */
public class ParkingTicket {
    private static final AtomicInteger ID_SEQ = new AtomicInteger();

    private final int id;
    private final ParkingSpot spot;
    private final Instant entryTime;

    public ParkingTicket(ParkingSpot spot) {
        this.id = ID_SEQ.incrementAndGet();
        this.spot = spot;
        this.entryTime = Instant.now();
    }

    public int getId() {
        return id;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Instant getEntryTime() {
        return entryTime;
    }
}

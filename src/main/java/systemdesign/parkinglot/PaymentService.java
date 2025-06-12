package systemdesign.parkinglot;

import java.time.Duration;
import java.time.Instant;

/**
 * Calculates parking fees.
 */
public class PaymentService {
    private final double ratePerHour;

    public PaymentService(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public double calculateFee(ParkingTicket ticket) {
        Instant exitTime = Instant.now();
        long hours = Math.max(1, Duration.between(ticket.getEntryTime(), exitTime).toHours());
        return hours * ratePerHour;
    }
}

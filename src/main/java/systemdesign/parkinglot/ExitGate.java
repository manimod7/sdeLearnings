package systemdesign.parkinglot;

/**
 * Handles vehicle exits from the parking lot.
 */
public class ExitGate {
    private final PaymentService paymentService;

    public ExitGate(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public double exitVehicle(ParkingTicket ticket) {
        double fee = paymentService.calculateFee(ticket);
        ticket.getSpot().removeVehicle();
        return fee;
    }
}

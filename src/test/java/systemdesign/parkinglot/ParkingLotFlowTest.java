package systemdesign.parkinglot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotFlowTest {

    @Test
    public void testParkingFlow() {
        ParkingLot lot = new ParkingLot();
        ParkingFloor floor = new ParkingFloor(1);
        floor.addSpot(new ParkingSpot("1A", VehicleType.CAR));
        lot.addFloor(floor);

        EntryGate entryGate = new EntryGate(lot);
        Vehicle car = new Vehicle(VehicleType.CAR);
        ParkingTicket ticket = entryGate.parkVehicle(car);
        assertNotNull(ticket);
        assertFalse(ticket.getSpot().isFree());

        PaymentService paymentService = new PaymentService(10.0);
        ExitGate exitGate = new ExitGate(paymentService);
        double fee = exitGate.exitVehicle(ticket);
        assertEquals(10.0, fee, 0.001);
        assertTrue(ticket.getSpot().isFree());
    }
}

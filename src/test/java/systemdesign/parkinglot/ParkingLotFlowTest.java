package systemdesign.parkinglot;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertNotNull(ticket);
        Assert.assertFalse(ticket.getSpot().isFree());

        PaymentService paymentService = new PaymentService(10.0);
        ExitGate exitGate = new ExitGate(paymentService);
        double fee = exitGate.exitVehicle(ticket);
        Assert.assertEquals(10.0, fee, 0.001);
        Assert.assertTrue(ticket.getSpot().isFree());
    }
}

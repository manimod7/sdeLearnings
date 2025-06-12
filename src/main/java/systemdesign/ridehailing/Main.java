package systemdesign.ridehailing;

/**
 * Demo for RideService.
 */
public class Main {
    public static void main(String[] args) {
        Rider rider = new Rider("Alice");
        Driver driver = new Driver("Bob");
        new RideService().requestRide(rider, driver);
    }
}

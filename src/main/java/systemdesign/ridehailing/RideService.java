package systemdesign.ridehailing;

/**
 * Service matching riders with drivers.
 */
public class RideService {
    public void requestRide(Rider rider, Driver driver) {
        System.out.println(rider.getName() + " matched with " + driver.getName());
    }
}

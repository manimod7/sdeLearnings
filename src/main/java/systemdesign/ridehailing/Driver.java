package systemdesign.ridehailing;

/**
 * Driver offering rides.
 */
public class Driver {
    private final String name;

    public Driver(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

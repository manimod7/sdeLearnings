package systemdesign.fooddelivery;

/**
 * Restaurant entity.
 */
public class Restaurant {
    private final String name;

    public Restaurant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

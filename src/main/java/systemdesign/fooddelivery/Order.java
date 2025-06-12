package systemdesign.fooddelivery;

/**
 * Order placed by a customer.
 */
public class Order {
    private final Restaurant restaurant;
    private final String item;

    public Order(Restaurant restaurant, String item) {
        this.restaurant = restaurant;
        this.item = item;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getItem() {
        return item;
    }
}

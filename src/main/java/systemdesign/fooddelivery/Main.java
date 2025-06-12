package systemdesign.fooddelivery;

/**
 * Demo for DeliveryService.
 */
public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant("Tasty");
        Order order = new Order(restaurant, "Pizza");
        new DeliveryService().dispatch(order);
    }
}

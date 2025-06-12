package systemdesign.fooddelivery;

/**
 * Service to dispatch orders.
 */
public class DeliveryService {
    public void dispatch(Order order) {
        System.out.println("Dispatching " + order.getItem() + " from " +
                order.getRestaurant().getName());
    }
}

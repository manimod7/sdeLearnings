package systemdesign.splitwise;

import java.util.Map;

/**
 * Demo for the Splitwise service.
 */
public class Main {
    public static void main(String[] args) {
        SplitwiseService service = new SplitwiseService();
        User alice = new User("Alice");
        User bob = new User("Bob");
        service.addExpense(new Expense(alice, 100, Map.of(alice, 50.0, bob, 50.0)));
        System.out.println("Balance Bob: " + service.getBalance(bob));
    }
}

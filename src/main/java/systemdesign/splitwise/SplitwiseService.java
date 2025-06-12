package systemdesign.splitwise;

import java.util.HashMap;
import java.util.Map;

/**
 * Very small Splitwise-like service keeping track of balances.
 */
public class SplitwiseService {
    private final Map<User, Double> balanceMap = new HashMap<>();

    public void addExpense(Expense expense) {
        double amount = expense.getAmount();
        User paidBy = expense.getPaidBy();
        balanceMap.put(paidBy, balanceMap.getOrDefault(paidBy, 0.0) + amount);
        for (Map.Entry<User, Double> entry : expense.getShares().entrySet()) {
            balanceMap.put(entry.getKey(),
                    balanceMap.getOrDefault(entry.getKey(), 0.0) - entry.getValue());
        }
    }

    public double getBalance(User user) {
        return balanceMap.getOrDefault(user, 0.0);
    }
}

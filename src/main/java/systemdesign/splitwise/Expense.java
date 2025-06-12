package systemdesign.splitwise;

import java.util.Map;

/**
 * Expense with shares per user.
 */
public class Expense {
    private final User paidBy;
    private final double amount;
    private final Map<User, Double> shares;

    public Expense(User paidBy, double amount, Map<User, Double> shares) {
        this.paidBy = paidBy;
        this.amount = amount;
        this.shares = shares;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public double getAmount() {
        return amount;
    }

    public Map<User, Double> getShares() {
        return shares;
    }
}

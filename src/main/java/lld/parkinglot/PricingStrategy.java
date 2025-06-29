package lld.parkinglot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Strategy interface for different pricing models.
 */
public interface PricingStrategy {
    BigDecimal calculateFee(ParkingTicket ticket, LocalDateTime exitTime);
    String getStrategyName();
}

/**
 * Hourly pricing strategy.
 */
class HourlyPricingStrategy implements PricingStrategy {
    private final BigDecimal hourlyRate;
    
    public HourlyPricingStrategy(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    @Override
    public BigDecimal calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
        long minutes = java.time.Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60); // Round up to next hour
        
        BigDecimal baseFee = hourlyRate.multiply(BigDecimal.valueOf(hours));
        double multiplier = ticket.getVehicle().getFeeMultiplier();
        
        return baseFee.multiply(BigDecimal.valueOf(multiplier));
    }
    
    @Override
    public String getStrategyName() {
        return "Hourly Pricing";
    }
}
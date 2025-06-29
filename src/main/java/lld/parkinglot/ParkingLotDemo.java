package lld.parkinglot;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Comprehensive demo of the Parking Lot system.
 * Demonstrates all major features and design patterns.
 */
public class ParkingLotDemo {
    
    public static void main(String[] args) {
        System.out.println("🏢 Comprehensive Parking Lot Management System Demo 🏢");
        System.out.println("=" * 60);
        
        // Create parking lot
        ParkingLot parkingLot = createParkingLot();
        
        // Add event listener for monitoring
        parkingLot.addEventListener(new DemoEventListener());
        
        // Demo scenarios
        demonstrateBasicParking(parkingLot);
        demonstrateAdvancedFeatures(parkingLot);
        demonstratePricingStrategies(parkingLot);
        displayFinalStatistics(parkingLot);
        
        System.out.println("\n🎉 Demo completed successfully! 🎉");
    }
    
    private static ParkingLot createParkingLot() {
        System.out.println("\n🏗️ Creating parking lot with multiple levels...");
        
        ParkingLot lot = new ParkingLot("LOT001", "Downtown Parking Center", "123 Main St");
        
        // Create Level 1
        Level level1 = new Level(1);
        addSpotsToLevel(level1, 1, 20, 30, 15, 3, 2, 1); // M, C, L, H, E, V
        lot.addLevel(level1);
        
        // Create Level 2  
        Level level2 = new Level(2);
        addSpotsToLevel(level2, 2, 15, 35, 20, 2, 3, 1);
        lot.addLevel(level2);
        
        // Create Level 3
        Level level3 = new Level(3);
        addSpotsToLevel(level3, 3, 10, 25, 25, 2, 2, 2);
        lot.addLevel(level3);
        
        System.out.println("✅ Parking lot created with 3 levels");
        return lot;
    }
    
    private static void addSpotsToLevel(Level level, int levelNum, int motorcycleSpots, 
                                       int compactSpots, int largeSpots, int handicappedSpots,
                                       int electricSpots, int vipSpots) {
        int spotCounter = 1;
        
        // Add motorcycle spots
        for (int i = 0; i < motorcycleSpots; i++) {
            String spotId = String.format("L%d-M-%03d", levelNum, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotId, SpotType.MOTORCYCLE, levelNum, "M", i + 1);
            level.addSpot(spot);
        }
        
        // Add compact spots
        for (int i = 0; i < compactSpots; i++) {
            String spotId = String.format("L%d-C-%03d", levelNum, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotId, SpotType.COMPACT, levelNum, "C", i + 1);
            level.addSpot(spot);
        }
        
        // Add large spots
        for (int i = 0; i < largeSpots; i++) {
            String spotId = String.format("L%d-L-%03d", levelNum, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotId, SpotType.LARGE, levelNum, "L", i + 1);
            level.addSpot(spot);
        }
        
        // Add handicapped spots
        for (int i = 0; i < handicappedSpots; i++) {
            String spotId = String.format("L%d-H-%03d", levelNum, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotId, SpotType.HANDICAPPED, levelNum, "H", i + 1);
            level.addSpot(spot);
        }
        
        // Add electric spots
        for (int i = 0; i < electricSpots; i++) {
            String spotId = String.format("L%d-E-%03d", levelNum, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotId, SpotType.ELECTRIC, levelNum, "E", i + 1);
            spot.setHasChargingStation(true);
            level.addSpot(spot);
        }
        
        // Add VIP spots
        for (int i = 0; i < vipSpots; i++) {
            String spotId = String.format("L%d-V-%03d", levelNum, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotId, SpotType.VIP, levelNum, "V", i + 1);
            spot.setHasCover(true);
            spot.setSecurityLevel(5);
            level.addSpot(spot);
        }
        
        System.out.println(String.format("  Level %d: %d spots (%dM, %dC, %dL, %dH, %dE, %dV)",
                         levelNum, spotCounter - 1, motorcycleSpots, compactSpots, largeSpots,
                         handicappedSpots, electricSpots, vipSpots));
    }
    
    private static void demonstrateBasicParking(ParkingLot parkingLot) {
        System.out.println("\n🚗 Demonstrating basic parking operations...");
        
        // Create various vehicles
        Vehicle motorcycle = new Motorcycle("BIKE123", "John Rider", "555-0101");
        motorcycle.setColor("Red");
        motorcycle.setModel("Harley Davidson");
        motorcycle.setYear(2022);
        
        Vehicle car = new Car("CAR456", "Jane Driver", "555-0102");
        car.setColor("Blue");
        car.setModel("Toyota Camry");
        car.setYear(2023);
        
        Vehicle truck = new Truck("TRUCK789", "Bob Hauler", "555-0103");
        truck.setColor("White");
        truck.setModel("Ford F-150");
        truck.setYear(2021);
        
        Vehicle electricCar = new ElectricCar("EV001", "Alice Green", "555-0104");
        electricCar.setColor("Silver");
        electricCar.setModel("Tesla Model 3");
        electricCar.setYear(2024);
        
        // Park vehicles
        System.out.println("\n🎫 Parking vehicles...");
        ParkingTicket motorcycleTicket = parkingLot.parkVehicle(motorcycle);
        ParkingTicket carTicket = parkingLot.parkVehicle(car);
        ParkingTicket truckTicket = parkingLot.parkVehicle(truck);
        ParkingTicket evTicket = parkingLot.parkVehicle(electricCar);
        
        // Display initial capacity
        displayCapacityInfo(parkingLot);
    }
    
    private static void demonstrateAdvancedFeatures(ParkingLot parkingLot) {
        System.out.println("\n🎯 Demonstrating advanced features...");
        
        // Test vehicle search
        System.out.println("\n🔍 Finding parked vehicles:");
        Vehicle foundVehicle = parkingLot.findVehicle("CAR456");
        if (foundVehicle != null) {
            System.out.println("  Found: " + foundVehicle.getDisplayInfo());
        }
        
        // Test spot reservation
        System.out.println("\n📅 Testing spot reservation:");
        SpotReservation reservation = parkingLot.reserveSpot(VehicleType.CAR, 60);
        if (reservation != null) {
            System.out.println("  Reserved spot: " + reservation.getSpot().getSpotId() + 
                             " for " + reservation.getDurationMinutes() + " minutes");
        }
        
        // Test out of order functionality
        System.out.println("\n🔧 Testing maintenance mode:");
        ParkingSpot spotToMaintain = parkingLot.findAvailableSpot(VehicleType.CAR);
        if (spotToMaintain != null) {
            spotToMaintain.markOutOfOrder("Cleaning and maintenance");
            System.out.println("  Marked spot " + spotToMaintain.getSpotId() + " as out of order");
            
            // Restore after demo
            spotToMaintain.restoreToService();
            System.out.println("  Restored spot " + spotToMaintain.getSpotId() + " to service");
        }
    }
    
    private static void demonstratePricingStrategies(ParkingLot parkingLot) {
        System.out.println("\n💰 Demonstrating pricing strategies...");
        
        // Get a parked vehicle for exit demo
        if (!parkingLot.getActiveTickets().isEmpty()) {
            ParkingTicket ticket = parkingLot.getActiveTickets().iterator().next();
            String ticketId = ticket.getTicketId();
            
            System.out.println("\n🚪 Processing vehicle exit:");
            System.out.println("  Vehicle: " + ticket.getVehicle().getDisplayInfo());
            System.out.println("  Parked at: " + ticket.getSpot().getSpotId());
            System.out.println("  Duration: " + ticket.getParkingDurationMinutes() + " minutes");
            
            // Calculate fee with current strategy
            PaymentInfo payment = parkingLot.processExit(ticketId);
            if (payment != null) {
                System.out.println("  Fee: $" + payment.getFee());
                System.out.println("  ✅ Payment processed successfully");
            }
        }
    }
    
    private static void displayCapacityInfo(ParkingLot parkingLot) {
        System.out.println("\n📊 Current parking lot status:");
        ParkingLotStats stats = parkingLot.getStatistics();
        
        System.out.println(String.format("  Overall: %d/%d spots occupied (%.1f%% full)",
                         stats.getOccupiedSpots(), stats.getTotalSpots(), 
                         stats.getOccupancyRate() * 100));
        
        System.out.println("  By spot type:");
        for (Map.Entry<SpotType, CapacityInfo> entry : stats.getCapacityByType().entrySet()) {
            SpotType spotType = entry.getKey();
            CapacityInfo info = entry.getValue();
            if (info.getTotal() > 0) {
                System.out.println(String.format("    %s: %d/%d available",
                                 spotType.getDisplayName(), info.getAvailable(), info.getTotal()));
            }
        }
    }
    
    private static void displayFinalStatistics(ParkingLot parkingLot) {
        System.out.println("\n📈 Final Statistics:");
        ParkingLotStats stats = parkingLot.getStatistics();
        
        System.out.println("  Parking Lot: " + parkingLot.getName());
        System.out.println("  Address: " + parkingLot.getAddress());
        System.out.println("  Levels: " + stats.getLevels());
        System.out.println("  Total Spots: " + stats.getTotalSpots());
        System.out.println("  Currently Occupied: " + stats.getOccupiedSpots());
        System.out.println("  Available: " + stats.getAvailableSpots());
        System.out.println("  Occupancy Rate: " + String.format("%.1f%%", stats.getOccupancyRate() * 100));
        System.out.println("  Active Tickets: " + stats.getActiveTickets());
        
        System.out.println("\n  Parked Vehicles:");
        for (Vehicle vehicle : parkingLot.getParkedVehicles()) {
            System.out.println("    " + vehicle.getDisplayInfo());
        }
    }
    
    /**
     * Demo event listener to showcase Observer pattern.
     */
    static class DemoEventListener implements ParkingEventListener {
        
        @Override
        public void onVehicleParked(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket) {
            System.out.println(String.format("  ✅ %s parked at %s (Ticket: %s)",
                             vehicle.getType().getDisplayName() + " " + vehicle.getLicensePlate(),
                             spot.getSpotId(),
                             ticket.getTicketId().substring(0, 8) + "..."));
        }
        
        @Override
        public void onVehicleExited(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket, BigDecimal fee) {
            System.out.println(String.format("  🚪 %s exited from %s (Fee: $%s, Duration: %d min)",
                             vehicle.getLicensePlate(),
                             spot.getSpotId(),
                             fee,
                             ticket.getParkingDurationMinutes()));
        }
        
        @Override
        public void onParkingFailed(Vehicle vehicle, String reason) {
            System.out.println(String.format("  ❌ Parking failed for %s: %s",
                             vehicle.getLicensePlate(), reason));
        }
        
        @Override
        public void onCapacityChanged(Map<SpotType, CapacityInfo> capacityInfo) {
            // Could log capacity changes for monitoring
        }
        
        @Override
        public void onSpotReserved(ParkingSpot spot, VehicleType vehicleType, int durationMinutes) {
            System.out.println(String.format("  📅 Reserved %s for %s (%d minutes)",
                             spot.getSpotId(), vehicleType.getDisplayName(), durationMinutes));
        }
    }
}
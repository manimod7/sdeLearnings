# Parking Lot System - Low Level Design

## 🎯 Problem Statement (Classic Interview Question)
Design a parking lot system that can accommodate different types of vehicles (cars, trucks, motorcycles) across multiple levels. The system should efficiently allocate parking spots, track occupancy, handle payments, and provide real-time information about availability.

## 📋 Requirements

### Functional Requirements
- 🚗 Support multiple vehicle types (Motorcycle, Car, Truck)
- 🏢 Multi-level parking structure with different spot sizes
- 🎫 Entry/exit ticket system with timestamp tracking
- 💰 Flexible pricing strategy (hourly, daily, flat rate)
- 📊 Real-time availability tracking per vehicle type
- 🔍 Find nearest available spot for a vehicle type
- 💳 Multiple payment methods (Cash, Credit Card, Mobile Payment)
- 📈 Occupancy reporting and analytics
- 🚫 Handle parking violations and overstay penalties

### Non-Functional Requirements
- ⚡ Fast spot allocation (< 100ms response time)
- 📊 Support thousands of parking spots
- 🧵 Thread-safe for concurrent operations
- 📈 Scalable to multiple parking facilities
- 💾 Persistent storage for tickets and payments
- 🔧 Extensible for new vehicle types and pricing models

## 🏗️ Architecture & Design Patterns

### Primary Design Patterns

#### 1. Factory Pattern
- **Purpose**: Create different types of vehicles and parking spots
- **Implementation**: `VehicleFactory`, `ParkingSpotFactory`
- **Benefits**: 
  - Centralized object creation
  - Easy to add new vehicle types
  - Encapsulation of creation logic

#### 2. Strategy Pattern
- **Purpose**: Different pricing strategies for different scenarios
- **Implementation**: `PricingStrategy` interface with multiple implementations
- **Benefits**: 
  - Runtime pricing strategy selection
  - Easy to add new pricing models
  - Clean separation of pricing logic

#### 3. Observer Pattern
- **Purpose**: Notify systems when parking events occur
- **Implementation**: `ParkingEventListener` for capacity updates, notifications
- **Benefits**: 
  - Real-time capacity monitoring
  - Integration with external systems
  - Loose coupling between components

#### 4. Singleton Pattern
- **Purpose**: Single parking lot instance per facility
- **Implementation**: `ParkingLot` singleton per location
- **Benefits**: Centralized state management

## 🎨 UML Class Diagram

```
                    ┌─────────────────────────┐
                    │     ParkingLot          │
                    ├─────────────────────────┤
                    │ - levels: List<Level>   │
                    │ - entryGates: List      │
                    │ - exitGates: List       │
                    │ - ticketSystem: System  │
                    │ - paymentSystem: System │
                    ├─────────────────────────┤
                    │ + parkVehicle()         │
                    │ + removeVehicle()       │
                    │ + getAvailableSpots()   │
                    │ + generateTicket()      │
                    │ + processPayment()      │
                    └─────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
        ┌─────────────────────┐ ┌─────────────────────┐
        │      Level          │ │    ParkingSpot      │
        ├─────────────────────┤ ├─────────────────────┤
        │ - levelNumber: int  │ │ - id: String        │
        │ - spots: List       │ │ - type: SpotType    │
        │ - capacity: Map     │ │ - vehicle: Vehicle  │
        ├─────────────────────┤ │ - isOccupied: bool  │
        │ + findAvailable()   │ ├─────────────────────┤
        │ + getOccupancy()    │ │ + park()            │
        │ + addSpot()         │ │ + removeVehicle()   │
        └─────────────────────┘ │ + canFitVehicle()   │
                                └─────────────────────┘
                                          │
                                          │ contains
                                          ▼
                    ┌─────────────────────────────────┐
                    │        Vehicle                  │
                    ├─────────────────────────────────┤
                    │ - licensePlate: String          │
                    │ - type: VehicleType             │
                    │ - size: VehicleSize             │
                    ├─────────────────────────────────┤
                    │ + getType()                     │
                    │ + canFitInSpot()                │
                    └─────────────────────────────────┘
                                          △
                                          │
                    ┌─────────────────────┼─────────────────────┐
                    │                     │                     │
                    ▼                     ▼                     ▼
        ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
        │   Motorcycle    │ │      Car        │ │     Truck       │
        ├─────────────────┤ ├─────────────────┤ ├─────────────────┤
        │ + canFitInSpot()│ │ + canFitInSpot()│ │ + canFitInSpot()│
        └─────────────────┘ └─────────────────┘ └─────────────────┘

                    ┌─────────────────────────────────┐
                    │     PricingStrategy             │
                    ├─────────────────────────────────┤
                    │ + calculateFee(ticket, exit)    │
                    │ + getStrategyName()             │
                    └─────────────────────────────────┘
                                          △
                                          │
                    ┌─────────────────────┼─────────────────────┐
                    │                     │                     │
                    ▼                     ▼                     ▼
        ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
        │ HourlyPricing   │ │  FlatRatePricing│ │ WeekendPricing  │
        ├─────────────────┤ ├─────────────────┤ ├─────────────────┤
        │ - hourlyRate    │ │ - flatRate      │ │ - weekdayRate   │
        │ + calculateFee()│ │ + calculateFee()│ │ - weekendRate   │
        └─────────────────┘ └─────────────────┘ │ + calculateFee()│
                                                └─────────────────┘

                    ┌─────────────────────────────────┐
                    │       ParkingTicket             │
                    ├─────────────────────────────────┤
                    │ - ticketId: String              │
                    │ - vehicle: Vehicle              │
                    │ - spot: ParkingSpot             │
                    │ - entryTime: DateTime           │
                    │ - exitTime: DateTime            │
                    │ - fee: BigDecimal               │
                    ├─────────────────────────────────┤
                    │ + calculateDuration()           │
                    │ + calculateFee()                │
                    │ + isValid()                     │
                    └─────────────────────────────────┘
```

## 📊 Time & Space Complexity Analysis

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|--------|
| Find Available Spot | O(L × S) | O(1) | L=levels, S=spots per level |
| Park Vehicle | O(L × S) | O(1) | Find spot + allocation |
| Remove Vehicle | O(1) | O(1) | Direct spot access via ticket |
| Generate Ticket | O(1) | O(1) | Object creation and storage |
| Calculate Parking Fee | O(1) | O(1) | Time-based calculation |
| Get Availability Count | O(L × S) | O(1) | Count available spots |
| Payment Processing | O(1) | O(1) | Payment gateway call |
| Spot Lookup by ID | O(1) | O(1) | HashMap lookup |

**Overall Space Complexity**: O(L × S + V + T) where L=levels, S=spots, V=vehicles, T=tickets

## 🔄 Parking Flow

```
Vehicle Arrives
      │
      ▼
┌─────────────────┐
│ Entry Gate      │
│ Scans License   │
└─────────────────┘
      │
      ▼
┌─────────────────┐     ┌─────────────────┐
│ Find Available  │ No  │ Display "LOT    │
│ Spot for Vehicle│────▶│ FULL" Message  │
│ Type            │     └─────────────────┘
└─────────────────┘
      │ Yes
      ▼
┌─────────────────┐
│ Allocate Spot & │
│ Generate Ticket │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ Display Spot    │
│ Information     │
│ to Driver       │
└─────────────────┘
      │
      ⋮ (Vehicle Parked)
      ⋮
      ▼
┌─────────────────┐
│ Vehicle Returns │
│ to Exit Gate    │
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ Scan Ticket &   │
│ Calculate Fee   │
└─────────────────┘
      │
      ▼
┌─────────────────┐     ┌─────────────────┐
│ Process Payment │────▶│ Release Spot &  │
│ (Multiple       │     │ Update Capacity │
│ Methods)        │     └─────────────────┘
└─────────────────┘
      │
      ▼
┌─────────────────┐
│ Exit Gate Opens │
│ Vehicle Leaves  │
└─────────────────┘
```

## 🎯 Spot Allocation Strategy

### Size-Based Allocation Algorithm
```java
public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
    // 1. Try to find exact size match first
    ParkingSpot exactMatch = findSpotByType(vehicleType.getPreferredSpotType());
    if (exactMatch != null) return exactMatch;
    
    // 2. Try larger spots if exact match not available
    for (SpotType spotType : getCompatibleSpotTypes(vehicleType)) {
        ParkingSpot spot = findSpotByType(spotType);
        if (spot != null) return spot;
    }
    
    return null; // No available spots
}

// Compatibility matrix
// Motorcycle: MOTORCYCLE, COMPACT, LARGE
// Car: COMPACT, LARGE  
// Truck: LARGE only
```

## 💰 Pricing Strategies

### 1. Hourly Pricing
```java
public BigDecimal calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
    Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
    long hours = Math.max(1, duration.toHours()); // Minimum 1 hour
    return hourlyRate.multiply(BigDecimal.valueOf(hours));
}
```

### 2. Flat Rate Pricing
```java
public BigDecimal calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
    return flatRate; // Same fee regardless of duration
}
```

### 3. Weekend Pricing
```java
public BigDecimal calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
    DayOfWeek day = exitTime.getDayOfWeek();
    BigDecimal rate = (day == SATURDAY || day == SUNDAY) ? weekendRate : weekdayRate;
    
    Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
    long hours = Math.max(1, duration.toHours());
    return rate.multiply(BigDecimal.valueOf(hours));
}
```

## ⚖️ Pros and Cons

### Pros
✅ **Flexible Vehicle Support**: Easy to add new vehicle types  
✅ **Dynamic Pricing**: Multiple pricing strategies for different scenarios  
✅ **Real-time Tracking**: Live capacity monitoring and availability  
✅ **Scalable Design**: Multi-level support with configurable capacity  
✅ **Payment Flexibility**: Multiple payment methods supported  
✅ **Thread Safety**: Concurrent operations for multiple entry/exit points  
✅ **Extensible Architecture**: Clean interfaces for adding features  
✅ **Efficient Allocation**: Optimized spot finding algorithms  

### Cons
❌ **Memory Usage**: Stores all parking spots and tickets in memory  
❌ **Single Point of Failure**: Centralized parking lot management  
❌ **Scalability Limits**: Linear search for spot allocation  
❌ **No Reservations**: No advance booking system  
❌ **Limited Analytics**: Basic reporting capabilities  

## 🚀 Scalability & Extensibility

### Performance Optimizations
- **Spot Indexing**: HashMap index by spot type for O(1) lookup
- **Availability Caching**: Cache availability counts per level/type
- **Database Optimization**: Indexes on vehicle license plate, ticket ID
- **Connection Pooling**: Efficient database connection management

### Horizontal Scaling
- **Microservices**: Separate services for parking, payment, notification
- **Database Sharding**: Partition by parking lot location
- **Load Balancing**: Distribute requests across multiple instances
- **Message Queues**: Async processing of parking events

### Feature Extensions
- **Reservation System**: Advance booking with time slots
- **VIP Parking**: Premium spots with different pricing
- **Electric Vehicle Support**: EV charging station integration
- **Mobile App Integration**: QR code tickets, mobile payments
- **Analytics Dashboard**: Advanced reporting and insights
- **Dynamic Pricing**: Demand-based pricing adjustments
- **Security Integration**: Camera systems and license plate recognition

## 🧪 Edge Cases Handled

1. **Concurrent Access**: Thread-safe spot allocation and deallocation
2. **Invalid Tickets**: Validation of ticket authenticity and expiration
3. **Payment Failures**: Graceful handling of payment processing errors
4. **Overstay Penalties**: Additional charges for extended parking
5. **System Failures**: Graceful degradation and recovery mechanisms
6. **Capacity Overflow**: Handling when parking lot exceeds capacity
7. **Price Changes**: Dynamic pricing updates without service interruption
8. **Lost Tickets**: Alternative exit procedures and penalty fees

## 🏭 Production Considerations

### Monitoring & Observability
- **Capacity Metrics**: Real-time occupancy rates and trends
- **Revenue Tracking**: Daily/monthly revenue analytics
- **Performance Monitoring**: Response times for spot allocation
- **Error Tracking**: Payment failures, system errors
- **User Analytics**: Peak usage times, average stay duration

### Integration Points
- **Payment Gateways**: Credit card, mobile payment processing
- **Notification Systems**: SMS, email, push notifications
- **Security Systems**: Camera integration, license plate recognition
- **External APIs**: Traffic management, navigation apps
- **Mobile Applications**: Real-time availability, mobile payments

### Data Management
- **Backup Strategy**: Regular backups of ticket and payment data
- **Data Retention**: Policies for historical parking data
- **Privacy Compliance**: GDPR compliance for license plate data
- **Audit Trails**: Complete logging of all parking transactions

## 🧰 How to Run

### Prerequisites
```bash
Java 11+
Maven 3.6+
```

### Run Demo
```bash
# Compile the project
mvn compile

# Run the demo
java -cp target/classes lld.parkinglot.ParkingLotDemo
```

### Sample Output
```
🏢 Parking Lot Management System Demo 🏢

🚗 Creating parking lot with 3 levels...
✅ Level 1: 50 spots (20 Motorcycle, 20 Compact, 10 Large)
✅ Level 2: 50 spots (15 Motorcycle, 25 Compact, 10 Large)  
✅ Level 3: 40 spots (10 Motorcycle, 20 Compact, 10 Large)

🎫 Parking vehicles...
🏍️ Motorcycle ABC123 parked at Level-1-M-001
🚗 Car XYZ789 parked at Level-1-C-001
🚛 Truck DEF456 parked at Level-1-L-001

📊 Current Occupancy:
Level 1: 47/50 spots available
Level 2: 50/50 spots available
Level 3: 40/40 spots available

💰 Processing exit for Car XYZ789...
Duration: 2 hours 15 minutes
Fee: $12.50 (Hourly rate: $5.00/hour)
Payment successful via Credit Card
```

## 🔮 Future Enhancements

### Advanced Features
1. **AI-Powered Optimization**: Machine learning for demand prediction
2. **Dynamic Routing**: Guide drivers to available spots efficiently
3. **Contactless Experience**: Fully automated entry/exit with mobile apps
4. **Environmental Monitoring**: Air quality, lighting optimization
5. **Integration Hub**: Connect with smart city infrastructure

### Business Features
6. **Subscription Plans**: Monthly/yearly parking passes
7. **Corporate Accounts**: Business customer management
8. **Event Management**: Special pricing for events and peak times
9. **Loyalty Program**: Rewards for frequent parkers
10. **Revenue Optimization**: Dynamic pricing based on demand

## 📚 Related Design Patterns

- **Decorator Pattern**: For adding features to parking spots (EV charging, valet)
- **Template Method**: For common payment processing workflows
- **Chain of Responsibility**: For handling different types of parking violations
- **Memento Pattern**: For saving/restoring parking lot state
- **Proxy Pattern**: For remote parking lot management

## 🔍 Testing Strategy

### Unit Tests
- Individual component testing (Vehicle, ParkingSpot, PricingStrategy)
- Pricing calculation validation
- Spot allocation algorithm verification

### Integration Tests
- End-to-end parking workflows
- Payment processing scenarios
- Capacity management validation

### Performance Tests
- Concurrent parking/exit operations
- Large-scale capacity testing
- Response time benchmarks

### Load Tests
- Peak hour simulation
- System behavior under stress
- Failover and recovery testing
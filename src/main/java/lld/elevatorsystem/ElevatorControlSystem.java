package lld.elevatorsystem;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Main controller for the elevator system
 * Implements Facade pattern to provide simplified interface
 * Uses Strategy pattern for scheduling algorithms
 * Thread-safe implementation for concurrent operations
 */
public class ElevatorControlSystem implements ElevatorObserver {
    private static final Logger logger = Logger.getLogger(ElevatorControlSystem.class.getName());
    
    private final List<Elevator> elevators;
    private final Queue<Request> pendingRequests;
    private final ElevatorSchedulingStrategy schedulingStrategy;
    private final ScheduledExecutorService executorService;
    private final int numFloors;
    private final Map<Integer, Set<Request>> floorRequests;
    
    // Statistics tracking
    private final Map<String, Integer> statistics;
    
    public ElevatorControlSystem(int numElevators, int numFloors, int elevatorCapacity) {
        this.numFloors = numFloors;
        this.elevators = new ArrayList<>();
        this.pendingRequests = new ConcurrentLinkedQueue<>();
        this.schedulingStrategy = new SCANSchedulingStrategy(); // Default strategy
        this.executorService = Executors.newScheduledThreadPool(numElevators + 2);
        this.floorRequests = new ConcurrentHashMap<>();
        this.statistics = new ConcurrentHashMap<>();
        
        // Initialize elevators
        for (int i = 1; i <= numElevators; i++) {
            Elevator elevator = new Elevator(i, elevatorCapacity, 1); // Start at ground floor
            elevator.addObserver(this);
            elevators.add(elevator);
        }
        
        // Initialize statistics
        statistics.put("totalRequests", 0);
        statistics.put("processedRequests", 0);
        statistics.put("averageWaitTime", 0);
        
        // Start processing requests
        startRequestProcessor();
        startElevatorMovement();
        
        logger.info(String.format("Elevator Control System initialized with %d elevators, %d floors", 
                                numElevators, numFloors));
    }
    
    /**
     * Submit external request (hall call)
     * @param floor Floor number where request is made
     * @param direction Desired direction
     * @return true if request was accepted
     */
    public boolean requestElevator(int floor, Direction direction) {
        if (!isValidFloor(floor)) {
            logger.warning("Invalid floor number: " + floor);
            return false;
        }
        
        if (direction == Direction.IDLE) {
            logger.warning("Invalid direction for external request: IDLE");
            return false;
        }
        
        Request request = Request.createExternalRequest(floor, direction);
        return submitRequest(request);
    }
    
    /**
     * Submit internal request (car call)
     * @param sourceFloor Current floor
     * @param destinationFloor Desired floor
     * @return true if request was accepted
     */
    public boolean requestFloor(int sourceFloor, int destinationFloor) {
        if (!isValidFloor(sourceFloor) || !isValidFloor(destinationFloor)) {
            logger.warning(String.format("Invalid floor numbers: source=%d, dest=%d", 
                                       sourceFloor, destinationFloor));
            return false;
        }
        
        if (sourceFloor == destinationFloor) {
            logger.warning("Source and destination floors are the same");
            return false;
        }
        
        Request request = Request.createInternalRequest(sourceFloor, destinationFloor);
        return submitRequest(request);
    }
    
    /**
     * Submit request to the system
     */
    private boolean submitRequest(Request request) {
        pendingRequests.offer(request);
        
        // Group requests by floor for efficient processing
        floorRequests.computeIfAbsent(request.getSourceFloor(), k -> ConcurrentHashMap.newKeySet())
                    .add(request);
        
        statistics.put("totalRequests", statistics.get("totalRequests") + 1);
        
        logger.info("Request submitted: " + request);
        return true;
    }
    
    /**
     * Start background request processor
     */
    private void startRequestProcessor() {
        executorService.scheduleWithFixedDelay(() -> {
            try {
                processRequests();
            } catch (Exception e) {
                logger.severe("Error processing requests: " + e.getMessage());
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Start elevator movement simulation
     */
    private void startElevatorMovement() {
        for (Elevator elevator : elevators) {
            executorService.scheduleWithFixedDelay(() -> {
                try {
                    elevator.move();
                } catch (Exception e) {
                    logger.severe("Error moving elevator " + elevator.getElevatorId() + ": " + e.getMessage());
                }
            }, 0, 2, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Process pending requests using scheduling strategy
     */
    private void processRequests() {
        while (!pendingRequests.isEmpty()) {
            Request request = pendingRequests.poll();
            if (request == null) break;
            
            Elevator selectedElevator = schedulingStrategy.selectElevator(elevators, request);
            if (selectedElevator != null) {
                boolean assigned = selectedElevator.addRequest(request);
                if (assigned) {
                    statistics.put("processedRequests", statistics.get("processedRequests") + 1);
                    logger.info(String.format("Request assigned to elevator %d: %s", 
                                            selectedElevator.getElevatorId(), request));
                } else {
                    // Put request back in queue if elevator couldn't accept it
                    pendingRequests.offer(request);
                }
            } else {
                // No available elevator, put request back in queue
                pendingRequests.offer(request);
                logger.warning("No available elevator for request: " + request);
                break; // Avoid infinite loop
            }
        }
    }
    
    /**
     * Get system status
     */
    public SystemStatus getSystemStatus() {
        List<ElevatorStatus> elevatorStatuses = new ArrayList<>();
        for (Elevator elevator : elevators) {
            elevatorStatuses.add(new ElevatorStatus(
                elevator.getElevatorId(),
                elevator.getCurrentFloor(),
                elevator.getCurrentDirection(),
                elevator.getState(),
                elevator.getLoadCount(),
                elevator.getCapacity()
            ));
        }
        
        return new SystemStatus(
            elevatorStatuses,
            pendingRequests.size(),
            new HashMap<>(statistics)
        );
    }
    
    /**
     * Set elevator to maintenance mode
     */
    public void setElevatorMaintenance(int elevatorId, boolean maintenance) {
        Elevator elevator = findElevatorById(elevatorId);
        if (elevator != null) {
            if (maintenance) {
                elevator.setMaintenanceMode();
            } else {
                elevator.resumeNormalOperation();
            }
            logger.info(String.format("Elevator %d maintenance mode: %s", elevatorId, maintenance));
        }
    }
    
    /**
     * Emergency stop all elevators
     */
    public void emergencyStop() {
        for (Elevator elevator : elevators) {
            elevator.setEmergencyMode();
        }
        logger.warning("Emergency stop activated for all elevators");
    }
    
    /**
     * Resume normal operation after emergency
     */
    public void resumeNormalOperation() {
        for (Elevator elevator : elevators) {
            elevator.resumeNormalOperation();
        }
        logger.info("Normal operation resumed for all elevators");
    }
    
    /**
     * Find elevator by ID
     */
    private Elevator findElevatorById(int elevatorId) {
        return elevators.stream()
                       .filter(e -> e.getElevatorId() == elevatorId)
                       .findFirst()
                       .orElse(null);
    }
    
    /**
     * Validate floor number
     */
    private boolean isValidFloor(int floor) {
        return floor >= 1 && floor <= numFloors;
    }
    
    /**
     * Shutdown the elevator system
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Elevator Control System shutdown completed");
    }
    
    @Override
    public void onElevatorEvent(int elevatorId, String message) {
        logger.info(String.format("Elevator Event [%d]: %s", elevatorId, message));
    }
    
    // Inner classes for status reporting
    public static class ElevatorStatus {
        public final int elevatorId;
        public final int currentFloor;
        public final Direction direction;
        public final ElevatorState state;
        public final int currentLoad;
        public final int capacity;
        
        public ElevatorStatus(int elevatorId, int currentFloor, Direction direction, 
                            ElevatorState state, int currentLoad, int capacity) {
            this.elevatorId = elevatorId;
            this.currentFloor = currentFloor;
            this.direction = direction;
            this.state = state;
            this.currentLoad = currentLoad;
            this.capacity = capacity;
        }
        
        @Override
        public String toString() {
            return String.format("Elevator{id=%d, floor=%d, dir=%s, state=%s, load=%d/%d}", 
                               elevatorId, currentFloor, direction, state, currentLoad, capacity);
        }
    }
    
    public static class SystemStatus {
        public final List<ElevatorStatus> elevators;
        public final int pendingRequests;
        public final Map<String, Integer> statistics;
        
        public SystemStatus(List<ElevatorStatus> elevators, int pendingRequests, 
                          Map<String, Integer> statistics) {
            this.elevators = elevators;
            this.pendingRequests = pendingRequests;
            this.statistics = statistics;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("System Status:\n");
            sb.append(String.format("Pending Requests: %d\n", pendingRequests));
            sb.append("Statistics: ").append(statistics).append("\n");
            sb.append("Elevators:\n");
            for (ElevatorStatus status : elevators) {
                sb.append("  ").append(status).append("\n");
            }
            return sb.toString();
        }
    }
}
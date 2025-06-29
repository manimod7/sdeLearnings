package lld.elevatorsystem;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Demo class showcasing the Elevator System functionality
 * Provides interactive interface for testing the system
 */
public class ElevatorSystemDemo {
    private static final int NUM_ELEVATORS = 3;
    private static final int NUM_FLOORS = 10;
    private static final int ELEVATOR_CAPACITY = 8;
    
    public static void main(String[] args) {
        System.out.println("🏢 Elevator Control System Demo");
        System.out.println("===============================");
        
        // Initialize the elevator system
        ElevatorControlSystem controlSystem = new ElevatorControlSystem(
            NUM_ELEVATORS, NUM_FLOORS, ELEVATOR_CAPACITY
        );
        
        System.out.printf("Initialized system with %d elevators, %d floors, capacity %d per elevator\n\n", 
                         NUM_ELEVATORS, NUM_FLOORS, ELEVATOR_CAPACITY);
        
        // Demo scenarios
        runDemoScenarios(controlSystem);
        
        // Interactive mode
        runInteractiveMode(controlSystem);
        
        // Shutdown
        controlSystem.shutdown();
        System.out.println("Demo completed. System shutdown.");
    }
    
    /**
     * Run predefined demo scenarios
     */
    private static void runDemoScenarios(ElevatorControlSystem controlSystem) {
        System.out.println("🚀 Running Demo Scenarios...\n");
        
        // Scenario 1: Basic requests
        System.out.println("Scenario 1: Basic elevator requests");
        controlSystem.requestElevator(5, Direction.UP);
        controlSystem.requestElevator(3, Direction.DOWN);
        controlSystem.requestFloor(1, 8);
        controlSystem.requestFloor(7, 2);
        
        // Let elevators process for a while
        sleep(5000);
        printSystemStatus(controlSystem);
        
        // Scenario 2: Heavy load simulation
        System.out.println("\nScenario 2: Heavy load simulation");
        for (int i = 1; i <= 10; i++) {
            Direction dir = (i % 2 == 0) ? Direction.UP : Direction.DOWN;
            controlSystem.requestElevator(i, dir);
        }
        
        sleep(10000);
        printSystemStatus(controlSystem);
        
        // Scenario 3: Maintenance mode
        System.out.println("\nScenario 3: Maintenance mode demonstration");
        controlSystem.setElevatorMaintenance(1, true);
        controlSystem.requestElevator(4, Direction.UP);
        controlSystem.requestElevator(6, Direction.DOWN);
        
        sleep(5000);
        controlSystem.setElevatorMaintenance(1, false);
        printSystemStatus(controlSystem);
        
        // Scenario 4: Emergency situation
        System.out.println("\nScenario 4: Emergency stop demonstration");
        controlSystem.requestElevator(8, Direction.DOWN);
        controlSystem.emergencyStop();
        
        sleep(2000);
        controlSystem.resumeNormalOperation();
        printSystemStatus(controlSystem);
        
        sleep(5000);
    }
    
    /**
     * Interactive mode for manual testing
     */
    private static void runInteractiveMode(ElevatorControlSystem controlSystem) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n🎮 Interactive Mode");
        System.out.println("Commands:");
        System.out.println("  call <floor> <up/down> - Request elevator");
        System.out.println("  go <from> <to> - Request floor (internal)");
        System.out.println("  status - Show system status");
        System.out.println("  maint <id> <on/off> - Set maintenance mode");
        System.out.println("  emergency - Emergency stop");
        System.out.println("  resume - Resume operation");
        System.out.println("  quit - Exit");
        System.out.println();
        
        while (true) {
            System.out.print("elevator> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("quit") || input.equals("exit")) {
                break;
            }
            
            try {
                processCommand(controlSystem, input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    /**
     * Process interactive commands
     */
    private static void processCommand(ElevatorControlSystem controlSystem, String input) {
        String[] parts = input.split("\\s+");
        
        switch (parts[0]) {
            case "call":
                if (parts.length != 3) {
                    System.out.println("Usage: call <floor> <up/down>");
                    return;
                }
                int floor = Integer.parseInt(parts[1]);
                Direction direction = parts[2].equals("up") ? Direction.UP : Direction.DOWN;
                boolean success = controlSystem.requestElevator(floor, direction);
                System.out.println(success ? "Request submitted" : "Request failed");
                break;
                
            case "go":
                if (parts.length != 3) {
                    System.out.println("Usage: go <from> <to>");
                    return;
                }
                int from = Integer.parseInt(parts[1]);
                int to = Integer.parseInt(parts[2]);
                boolean goSuccess = controlSystem.requestFloor(from, to);
                System.out.println(goSuccess ? "Request submitted" : "Request failed");
                break;
                
            case "status":
                printSystemStatus(controlSystem);
                break;
                
            case "maint":
                if (parts.length != 3) {
                    System.out.println("Usage: maint <elevator_id> <on/off>");
                    return;
                }
                int elevatorId = Integer.parseInt(parts[1]);
                boolean maintenance = parts[2].equals("on");
                controlSystem.setElevatorMaintenance(elevatorId, maintenance);
                System.out.println("Maintenance mode " + (maintenance ? "enabled" : "disabled") + 
                                 " for elevator " + elevatorId);
                break;
                
            case "emergency":
                controlSystem.emergencyStop();
                System.out.println("Emergency stop activated");
                break;
                
            case "resume":
                controlSystem.resumeNormalOperation();
                System.out.println("Normal operation resumed");
                break;
                
            default:
                System.out.println("Unknown command: " + parts[0]);
        }
    }
    
    /**
     * Print current system status
     */
    private static void printSystemStatus(ElevatorControlSystem controlSystem) {
        System.out.println("\n📊 " + controlSystem.getSystemStatus());
    }
    
    /**
     * Sleep utility method
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
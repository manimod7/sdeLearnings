package lld.colorlist;

import java.util.List;
import java.util.Scanner;

/**
 * Demo class showcasing the Color List Management System
 * Provides interactive interface for testing the system functionality
 */
public class ColorListDemo {
    private static final ColorListService service = new ColorListService();
    private static String currentUserId = null;
    
    public static void main(String[] args) {
        System.out.println("🎨 Color List Management System Demo");
        System.out.println("====================================");
        
        // Run predefined demo scenarios
        runDemoScenarios();
        
        // Interactive mode
        runInteractiveMode();
        
        System.out.println("Demo completed!");
    }
    
    /**
     * Run predefined demo scenarios
     */
    private static void runDemoScenarios() {
        System.out.println("\n🚀 Running Demo Scenarios...\n");
        
        // Scenario 1: User registration and basic operations
        System.out.println("Scenario 1: User Registration and Basic Operations");
        System.out.println("-------------------------------------------------");
        
        // Register users
        service.registerUser("user1", "Alice", "alice@example.com");
        service.registerUser("user2", "Bob", "bob@example.com");
        service.registerUser("user3", "Carol", "carol@example.com");
        System.out.println("✅ Registered 3 users");
        
        // Create color lists
        String list1 = service.createColorList("Web Colors", "user1", "Colors for web design");
        String list2 = service.createColorList("Nature Palette", "user2", "Colors inspired by nature");
        System.out.println("✅ Created color lists");
        
        // Add colors to lists
        addDemoColors(list1, "user1");
        addDemoColors(list2, "user2");
        System.out.println("✅ Added colors to lists");
        
        // Scenario 2: Sharing and access control
        System.out.println("\nScenario 2: Sharing and Access Control");
        System.out.println("--------------------------------------");
        
        // Share list with edit permission
        service.shareColorList(list1, "user2", ColorList.Permission.EDIT, "user1");
        System.out.println("✅ Alice shared Web Colors with Bob (EDIT permission)");
        
        // Share list with view permission
        service.shareColorList(list2, "user3", ColorList.Permission.VIEW, "user2");
        System.out.println("✅ Bob shared Nature Palette with Carol (VIEW permission)");
        
        // Test access permissions
        ColorList sharedList = service.getColorList(list1, "user2");
        if (sharedList != null) {
            System.out.println("✅ Bob can access Alice's Web Colors list");
        }
        
        // Scenario 3: Color operations and palette generation
        System.out.println("\nScenario 3: Color Operations and Palette Generation");
        System.out.println("--------------------------------------------------");
        
        // Generate palettes
        ColorList webColors = service.getColorList(list1, "user1");
        if (webColors != null && !webColors.getColors("user1").isEmpty()) {
            Color baseColor = webColors.getColors("user1").get(0);
            List<Color> complementaryPalette = webColors.generatePalette("user1", PaletteType.COMPLEMENTARY);
            System.out.println("✅ Generated complementary palette: " + complementaryPalette.size() + " colors");
            
            List<Color> monochromaticPalette = webColors.generatePalette("user1", PaletteType.MONOCHROMATIC);
            System.out.println("✅ Generated monochromatic palette: " + monochromaticPalette.size() + " colors");
        }
        
        // Scenario 4: Search and recommendations
        System.out.println("\nScenario 4: Search and Recommendations");
        System.out.println("--------------------------------------");
        
        // Search for colors
        List<ColorListService.ColorSearchResult> redColors = 
            service.searchColors("user1", "red", ColorListService.SearchCriteria.NAME);
        System.out.println("✅ Found " + redColors.size() + " colors matching 'red'");
        
        // Get recommendations
        List<Color> recommendations = service.getRecommendations("user1", 5);
        System.out.println("✅ Generated " + recommendations.size() + " color recommendations");
        
        // Get popular colors
        List<Color> popularColors = service.getPopularColors(5);
        System.out.println("✅ Found " + popularColors.size() + " popular colors");
        
        // Scenario 5: Export functionality
        System.out.println("\nScenario 5: Export Functionality");
        System.out.println("--------------------------------");
        
        if (webColors != null) {
            List<Color> colors = webColors.getColors("user1");
            String jsonExport = webColors.exportToFormat(ExportFormat.JSON, "user1");
            System.out.println("✅ Exported to JSON (" + jsonExport.length() + " characters)");
            
            String cssExport = webColors.exportToFormat(ExportFormat.CSS, "user1");
            System.out.println("✅ Exported to CSS (" + cssExport.length() + " characters)");
        }
        
        System.out.println("\n📊 Demo scenarios completed successfully!\n");
    }
    
    /**
     * Add demo colors to a list
     */
    private static void addDemoColors(String listId, String userId) {
        Color[] demoColors = {
            Color.fromHex("Primary Blue", "#007bff"),
            Color.fromHex("Success Green", "#28a745"),
            Color.fromHex("Warning Orange", "#ffc107"),
            Color.fromHex("Danger Red", "#dc3545"),
            Color.fromHex("Dark Gray", "#343a40"),
            Color.fromRGB("Light Blue", 173, 216, 230),
            Color.fromRGB("Coral", 255, 127, 80),
            Color.fromRGB("Gold", 255, 215, 0)
        };
        
        for (Color color : demoColors) {
            service.addColorToList(listId, color, userId);
        }
    }
    
    /**
     * Interactive mode for manual testing
     */
    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("🎮 Interactive Mode");
        System.out.println("Commands:");
        System.out.println("  login <userId> - Login as user");
        System.out.println("  register <userId> <name> <email> - Register new user");
        System.out.println("  create <name> <description> - Create color list");
        System.out.println("  add <listId> <name> <hex> - Add color to list");
        System.out.println("  lists - Show my color lists");
        System.out.println("  view <listId> - View color list");
        System.out.println("  share <listId> <userId> <permission> - Share list");
        System.out.println("  search <query> - Search colors");
        System.out.println("  recommend - Get color recommendations");
        System.out.println("  export <listId> <format> - Export list");
        System.out.println("  quit - Exit");
        System.out.println();
        
        while (true) {
            System.out.print("color> ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("quit") || input.equals("exit")) {
                break;
            }
            
            try {
                processCommand(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    /**
     * Process interactive commands
     */
    private static void processCommand(String input) {
        String[] parts = input.split("\\s+", 4);
        String command = parts[0].toLowerCase();
        
        switch (command) {
            case "login":
                if (parts.length < 2) {
                    System.out.println("Usage: login <userId>");
                    return;
                }
                currentUserId = parts[1];
                System.out.println("Logged in as: " + currentUserId);
                break;
                
            case "register":
                if (parts.length < 4) {
                    System.out.println("Usage: register <userId> <name> <email>");
                    return;
                }
                boolean registered = service.registerUser(parts[1], parts[2], parts[3]);
                System.out.println(registered ? "User registered successfully" : "User already exists");
                break;
                
            case "create":
                if (currentUserId == null) {
                    System.out.println("Please login first");
                    return;
                }
                if (parts.length < 2) {
                    System.out.println("Usage: create <name> [description]");
                    return;
                }
                String description = parts.length > 2 ? parts[2] : "";
                String listId = service.createColorList(parts[1], currentUserId, description);
                System.out.println("Created color list: " + listId);
                break;
                
            case "add":
                if (currentUserId == null) {
                    System.out.println("Please login first");
                    return;
                }
                if (parts.length < 4) {
                    System.out.println("Usage: add <listId> <colorName> <hexCode>");
                    return;
                }
                Color color = Color.fromHex(parts[2], parts[3]);
                boolean added = service.addColorToList(parts[1], color, currentUserId);
                System.out.println(added ? "Color added successfully" : "Failed to add color");
                break;
                
            case "lists":
                if (currentUserId == null) {
                    System.out.println("Please login first");
                    return;
                }
                List<ColorListService.ColorListSummary> userLists = service.getUserColorLists(currentUserId);
                System.out.println("Your color lists:");
                for (ColorListService.ColorListSummary summary : userLists) {
                    System.out.println(String.format("  %s: %s (%d colors, %s)", 
                        summary.listId, summary.name, summary.colorCount, summary.accessLevel));
                }
                break;
                
            case "view":
                if (currentUserId == null) {
                    System.out.println("Please login first");
                    return;
                }
                if (parts.length < 2) {
                    System.out.println("Usage: view <listId>");
                    return;
                }
                ColorList colorList = service.getColorList(parts[1], currentUserId);
                if (colorList != null) {
                    System.out.println("Color List: " + colorList.getName());
                    List<Color> colors = colorList.getColors(currentUserId);
                    for (int i = 0; i < colors.size(); i++) {
                        Color c = colors.get(i);
                        System.out.println(String.format("  %d. %s (%s)", i + 1, c.getName(), c.getHexCode()));
                    }
                } else {
                    System.out.println("Color list not found or access denied");
                }
                break;
                
            case "search":
                if (currentUserId == null) {
                    System.out.println("Please login first");
                    return;
                }
                if (parts.length < 2) {
                    System.out.println("Usage: search <query>");
                    return;
                }
                List<ColorListService.ColorSearchResult> results = 
                    service.searchColors(currentUserId, parts[1], ColorListService.SearchCriteria.ALL);
                System.out.println("Search results:");
                for (ColorListService.ColorSearchResult result : results) {
                    System.out.println(String.format("  %s (%s) in %s", 
                        result.color.getName(), result.color.getHexCode(), result.listName));
                }
                break;
                
            case "recommend":
                if (currentUserId == null) {
                    System.out.println("Please login first");
                    return;
                }
                List<Color> recommendations = service.getRecommendations(currentUserId, 5);
                System.out.println("Color recommendations:");
                for (Color rec : recommendations) {
                    System.out.println(String.format("  %s (%s)", rec.getName(), rec.getHexCode()));
                }
                break;
                
            default:
                System.out.println("Unknown command: " + command);
        }
    }
}
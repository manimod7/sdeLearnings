package lld.tagmanagement;

import java.util.List;

/**
 * Demo class showcasing the Tag Management System
 * Demonstrates comprehensive tag operations for JIRA and Confluence
 */
public class TagManagementDemo {
    private static final TagManagementService service = new TagManagementService();
    
    public static void main(String[] args) {
        System.out.println("🏷️ Tag Management System Demo");
        System.out.println("=============================");
        
        // Run comprehensive demo scenarios
        runDemoScenarios();
        
        System.out.println("\n🎉 Demo completed successfully!");
    }
    
    /**
     * Run comprehensive demo scenarios
     */
    private static void runDemoScenarios() {
        System.out.println("\n🚀 Running Demo Scenarios...\n");
        
        // Scenario 1: User setup and basic tag creation
        System.out.println("Scenario 1: User Setup and Tag Creation");
        System.out.println("---------------------------------------");
        
        // Register users with different roles
        service.registerUser("admin1", "Alice Admin", "alice@company.com", UserRole.ADMIN);
        service.registerUser("dev1", "Bob Developer", "bob@company.com", UserRole.DEVELOPER);
        service.registerUser("creator1", "Carol Creator", "carol@company.com", UserRole.CONTENT_CREATOR);
        service.registerUser("viewer1", "David Viewer", "david@company.com", UserRole.VIEWER);
        System.out.println("✅ Registered 4 users with different roles");
        
        // Create tags
        String techTag = service.createTag("technology", "Technology-related content", "#FF5722", "admin1");
        String bugTag = service.createTag("bug", "Bug reports and issues", "#F44336", "admin1");
        String featureTag = service.createTag("feature", "New feature requests", "#4CAF50", "admin1");
        String docsTag = service.createTag("documentation", "Documentation and guides", "#2196F3", "admin1");
        String frontendTag = service.createTag("frontend", "Frontend development", "#9C27B0", "admin1");
        String backendTag = service.createTag("backend", "Backend development", "#607D8B", "admin1");
        System.out.println("✅ Created 6 main tags");
        
        // Scenario 2: Hierarchical tag structure
        System.out.println("\nScenario 2: Hierarchical Tag Structure");
        System.out.println("--------------------------------------");
        
        // Create hierarchy: technology -> frontend, backend
        service.createHierarchy(techTag, frontendTag, "admin1");
        service.createHierarchy(techTag, backendTag, "admin1");
        System.out.println("✅ Created hierarchical structure: technology > frontend, backend");
        
        // Add synonyms
        Tag frontendTagObj = service.searchTags("frontend", null, null, TagManagementService.SortOption.ALPHABETICAL, 1).get(0);
        frontendTagObj.addSynonym("UI");
        frontendTagObj.addSynonym("user interface");
        frontendTagObj.addSynonym("client-side");
        
        Tag backendTagObj = service.searchTags("backend", null, null, TagManagementService.SortOption.ALPHABETICAL, 1).get(0);
        backendTagObj.addSynonym("server-side");
        backendTagObj.addSynonym("API");
        backendTagObj.addSynonym("database");
        System.out.println("✅ Added synonyms to frontend and backend tags");
        
        // Scenario 3: Tag associations with JIRA and Confluence
        System.out.println("\nScenario 3: Tag Associations");
        System.out.println("----------------------------");
        
        // Associate tags with JIRA issues
        service.associateTag(bugTag, "PROJ-123", TagAssociation.EntityType.JIRA_ISSUE, "PROJECT-1", "dev1");
        service.associateTag(frontendTag, "PROJ-123", TagAssociation.EntityType.JIRA_ISSUE, "PROJECT-1", "dev1");
        service.associateTag(featureTag, "PROJ-124", TagAssociation.EntityType.JIRA_ISSUE, "PROJECT-1", "dev1");
        service.associateTag(backendTag, "PROJ-124", TagAssociation.EntityType.JIRA_ISSUE, "PROJECT-1", "dev1");
        System.out.println("✅ Associated tags with JIRA issues");
        
        // Associate tags with Confluence pages
        service.associateTag(docsTag, "PAGE-001", TagAssociation.EntityType.CONFLUENCE_PAGE, "PROJECT-1", "creator1");
        service.associateTag(frontendTag, "PAGE-001", TagAssociation.EntityType.CONFLUENCE_PAGE, "PROJECT-1", "creator1");
        service.associateTag(docsTag, "PAGE-002", TagAssociation.EntityType.CONFLUENCE_PAGE, "PROJECT-1", "creator1");
        service.associateTag(backendTag, "PAGE-002", TagAssociation.EntityType.CONFLUENCE_PAGE, "PROJECT-1", "creator1");
        System.out.println("✅ Associated tags with Confluence pages");
        
        // Scenario 4: Search and discovery
        System.out.println("\nScenario 4: Search and Discovery");
        System.out.println("--------------------------------");
        
        // Search tags
        List<Tag> techTags = service.searchTags("tech", TagStatus.ACTIVE, null, TagManagementService.SortOption.USAGE_COUNT, 10);
        System.out.println("✅ Found " + techTags.size() + " tags matching 'tech'");
        
        List<Tag> popularTags = service.searchTags("", TagStatus.ACTIVE, "PROJECT-1", TagManagementService.SortOption.USAGE_COUNT, 5);
        System.out.println("✅ Found " + popularTags.size() + " popular tags in PROJECT-1");
        
        // Get tags for specific entities
        List<Tag> jiraIssueTags = service.getTagsForEntity("PROJ-123");
        System.out.println("✅ JIRA issue PROJ-123 has " + jiraIssueTags.size() + " tags");
        
        List<Tag> confluencePageTags = service.getTagsForEntity("PAGE-001");
        System.out.println("✅ Confluence page PAGE-001 has " + confluencePageTags.size() + " tags");
        
        // Scenario 5: Tag suggestions and analytics
        System.out.println("\nScenario 5: Tag Suggestions and Analytics");
        System.out.println("-----------------------------------------");
        
        // Get tag suggestions
        List<TagManagementService.TagSuggestion> suggestions = service.getSuggestions(\n            \"PROJ-125\", \n            \"This is a frontend bug in the user interface component\", \n            5\n        );\n        System.out.println(\"✅ Generated \" + suggestions.size() + \" tag suggestions\");\n        \n        for (TagManagementService.TagSuggestion suggestion : suggestions) {\n            System.out.println(\"   - \" + suggestion.getTag().getName() + \n                             \" (score: \" + String.format(\"%.2f\", suggestion.getScore()) + \n                             \", reason: \" + suggestion.getReason() + \")\");\n        }\n        \n        // Get tag analytics\n        TagManagementService.TagAnalytics analytics = service.getTagAnalytics(frontendTag);\n        if (analytics != null) {\n            System.out.println(\"✅ Frontend tag analytics:\");\n            System.out.println(\"   - Total usage: \" + analytics.getTotalUsage());\n            System.out.println(\"   - Usage by type: \" + analytics.getUsageByType());\n            System.out.println(\"   - Usage by project: \" + analytics.getUsageByProject());\n        }\n        \n        // Scenario 6: Bulk operations\n        System.out.println(\"\\nScenario 6: Bulk Operations\");\n        System.out.println(\"---------------------------\");\n        \n        // Bulk associate tags\n        List<String> tagIds = List.of(docsTag, techTag);\n        List<String> entityIds = List.of(\"PAGE-003\", \"PAGE-004\", \"PAGE-005\");\n        \n        TagManagementService.BulkOperationResult bulkResult = service.bulkAssociateTags(\n            tagIds, entityIds, TagAssociation.EntityType.CONFLUENCE_PAGE, \"PROJECT-1\", \"creator1\"\n        );\n        \n        System.out.println(\"✅ Bulk operation completed:\");\n        System.out.println(\"   - Successful: \" + bulkResult.getSuccessCount());\n        System.out.println(\"   - Failed: \" + bulkResult.getFailureCount());\n        \n        // Scenario 7: Tag hierarchy and relationships\n        System.out.println(\"\\nScenario 7: Tag Hierarchy\");\n        System.out.println(\"--------------------------\");\n        \n        // Get tag hierarchy\n        TagManagementService.TagHierarchy hierarchy = service.getTagHierarchy(techTag);\n        if (hierarchy != null) {\n            System.out.println(\"✅ Technology tag hierarchy:\");\n            printHierarchy(hierarchy, 0);\n        }\n        \n        // Scenario 8: Advanced search and filtering\n        System.out.println(\"\\nScenario 8: Advanced Search and Filtering\");\n        System.out.println(\"-----------------------------------------\");\n        \n        // Search by different criteria\n        List<Tag> alphabeticalTags = service.searchTags(\"\", TagStatus.ACTIVE, null, \n            TagManagementService.SortOption.ALPHABETICAL, 10);\n        System.out.println(\"✅ Tags in alphabetical order: \" + \n            alphabeticalTags.stream().map(Tag::getName).toList());\n        \n        List<Tag> usageSortedTags = service.searchTags(\"\", TagStatus.ACTIVE, null, \n            TagManagementService.SortOption.USAGE_COUNT, 10);\n        System.out.println(\"✅ Tags by usage count: \" + \n            usageSortedTags.stream().map(tag -> tag.getName() + \"(\" + tag.getUsageCount() + \")\").toList());\n        \n        // Get entities for specific tags\n        List<TagAssociation> frontendAssociations = service.getEntitiesForTag(frontendTag, null);\n        System.out.println(\"✅ Frontend tag is associated with \" + frontendAssociations.size() + \" entities\");\n        \n        List<TagAssociation> jiraAssociations = service.getEntitiesForTag(frontendTag, TagAssociation.EntityType.JIRA_ISSUE);\n        System.out.println(\"✅ Frontend tag is associated with \" + jiraAssociations.size() + \" JIRA issues\");\n        \n        // Scenario 9: Tag lifecycle management\n        System.out.println(\"\\nScenario 9: Tag Lifecycle Management\");\n        System.out.println(\"------------------------------------\");\n        \n        // Create a temporary tag for deactivation demo\n        String tempTag = service.createTag(\"temporary\", \"Temporary tag for demo\", \"#9E9E9E\", \"admin1\");\n        System.out.println(\"✅ Created temporary tag\");\n        \n        // Deactivate tag\n        service.deactivateTag(tempTag, \"admin1\");\n        System.out.println(\"✅ Deactivated temporary tag\");\n        \n        // Verify deactivated tag doesn't appear in active searches\n        List<Tag> activeTags = service.searchTags(\"temporary\", TagStatus.ACTIVE, null, \n            TagManagementService.SortOption.ALPHABETICAL, 10);\n        List<Tag> inactiveTags = service.searchTags(\"temporary\", TagStatus.INACTIVE, null, \n            TagManagementService.SortOption.ALPHABETICAL, 10);\n        \n        System.out.println(\"✅ Active 'temporary' tags: \" + activeTags.size());\n        System.out.println(\"✅ Inactive 'temporary' tags: \" + inactiveTags.size());\n        \n        // Demonstrate tag usage statistics\n        demonstrateTagStatistics();\n    }\n    \n    /**\n     * Print tag hierarchy recursively\n     */\n    private static void printHierarchy(TagManagementService.TagHierarchy hierarchy, int depth) {\n        String indent = \"  \".repeat(depth);\n        Tag tag = hierarchy.getTag();\n        System.out.println(indent + \"- \" + tag.getName() + \" (usage: \" + tag.getUsageCount() + \")\");\n        \n        for (TagManagementService.TagHierarchy child : hierarchy.getChildren()) {\n            printHierarchy(child, depth + 1);\n        }\n    }\n    \n    /**\n     * Demonstrate detailed tag statistics\n     */\n    private static void demonstrateTagStatistics() {\n        System.out.println(\"\\n📊 Detailed Tag Statistics\");\n        System.out.println(\"==========================\");\n        \n        // Get all tags and show statistics\n        List<Tag> allTags = service.searchTags(\"\", TagStatus.ACTIVE, null, \n            TagManagementService.SortOption.USAGE_COUNT, 20);\n        \n        System.out.println(\"📈 Tag Usage Summary:\");\n        for (Tag tag : allTags) {\n            Tag.TagUsageStats stats = tag.getUsageStats();\n            System.out.println(String.format(\"   %s: %d uses, %d projects, %d children\", \n                tag.getName(), \n                stats.getUsageCount(), \n                stats.getProjectCount(), \n                stats.getChildCount()\n            ));\n        }\n        \n        // Show tag associations summary\n        System.out.println(\"\\n🔗 Association Summary:\");\n        int totalJiraAssociations = 0;\n        int totalConfluenceAssociations = 0;\n        \n        for (Tag tag : allTags) {\n            List<TagAssociation> jiraAssocs = service.getEntitiesForTag(tag.getTagId(), TagAssociation.EntityType.JIRA_ISSUE);\n            List<TagAssociation> confluenceAssocs = service.getEntitiesForTag(tag.getTagId(), TagAssociation.EntityType.CONFLUENCE_PAGE);\n            \n            totalJiraAssociations += jiraAssocs.size();\n            totalConfluenceAssociations += confluenceAssocs.size();\n        }\n        \n        System.out.println(\"   Total JIRA associations: \" + totalJiraAssociations);\n        System.out.println(\"   Total Confluence associations: \" + totalConfluenceAssociations);\n        System.out.println(\"   Total active tags: \" + allTags.size());\n    }\n}"
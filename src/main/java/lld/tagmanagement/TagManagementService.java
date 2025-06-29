package lld.tagmanagement;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Main service class for tag management system
 * Provides comprehensive API for tag operations across JIRA and Confluence
 */
public class TagManagementService {
    private final Map<String, Tag> tags;
    private final Map<String, TagAssociation> associations;
    private final Map<String, User> users;
    private final Map<String, Set<String>> entityTags; // entityId -> tagIds
    private final Map<String, Set<String>> tagEntities; // tagId -> entityIds
    private final NotificationService notificationService;
    
    public TagManagementService() {
        this.tags = new ConcurrentHashMap<>();
        this.associations = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.entityTags = new ConcurrentHashMap<>();
        this.tagEntities = new ConcurrentHashMap<>();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Register a user in the system
     */
    public boolean registerUser(String userId, String name, String email, UserRole role) {
        if (users.containsKey(userId)) {
            return false;
        }
        
        User user = new User(userId, name, email, role);
        users.put(userId, user);
        return true;
    }
    
    /**
     * Create a new tag
     */
    public String createTag(String name, String description, String color, String createdBy) {
        User creator = users.get(createdBy);
        if (creator == null || creator.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admins can create tags");
        }
        
        // Check for duplicate tag names
        if (tagExists(name)) {
            throw new IllegalArgumentException("Tag with name '" + name + "' already exists");
        }
        
        String tagId = generateTagId();
        Tag tag = new Tag(tagId, name, description, color, createdBy);
        tags.put(tagId, tag);
        
        notificationService.sendNotification(createdBy, "Tag created: " + name);
        return tagId;
    }
    
    /**
     * Create hierarchical tag relationship
     */
    public boolean createHierarchy(String parentTagId, String childTagId, String userId) {
        User user = users.get(userId);
        if (user == null || user.getRole() != UserRole.ADMIN) {
            return false;
        }
        
        Tag parentTag = tags.get(parentTagId);
        Tag childTag = tags.get(childTagId);
        
        if (parentTag == null || childTag == null) {
            return false;
        }
        
        // Prevent circular dependencies
        if (wouldCreateCircularDependency(parentTagId, childTagId)) {
            return false;
        }
        
        parentTag.addChild(childTagId);
        childTag.setParent(parentTagId);
        
        // Update hierarchy depth
        updateHierarchyDepths();
        
        return true;
    }
    
    /**
     * Associate tag with entity (JIRA issue or Confluence page)
     */
    public boolean associateTag(String tagId, String entityId, TagAssociation.EntityType entityType, 
                              String projectId, String userId) {
        User user = users.get(userId);
        Tag tag = tags.get(tagId);
        
        if (user == null || tag == null || tag.getStatus() != TagStatus.ACTIVE) {
            return false;
        }
        
        // Check user permissions
        if (!hasPermissionToTag(user, entityType)) {
            return false;
        }
        
        String associationId = generateAssociationId();
        TagAssociation association = new TagAssociation(
            associationId, tagId, entityId, entityType, userId, projectId
        );
        
        associations.put(associationId, association);
        
        // Update indexes
        entityTags.computeIfAbsent(entityId, k -> ConcurrentHashMap.newKeySet()).add(tagId);
        tagEntities.computeIfAbsent(tagId, k -> ConcurrentHashMap.newKeySet()).add(entityId);
        
        // Update tag usage
        tag.incrementUsage();
        tag.associateWithProject(projectId);
        
        return true;
    }
    
    /**
     * Remove tag association
     */
    public boolean removeAssociation(String tagId, String entityId, TagAssociation.EntityType entityType, String userId) {
        User user = users.get(userId);
        if (user == null || !hasPermissionToTag(user, entityType)) {
            return false;
        }
        
        // Find and remove association
        TagAssociation toRemove = associations.values().stream()
            .filter(assoc -> assoc.getTagId().equals(tagId) && 
                           assoc.getEntityId().equals(entityId) && 
                           assoc.getEntityType() == entityType)
            .findFirst()
            .orElse(null);
        
        if (toRemove == null) {
            return false;
        }
        
        associations.remove(toRemove.getAssociationId());
        
        // Update indexes
        Set<String> entityTagSet = entityTags.get(entityId);
        if (entityTagSet != null) {
            entityTagSet.remove(tagId);
            if (entityTagSet.isEmpty()) {
                entityTags.remove(entityId);
            }
        }
        
        Set<String> tagEntitySet = tagEntities.get(tagId);
        if (tagEntitySet != null) {
            tagEntitySet.remove(entityId);
            if (tagEntitySet.isEmpty()) {
                tagEntities.remove(tagId);
            }
        }
        
        // Update tag usage
        Tag tag = tags.get(tagId);
        if (tag != null) {
            tag.decrementUsage();
        }
        
        return true;
    }
    
    /**
     * Search tags with filters and sorting
     */
    public List<Tag> searchTags(String query, TagStatus status, String projectId, 
                               SortOption sortBy, int limit) {
        return tags.values().stream()
                   .filter(tag -> matchesSearchCriteria(tag, query, status, projectId))
                   .sorted(getComparator(sortBy))
                   .limit(limit)
                   .collect(Collectors.toList());
    }
    
    /**
     * Get tags for specific entity
     */
    public List<Tag> getTagsForEntity(String entityId) {
        Set<String> tagIds = entityTags.getOrDefault(entityId, Collections.emptySet());
        return tagIds.stream()
                    .map(tags::get)
                    .filter(Objects::nonNull)
                    .filter(tag -> tag.getStatus() == TagStatus.ACTIVE)
                    .collect(Collectors.toList());
    }
    
    /**
     * Get entities for specific tag
     */
    public List<TagAssociation> getEntitiesForTag(String tagId, TagAssociation.EntityType entityType) {
        return associations.values().stream()
                          .filter(assoc -> assoc.getTagId().equals(tagId))
                          .filter(assoc -> entityType == null || assoc.getEntityType() == entityType)
                          .collect(Collectors.toList());
    }
    
    /**
     * Get tag suggestions based on entity content and existing tags
     */
    public List<TagSuggestion> getSuggestions(String entityId, String content, int limit) {
        List<TagSuggestion> suggestions = new ArrayList<>();
        
        // Get popular tags in same project
        String projectId = getProjectForEntity(entityId);
        if (projectId != null) {
            List<Tag> popularTags = getPopularTagsInProject(projectId, limit * 2);
            for (Tag tag : popularTags) {
                double score = calculateSuggestionScore(tag, content, entityId);
                if (score > 0.1) { // Threshold for relevance
                    suggestions.add(new TagSuggestion(tag, score, "Popular in project"));
                }
            }
        }
        
        // Content-based suggestions
        for (Tag tag : tags.values()) {
            if (tag.getStatus() == TagStatus.ACTIVE && contentMatchesTag(content, tag)) {
                double score = calculateContentMatchScore(content, tag);
                suggestions.add(new TagSuggestion(tag, score, "Content match"));
            }
        }
        
        // Sort by score and limit
        return suggestions.stream()
                         .sorted((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()))
                         .limit(limit)
                         .collect(Collectors.toList());
    }
    
    /**
     * Get tag analytics
     */
    public TagAnalytics getTagAnalytics(String tagId) {
        Tag tag = tags.get(tagId);
        if (tag == null) {
            return null;
        }
        
        List<TagAssociation> tagAssociations = getEntitiesForTag(tagId, null);
        
        Map<TagAssociation.EntityType, Long> usageByType = tagAssociations.stream()
            .collect(Collectors.groupingBy(
                TagAssociation::getEntityType,
                Collectors.counting()
            ));
        
        Map<String, Long> usageByProject = tagAssociations.stream()
            .filter(assoc -> assoc.getProjectId() != null)
            .collect(Collectors.groupingBy(
                TagAssociation::getProjectId,
                Collectors.counting()
            ));
        
        return new TagAnalytics(tag, usageByType, usageByProject, tagAssociations.size());
    }
    
    /**
     * Deactivate tag
     */
    public boolean deactivateTag(String tagId, String userId) {
        User user = users.get(userId);
        if (user == null || user.getRole() != UserRole.ADMIN) {
            return false;
        }
        
        Tag tag = tags.get(tagId);
        if (tag == null) {
            return false;
        }
        
        tag.setStatus(TagStatus.INACTIVE);
        notificationService.sendNotification(userId, "Tag deactivated: " + tag.getName());
        return true;
    }
    
    /**
     * Get tag hierarchy tree
     */
    public TagHierarchy getTagHierarchy(String rootTagId) {
        Tag rootTag = tags.get(rootTagId);
        if (rootTag == null) {
            return null;
        }
        
        return buildHierarchyTree(rootTag);
    }
    
    /**
     * Bulk tag operations
     */
    public BulkOperationResult bulkAssociateTags(List<String> tagIds, List<String> entityIds, 
                                               TagAssociation.EntityType entityType, String projectId, String userId) {
        List<String> successful = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        
        for (String entityId : entityIds) {
            for (String tagId : tagIds) {
                boolean success = associateTag(tagId, entityId, entityType, projectId, userId);
                if (success) {
                    successful.add(entityId + ":" + tagId);
                } else {
                    failed.add(entityId + ":" + tagId);
                }
            }
        }
        
        return new BulkOperationResult(successful, failed);
    }
    
    // Helper methods
    private boolean tagExists(String name) {
        return tags.values().stream().anyMatch(tag -> tag.getName().equalsIgnoreCase(name));
    }
    
    private boolean wouldCreateCircularDependency(String parentTagId, String childTagId) {
        // Simple cycle detection - in a real implementation, you'd do a proper DFS
        Set<String> visited = new HashSet<>();
        return hasPathToTag(childTagId, parentTagId, visited);
    }
    
    private boolean hasPathToTag(String fromTagId, String toTagId, Set<String> visited) {
        if (fromTagId.equals(toTagId)) {
            return true;
        }
        
        if (visited.contains(fromTagId)) {
            return false;
        }
        
        visited.add(fromTagId);
        Tag fromTag = tags.get(fromTagId);
        
        if (fromTag != null) {
            for (String childId : fromTag.getChildTagIds()) {
                if (hasPathToTag(childId, toTagId, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void updateHierarchyDepths() {
        // Reset depths
        for (Tag tag : tags.values()) {
            tag.setMetadata("depth", 0);
        }
        
        // Calculate depths from root tags
        for (Tag tag : tags.values()) {
            if (tag.isRoot()) {
                updateDepthRecursive(tag, 0);
            }
        }
    }
    
    private void updateDepthRecursive(Tag tag, int depth) {
        tag.setMetadata("depth", depth);
        for (String childId : tag.getChildTagIds()) {
            Tag child = tags.get(childId);
            if (child != null) {
                updateDepthRecursive(child, depth + 1);
            }
        }
    }
    
    private boolean hasPermissionToTag(User user, TagAssociation.EntityType entityType) {
        switch (entityType) {
            case JIRA_ISSUE:
                return user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.DEVELOPER;
            case CONFLUENCE_PAGE:
                return user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.CONTENT_CREATOR;
            case PROJECT:
            case COMPONENT:
                return user.getRole() == UserRole.ADMIN;
            default:
                return false;
        }
    }
    
    private boolean matchesSearchCriteria(Tag tag, String query, TagStatus status, String projectId) {
        if (status != null && tag.getStatus() != status) {
            return false;
        }
        
        if (projectId != null && !tag.getAssociatedProjects().contains(projectId)) {
            return false;
        }
        
        return tag.matches(query);
    }
    
    private Comparator<Tag> getComparator(SortOption sortBy) {
        switch (sortBy) {
            case USAGE_COUNT:
                return (t1, t2) -> Long.compare(t2.getUsageCount(), t1.getUsageCount());
            case ALPHABETICAL:
                return Comparator.comparing(Tag::getName);
            case CREATED_DATE:
                return Comparator.comparing(Tag::getCreatedAt);
            case UPDATED_DATE:
            default:
                return Comparator.comparing(Tag::getUpdatedAt).reversed();
        }
    }
    
    private List<Tag> getPopularTagsInProject(String projectId, int limit) {
        return tags.values().stream()
                   .filter(tag -> tag.getAssociatedProjects().contains(projectId))
                   .sorted((t1, t2) -> Long.compare(t2.getUsageCount(), t1.getUsageCount()))
                   .limit(limit)
                   .collect(Collectors.toList());
    }
    
    private boolean contentMatchesTag(String content, Tag tag) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        String lowerContent = content.toLowerCase();
        return lowerContent.contains(tag.getName().toLowerCase()) ||
               tag.getSynonyms().stream().anyMatch(lowerContent::contains);
    }
    
    private double calculateSuggestionScore(Tag tag, String content, String entityId) {
        double score = 0.0;
        
        // Usage popularity (0-0.5)
        score += Math.min(tag.getUsageCount() / 100.0, 0.5);
        
        // Content relevance (0-0.5)
        if (contentMatchesTag(content, tag)) {
            score += 0.3;
        }
        
        return Math.min(score, 1.0);
    }
    
    private double calculateContentMatchScore(String content, Tag tag) {
        if (content == null || content.trim().isEmpty()) {
            return 0.0;
        }
        
        String lowerContent = content.toLowerCase();
        double score = 0.0;
        
        if (lowerContent.contains(tag.getName().toLowerCase())) {
            score += 0.8;
        }
        
        for (String synonym : tag.getSynonyms()) {
            if (lowerContent.contains(synonym)) {
                score += 0.6;
                break;
            }
        }
        
        return Math.min(score, 1.0);
    }
    
    private String getProjectForEntity(String entityId) {
        // In a real implementation, this would query the entity service
        return "default-project";
    }
    
    private TagHierarchy buildHierarchyTree(Tag rootTag) {
        List<TagHierarchy> children = new ArrayList<>();
        
        for (String childId : rootTag.getChildTagIds()) {
            Tag childTag = tags.get(childId);
            if (childTag != null) {
                children.add(buildHierarchyTree(childTag));
            }
        }
        
        return new TagHierarchy(rootTag, children);
    }
    
    private String generateTagId() {
        return "tag_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    private String generateAssociationId() {
        return "assoc_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    // Enums and inner classes
    public enum SortOption {
        ALPHABETICAL, USAGE_COUNT, CREATED_DATE, UPDATED_DATE
    }
    
    public static class User {
        private final String userId;
        private final String name;
        private final String email;
        private final UserRole role;
        
        public User(String userId, String name, String email, UserRole role) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role;
        }
        
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public UserRole getRole() { return role; }
    }
    
    public static class TagSuggestion {
        private final Tag tag;
        private final double score;
        private final String reason;
        
        public TagSuggestion(Tag tag, double score, String reason) {
            this.tag = tag;
            this.score = score;
            this.reason = reason;
        }
        
        public Tag getTag() { return tag; }
        public double getScore() { return score; }
        public String getReason() { return reason; }
    }
    
    public static class TagAnalytics {
        private final Tag tag;
        private final Map<TagAssociation.EntityType, Long> usageByType;
        private final Map<String, Long> usageByProject;
        private final int totalUsage;
        
        public TagAnalytics(Tag tag, Map<TagAssociation.EntityType, Long> usageByType,
                          Map<String, Long> usageByProject, int totalUsage) {
            this.tag = tag;
            this.usageByType = usageByType;
            this.usageByProject = usageByProject;
            this.totalUsage = totalUsage;
        }
        
        public Tag getTag() { return tag; }
        public Map<TagAssociation.EntityType, Long> getUsageByType() { return usageByType; }
        public Map<String, Long> getUsageByProject() { return usageByProject; }
        public int getTotalUsage() { return totalUsage; }
    }
    
    public static class TagHierarchy {
        private final Tag tag;
        private final List<TagHierarchy> children;
        
        public TagHierarchy(Tag tag, List<TagHierarchy> children) {
            this.tag = tag;
            this.children = children;
        }
        
        public Tag getTag() { return tag; }
        public List<TagHierarchy> getChildren() { return children; }
    }
    
    public static class BulkOperationResult {
        private final List<String> successful;
        private final List<String> failed;
        
        public BulkOperationResult(List<String> successful, List<String> failed) {
            this.successful = successful;
            this.failed = failed;
        }
        
        public List<String> getSuccessful() { return successful; }
        public List<String> getFailed() { return failed; }
        public int getSuccessCount() { return successful.size(); }
        public int getFailureCount() { return failed.size(); }
    }
    
    // Simple notification service
    private static class NotificationService {
        public void sendNotification(String userId, String message) {
            System.out.println("📧 Notification to " + userId + ": " + message);
        }
    }
}
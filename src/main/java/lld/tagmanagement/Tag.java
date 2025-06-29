package lld.tagmanagement;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a tag that can be applied to various entities
 * Supports hierarchical relationships and usage tracking
 */
public class Tag {
    private final String tagId;
    private final String name;
    private final String description;
    private final String color;
    private final String createdBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TagStatus status;
    private String parentTagId;
    private final Set<String> childTagIds;
    private final Map<String, Object> metadata;
    private final AtomicLong usageCount;
    private final Set<String> associatedProjects;
    private final Set<String> synonyms;
    
    public Tag(String tagId, String name, String description, String color, String createdBy) {
        this.tagId = tagId;
        this.name = name;
        this.description = description;
        this.color = color;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = TagStatus.ACTIVE;
        this.childTagIds = ConcurrentHashMap.newKeySet();
        this.metadata = new ConcurrentHashMap<>();
        this.usageCount = new AtomicLong(0);
        this.associatedProjects = ConcurrentHashMap.newKeySet();
        this.synonyms = ConcurrentHashMap.newKeySet();
    }
    
    /**
     * Set parent tag for hierarchical structure
     */
    public synchronized void setParent(String parentTagId) {
        this.parentTagId = parentTagId;
        updateTimestamp();
    }
    
    /**
     * Add child tag
     */
    public synchronized void addChild(String childTagId) {
        childTagIds.add(childTagId);
        updateTimestamp();
    }
    
    /**
     * Remove child tag
     */
    public synchronized boolean removeChild(String childTagId) {
        boolean removed = childTagIds.remove(childTagId);
        if (removed) {
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Update tag status
     */
    public synchronized void setStatus(TagStatus status) {
        this.status = status;
        updateTimestamp();
    }
    
    /**
     * Add synonym for tag
     */
    public void addSynonym(String synonym) {
        synonyms.add(synonym.toLowerCase().trim());
        updateTimestamp();
    }
    
    /**
     * Remove synonym
     */
    public boolean removeSynonym(String synonym) {
        boolean removed = synonyms.remove(synonym.toLowerCase().trim());
        if (removed) {
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Associate tag with project
     */
    public void associateWithProject(String projectId) {
        associatedProjects.add(projectId);
        updateTimestamp();
    }
    
    /**
     * Remove project association
     */
    public boolean removeProjectAssociation(String projectId) {
        return associatedProjects.remove(projectId);
    }
    
    /**
     * Increment usage count
     */
    public long incrementUsage() {
        updateTimestamp();
        return usageCount.incrementAndGet();
    }
    
    /**
     * Decrement usage count
     */
    public long decrementUsage() {
        long current = usageCount.get();
        if (current > 0) {
            updateTimestamp();
            return usageCount.decrementAndGet();
        }
        return current;
    }
    
    /**
     * Check if tag matches search query
     */
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check name
        if (name.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check description
        if (description != null && description.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check synonyms
        return synonyms.stream().anyMatch(synonym -> synonym.contains(lowerQuery));
    }
    
    /**
     * Get tag hierarchy depth
     */
    public int getHierarchyDepth() {
        // This would need to be calculated by the service with full tag tree
        return metadata.containsKey("depth") ? (Integer) metadata.get("depth") : 0;
    }
    
    /**
     * Add metadata
     */
    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
        updateTimestamp();
    }
    
    /**
     * Get metadata
     */
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    /**
     * Check if tag is root (has no parent)
     */
    public boolean isRoot() {
        return parentTagId == null;
    }
    
    /**
     * Check if tag is leaf (has no children)
     */
    public boolean isLeaf() {
        return childTagIds.isEmpty();
    }
    
    /**
     * Get usage statistics
     */
    public TagUsageStats getUsageStats() {
        return new TagUsageStats(
            usageCount.get(),
            associatedProjects.size(),
            childTagIds.size(),
            createdAt,
            updatedAt
        );
    }
    
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    public String getTagId() { return tagId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getColor() { return color; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public TagStatus getStatus() { return status; }
    public String getParentTagId() { return parentTagId; }
    public Set<String> getChildTagIds() { return new HashSet<>(childTagIds); }
    public long getUsageCount() { return usageCount.get(); }
    public Set<String> getAssociatedProjects() { return new HashSet<>(associatedProjects); }
    public Set<String> getSynonyms() { return new HashSet<>(synonyms); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return Objects.equals(tagId, tag.tagId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }
    
    @Override
    public String toString() {
        return String.format("Tag{id='%s', name='%s', status=%s, usage=%d, children=%d}", 
                           tagId, name, status, usageCount.get(), childTagIds.size());
    }
    
    // Inner class for usage statistics
    public static class TagUsageStats {
        private final long usageCount;
        private final int projectCount;
        private final int childCount;
        private final LocalDateTime createdAt;
        private final LocalDateTime lastUsed;
        
        public TagUsageStats(long usageCount, int projectCount, int childCount, 
                           LocalDateTime createdAt, LocalDateTime lastUsed) {
            this.usageCount = usageCount;
            this.projectCount = projectCount;
            this.childCount = childCount;
            this.createdAt = createdAt;
            this.lastUsed = lastUsed;
        }
        
        public long getUsageCount() { return usageCount; }
        public int getProjectCount() { return projectCount; }
        public int getChildCount() { return childCount; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getLastUsed() { return lastUsed; }
    }
}
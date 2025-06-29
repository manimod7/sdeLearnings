package lld.tagmanagement;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an association between a tag and an entity
 * Supports both JIRA issues and Confluence pages
 */
public class TagAssociation {
    private final String associationId;
    private final String tagId;
    private final String entityId;
    private final EntityType entityType;
    private final String associatedBy;
    private final LocalDateTime associatedAt;
    private final String projectId;
    
    public enum EntityType {
        JIRA_ISSUE,
        CONFLUENCE_PAGE,
        PROJECT,
        COMPONENT
    }
    
    public TagAssociation(String associationId, String tagId, String entityId, 
                         EntityType entityType, String associatedBy, String projectId) {
        this.associationId = associationId;
        this.tagId = tagId;
        this.entityId = entityId;
        this.entityType = entityType;
        this.associatedBy = associatedBy;
        this.associatedAt = LocalDateTime.now();
        this.projectId = projectId;
    }
    
    /**
     * Check if association matches filter criteria
     */
    public boolean matches(String tagIdFilter, EntityType entityTypeFilter, String projectIdFilter) {
        if (tagIdFilter != null && !tagId.equals(tagIdFilter)) {
            return false;
        }
        
        if (entityTypeFilter != null && entityType != entityTypeFilter) {
            return false;
        }
        
        if (projectIdFilter != null && !Objects.equals(projectId, projectIdFilter)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Generate association key for indexing
     */
    public String getAssociationKey() {
        return tagId + ":" + entityType + ":" + entityId;
    }
    
    // Getters
    public String getAssociationId() { return associationId; }
    public String getTagId() { return tagId; }
    public String getEntityId() { return entityId; }
    public EntityType getEntityType() { return entityType; }
    public String getAssociatedBy() { return associatedBy; }
    public LocalDateTime getAssociatedAt() { return associatedAt; }
    public String getProjectId() { return projectId; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TagAssociation that = (TagAssociation) obj;
        return Objects.equals(tagId, that.tagId) &&
               Objects.equals(entityId, that.entityId) &&
               entityType == that.entityType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tagId, entityId, entityType);
    }
    
    @Override
    public String toString() {
        return String.format("TagAssociation{tag='%s', entity='%s'(%s), project='%s'}", 
                           tagId, entityId, entityType, projectId);
    }
}
package lld.tagmanagement;

/**
 * Enum representing different user roles in the tag management system
 * Used for role-based access control
 */
public enum UserRole {
    ADMIN,           // Can create, manage, and delete tags; view all analytics
    DEVELOPER,       // Can apply tags to JIRA issues and projects
    CONTENT_CREATOR, // Can add tags to Confluence pages
    VIEWER           // Can view and search tags but not modify
}
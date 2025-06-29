package lld.tagmanagement;

/**
 * Enum representing the status of a tag
 * Used for tag lifecycle management
 */
public enum TagStatus {
    ACTIVE,     // Tag is active and can be used
    INACTIVE,   // Tag is deactivated but preserved for history
    DEPRECATED  // Tag is deprecated with suggested replacement
}
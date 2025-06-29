package lld.scorecard;

/**
 * Enum representing the lifecycle states of a scorecard
 * Used in the State pattern for scorecard management
 */
public enum ScorecardState {
    DRAFT,      // Admin can edit, not yet submitted
    SUBMITTED,  // Evaluator has submitted, pending finalization
    FINALIZED,  // Admin has finalized, user can view
    ARCHIVED    // Historical record, read-only
}
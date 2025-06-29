package lld.scorecard;

/**
 * Enum representing different user roles in the scorecard system
 * Used for role-based access control
 */
public enum UserRole {
    ADMIN,      // Can create scorecards, manage evaluators, finalize scores
    EVALUATOR,  // Can fill out scorecards, submit evaluations
    USER        // Can view finalized scorecards
}
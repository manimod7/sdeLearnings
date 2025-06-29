package lld.scorecard;

/**
 * Enum representing different types of responses for questions
 * Supports various evaluation formats
 */
public enum ResponseType {
    NUMERIC,        // Numeric score (e.g., 1-10)
    TEXT,           // Text feedback
    MULTIPLE_CHOICE,// Select from predefined options
    BOOLEAN,        // Yes/No response
    RATING,         // Star rating (1-5)
    PERCENTAGE      // Percentage score (0-100)
}
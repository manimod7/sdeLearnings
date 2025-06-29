package lld.scorecard;

import java.util.*;

/**
 * Represents a question in a scorecard section
 * Supports different response types and scoring rules
 */
public class Question {
    private final String questionId;
    private final String text;
    private final ResponseType responseType;
    private final double weight;
    private final boolean mandatory;
    private final Map<String, Object> constraints;
    private final List<String> options; // For multiple choice questions
    private final String helpText;
    
    public Question(String questionId, String text, ResponseType responseType, 
                   double weight, boolean mandatory) {
        this.questionId = questionId;
        this.text = text;
        this.responseType = responseType;
        this.weight = weight;
        this.mandatory = mandatory;
        this.constraints = new HashMap<>();
        this.options = new ArrayList<>();
        this.helpText = "";
    }
    
    public Question(String questionId, String text, ResponseType responseType, 
                   double weight, boolean mandatory, List<String> options, String helpText) {
        this.questionId = questionId;
        this.text = text;
        this.responseType = responseType;
        this.weight = weight;
        this.mandatory = mandatory;
        this.constraints = new HashMap<>();
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.helpText = helpText != null ? helpText : "";
    }
    
    /**
     * Validate response based on question type and constraints
     */
    public ValidationResult validateResponse(Object response) {
        if (response == null && mandatory) {
            return new ValidationResult(false, "Response is required for mandatory question");
        }
        
        if (response == null) {
            return new ValidationResult(true, "");
        }
        
        switch (responseType) {
            case NUMERIC:
                return validateNumericResponse(response);
            case TEXT:
                return validateTextResponse(response);
            case MULTIPLE_CHOICE:
                return validateMultipleChoiceResponse(response);
            case BOOLEAN:
                return validateBooleanResponse(response);
            case RATING:
                return validateRatingResponse(response);
            case PERCENTAGE:
                return validatePercentageResponse(response);
            default:
                return new ValidationResult(false, "Unknown response type");
        }
    }
    
    private ValidationResult validateNumericResponse(Object response) {
        try {
            double value = ((Number) response).doubleValue();
            
            if (constraints.containsKey("min")) {
                double min = ((Number) constraints.get("min")).doubleValue();
                if (value < min) {
                    return new ValidationResult(false, "Value must be at least " + min);
                }
            }
            
            if (constraints.containsKey("max")) {
                double max = ((Number) constraints.get("max")).doubleValue();
                if (value > max) {
                    return new ValidationResult(false, "Value must be at most " + max);
                }
            }
            
            return new ValidationResult(true, "");
        } catch (ClassCastException | NullPointerException e) {
            return new ValidationResult(false, "Invalid numeric value");
        }
    }
    
    private ValidationResult validateTextResponse(Object response) {
        if (!(response instanceof String)) {
            return new ValidationResult(false, "Text response expected");
        }
        
        String text = (String) response;
        
        if (constraints.containsKey("minLength")) {
            int minLength = ((Number) constraints.get("minLength")).intValue();
            if (text.length() < minLength) {
                return new ValidationResult(false, "Text must be at least " + minLength + " characters");
            }
        }
        
        if (constraints.containsKey("maxLength")) {
            int maxLength = ((Number) constraints.get("maxLength")).intValue();
            if (text.length() > maxLength) {
                return new ValidationResult(false, "Text must be at most " + maxLength + " characters");
            }
        }
        
        return new ValidationResult(true, "");
    }
    
    private ValidationResult validateMultipleChoiceResponse(Object response) {
        if (!(response instanceof String)) {
            return new ValidationResult(false, "String response expected for multiple choice");
        }
        
        String choice = (String) response;
        if (!options.contains(choice)) {
            return new ValidationResult(false, "Invalid choice: " + choice);
        }
        
        return new ValidationResult(true, "");
    }
    
    private ValidationResult validateBooleanResponse(Object response) {
        if (!(response instanceof Boolean)) {
            return new ValidationResult(false, "Boolean response expected");
        }
        
        return new ValidationResult(true, "");
    }
    
    private ValidationResult validateRatingResponse(Object response) {
        try {
            int rating = ((Number) response).intValue();
            if (rating < 1 || rating > 5) {
                return new ValidationResult(false, "Rating must be between 1 and 5");
            }
            return new ValidationResult(true, "");
        } catch (ClassCastException | NullPointerException e) {
            return new ValidationResult(false, "Invalid rating value");
        }
    }
    
    private ValidationResult validatePercentageResponse(Object response) {
        try {
            double percentage = ((Number) response).doubleValue();
            if (percentage < 0 || percentage > 100) {
                return new ValidationResult(false, "Percentage must be between 0 and 100");
            }
            return new ValidationResult(true, "");
        } catch (ClassCastException | NullPointerException e) {
            return new ValidationResult(false, "Invalid percentage value");
        }
    }
    
    /**
     * Calculate normalized score based on response
     */
    public double calculateScore(Object response) {
        ValidationResult validation = validateResponse(response);
        if (!validation.isValid()) {
            return 0.0;
        }
        
        if (response == null) {
            return 0.0;
        }
        
        switch (responseType) {
            case NUMERIC:
                return calculateNumericScore(response);
            case RATING:
                return calculateRatingScore(response);
            case PERCENTAGE:
                return calculatePercentageScore(response);
            case BOOLEAN:
                return calculateBooleanScore(response);
            case MULTIPLE_CHOICE:
                return calculateMultipleChoiceScore(response);
            case TEXT:
                return 1.0; // Text responses get full score if valid
            default:
                return 0.0;
        }
    }
    
    private double calculateNumericScore(Object response) {
        double value = ((Number) response).doubleValue();
        double min = constraints.containsKey("min") ? 
            ((Number) constraints.get("min")).doubleValue() : 0;
        double max = constraints.containsKey("max") ? 
            ((Number) constraints.get("max")).doubleValue() : 10;
        
        // Normalize to 0-1 range
        return (value - min) / (max - min);
    }
    
    private double calculateRatingScore(Object response) {
        int rating = ((Number) response).intValue();
        return (rating - 1) / 4.0; // Normalize 1-5 to 0-1
    }
    
    private double calculatePercentageScore(Object response) {
        double percentage = ((Number) response).doubleValue();
        return percentage / 100.0; // Normalize to 0-1
    }
    
    private double calculateBooleanScore(Object response) {
        Boolean bool = (Boolean) response;
        return bool ? 1.0 : 0.0;
    }
    
    private double calculateMultipleChoiceScore(Object response) {
        // For multiple choice, can assign different scores to different options
        // For simplicity, all valid choices get full score
        return 1.0;
    }
    
    // Builder pattern for creating questions with constraints
    public static class Builder {
        private final String questionId;
        private final String text;
        private final ResponseType responseType;
        private double weight = 1.0;
        private boolean mandatory = false;
        private final Map<String, Object> constraints = new HashMap<>();
        private final List<String> options = new ArrayList<>();
        private String helpText = "";
        
        public Builder(String questionId, String text, ResponseType responseType) {
            this.questionId = questionId;
            this.text = text;
            this.responseType = responseType;
        }
        
        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }
        
        public Builder mandatory() {
            this.mandatory = true;
            return this;
        }
        
        public Builder minValue(double min) {
            this.constraints.put("min", min);
            return this;
        }
        
        public Builder maxValue(double max) {
            this.constraints.put("max", max);
            return this;
        }
        
        public Builder minLength(int min) {
            this.constraints.put("minLength", min);
            return this;
        }
        
        public Builder maxLength(int max) {
            this.constraints.put("maxLength", max);
            return this;
        }
        
        public Builder addOption(String option) {
            this.options.add(option);
            return this;
        }
        
        public Builder options(List<String> options) {
            this.options.clear();
            this.options.addAll(options);
            return this;
        }
        
        public Builder helpText(String helpText) {
            this.helpText = helpText;
            return this;
        }
        
        public Question build() {
            Question question = new Question(questionId, text, responseType, weight, mandatory, options, helpText);
            question.constraints.putAll(this.constraints);
            return question;
        }
    }
    
    // Getters
    public String getQuestionId() { return questionId; }
    public String getText() { return text; }
    public ResponseType getResponseType() { return responseType; }
    public double getWeight() { return weight; }
    public boolean isMandatory() { return mandatory; }
    public List<String> getOptions() { return new ArrayList<>(options); }
    public String getHelpText() { return helpText; }
    public Map<String, Object> getConstraints() { return new HashMap<>(constraints); }
    
    @Override
    public String toString() {
        return String.format("Question{id='%s', type=%s, weight=%.2f, mandatory=%s}", 
                           questionId, responseType, weight, mandatory);
    }
    
    // Inner class for validation results
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
}
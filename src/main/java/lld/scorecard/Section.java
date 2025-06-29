package lld.scorecard;

import java.util.*;

/**
 * Represents a section within a scorecard containing related questions
 * Supports weighted scoring and section-level configurations
 */
public class Section {
    private final String sectionId;
    private final String title;
    private final String description;
    private final double weight;
    private final List<Question> questions;
    private final Map<String, Object> metadata;
    
    public Section(String sectionId, String title, String description, double weight) {
        this.sectionId = sectionId;
        this.title = title;
        this.description = description;
        this.weight = weight;
        this.questions = new ArrayList<>();
        this.metadata = new HashMap<>();
    }
    
    /**
     * Add question to section
     */
    public void addQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        
        // Check for duplicate question IDs
        for (Question existing : questions) {
            if (existing.getQuestionId().equals(question.getQuestionId())) {
                throw new IllegalArgumentException("Question ID already exists: " + question.getQuestionId());
            }
        }
        
        questions.add(question);
    }
    
    /**
     * Remove question from section
     */
    public boolean removeQuestion(String questionId) {
        return questions.removeIf(q -> q.getQuestionId().equals(questionId));
    }
    
    /**
     * Get question by ID
     */
    public Question getQuestion(String questionId) {
        return questions.stream()
                       .filter(q -> q.getQuestionId().equals(questionId))
                       .findFirst()
                       .orElse(null);
    }
    
    /**
     * Validate all responses in this section
     */
    public SectionValidationResult validateResponses(Map<String, Object> responses) {
        List<String> errors = new ArrayList<>();
        Map<String, Question.ValidationResult> questionResults = new HashMap<>();
        
        // Check all questions
        for (Question question : questions) {
            Object response = responses.get(question.getQuestionId());
            Question.ValidationResult result = question.validateResponse(response);
            questionResults.put(question.getQuestionId(), result);
            
            if (!result.isValid()) {
                errors.add(String.format("Question %s: %s", 
                                        question.getQuestionId(), result.getErrorMessage()));
            }
        }
        
        // Check for mandatory questions
        for (Question question : questions) {
            if (question.isMandatory() && !responses.containsKey(question.getQuestionId())) {
                errors.add(String.format("Missing response for mandatory question: %s", 
                                        question.getQuestionId()));
            }
        }
        
        return new SectionValidationResult(errors.isEmpty(), errors, questionResults);
    }
    
    /**
     * Calculate section score based on responses
     */
    public SectionScore calculateScore(Map<String, Object> responses) {
        double totalWeightedScore = 0.0;
        double totalWeight = 0.0;
        int answeredQuestions = 0;
        Map<String, Double> questionScores = new HashMap<>();
        
        for (Question question : questions) {
            Object response = responses.get(question.getQuestionId());
            
            if (response != null) {
                double questionScore = question.calculateScore(response);
                double weightedScore = questionScore * question.getWeight();
                
                totalWeightedScore += weightedScore;
                totalWeight += question.getWeight();
                answeredQuestions++;
                
                questionScores.put(question.getQuestionId(), questionScore);
            }
        }
        
        double sectionScore = totalWeight > 0 ? totalWeightedScore / totalWeight : 0.0;
        double completionRate = questions.size() > 0 ? 
            (double) answeredQuestions / questions.size() : 0.0;
        
        return new SectionScore(sectionScore, completionRate, questionScores, answeredQuestions, questions.size());
    }
    
    /**
     * Get section statistics
     */
    public SectionStatistics getStatistics() {
        int totalQuestions = questions.size();
        int mandatoryQuestions = (int) questions.stream().filter(Question::isMandatory).count();
        
        Map<ResponseType, Integer> responseTypeCounts = new HashMap<>();
        double totalWeight = 0.0;
        
        for (Question question : questions) {
            ResponseType type = question.getResponseType();
            responseTypeCounts.put(type, responseTypeCounts.getOrDefault(type, 0) + 1);
            totalWeight += question.getWeight();
        }
        
        return new SectionStatistics(totalQuestions, mandatoryQuestions, responseTypeCounts, totalWeight);
    }
    
    // Getters
    public String getSectionId() { return sectionId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getWeight() { return weight; }
    public List<Question> getQuestions() { return new ArrayList<>(questions); }
    public int getQuestionCount() { return questions.size(); }
    
    // Metadata management
    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    @Override
    public String toString() {
        return String.format("Section{id='%s', title='%s', questions=%d, weight=%.2f}", 
                           sectionId, title, questions.size(), weight);
    }
    
    // Inner classes for results and statistics
    public static class SectionValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final Map<String, Question.ValidationResult> questionResults;
        
        public SectionValidationResult(boolean valid, List<String> errors, 
                                     Map<String, Question.ValidationResult> questionResults) {
            this.valid = valid;
            this.errors = new ArrayList<>(errors);
            this.questionResults = new HashMap<>(questionResults);
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return new ArrayList<>(errors); }
        public Map<String, Question.ValidationResult> getQuestionResults() { 
            return new HashMap<>(questionResults); 
        }
    }
    
    public static class SectionScore {
        private final double score;
        private final double completionRate;
        private final Map<String, Double> questionScores;
        private final int answeredQuestions;
        private final int totalQuestions;
        
        public SectionScore(double score, double completionRate, Map<String, Double> questionScores,
                          int answeredQuestions, int totalQuestions) {
            this.score = score;
            this.completionRate = completionRate;
            this.questionScores = new HashMap<>(questionScores);
            this.answeredQuestions = answeredQuestions;
            this.totalQuestions = totalQuestions;
        }
        
        public double getScore() { return score; }
        public double getCompletionRate() { return completionRate; }
        public Map<String, Double> getQuestionScores() { return new HashMap<>(questionScores); }
        public int getAnsweredQuestions() { return answeredQuestions; }
        public int getTotalQuestions() { return totalQuestions; }
        
        @Override
        public String toString() {
            return String.format("SectionScore{score=%.2f, completion=%.1f%%, answered=%d/%d}", 
                               score, completionRate * 100, answeredQuestions, totalQuestions);
        }
    }
    
    public static class SectionStatistics {
        private final int totalQuestions;
        private final int mandatoryQuestions;
        private final Map<ResponseType, Integer> responseTypeCounts;
        private final double totalWeight;
        
        public SectionStatistics(int totalQuestions, int mandatoryQuestions,
                               Map<ResponseType, Integer> responseTypeCounts, double totalWeight) {
            this.totalQuestions = totalQuestions;
            this.mandatoryQuestions = mandatoryQuestions;
            this.responseTypeCounts = new HashMap<>(responseTypeCounts);
            this.totalWeight = totalWeight;
        }
        
        public int getTotalQuestions() { return totalQuestions; }
        public int getMandatoryQuestions() { return mandatoryQuestions; }
        public Map<ResponseType, Integer> getResponseTypeCounts() { 
            return new HashMap<>(responseTypeCounts); 
        }
        public double getTotalWeight() { return totalWeight; }
    }
}
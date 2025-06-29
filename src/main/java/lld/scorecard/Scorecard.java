package lld.scorecard;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Main scorecard entity representing an evaluation template
 * Contains multiple sections and manages scorecard lifecycle
 */
public class Scorecard {
    private final String scorecardId;
    private final String title;
    private final String description;
    private final String createdBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ScorecardState state;
    private final List<Section> sections;
    private final Set<String> assignedEvaluators;
    private String targetUserId;
    private final Map<String, Object> metadata;
    
    public Scorecard(String scorecardId, String title, String description, String createdBy) {
        this.scorecardId = scorecardId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.state = ScorecardState.DRAFT;
        this.sections = new ArrayList<>();
        this.assignedEvaluators = new HashSet<>();
        this.metadata = new HashMap<>();
    }
    
    /**
     * Add section to scorecard
     */
    public synchronized void addSection(Section section) {
        if (state != ScorecardState.DRAFT) {
            throw new IllegalStateException("Cannot modify scorecard in state: " + state);
        }
        
        // Check for duplicate section IDs
        for (Section existing : sections) {
            if (existing.getSectionId().equals(section.getSectionId())) {
                throw new IllegalArgumentException("Section ID already exists: " + section.getSectionId());
            }
        }
        
        sections.add(section);
        updateTimestamp();
    }
    
    /**
     * Remove section from scorecard
     */
    public synchronized boolean removeSection(String sectionId) {
        if (state != ScorecardState.DRAFT) {
            throw new IllegalStateException("Cannot modify scorecard in state: " + state);
        }
        
        boolean removed = sections.removeIf(s -> s.getSectionId().equals(sectionId));
        if (removed) {
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Assign evaluator to scorecard
     */
    public synchronized boolean assignEvaluator(String evaluatorId) {
        boolean added = assignedEvaluators.add(evaluatorId);
        if (added) {
            updateTimestamp();
        }
        return added;
    }
    
    /**
     * Remove evaluator assignment
     */
    public synchronized boolean removeEvaluator(String evaluatorId) {
        boolean removed = assignedEvaluators.remove(evaluatorId);
        if (removed) {
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Set target user for evaluation
     */
    public synchronized void setTargetUser(String userId) {
        this.targetUserId = userId;
        updateTimestamp();
    }
    
    /**
     * Change scorecard state
     */
    public synchronized boolean changeState(ScorecardState newState, String userId) {
        if (!canChangeState(state, newState, userId)) {
            return false;
        }
        
        this.state = newState;
        updateTimestamp();
        return true;
    }
    
    /**
     * Check if state change is valid
     */
    private boolean canChangeState(ScorecardState currentState, ScorecardState newState, String userId) {
        // State transition rules
        switch (currentState) {
            case DRAFT:
                return newState == ScorecardState.SUBMITTED && assignedEvaluators.contains(userId);
            case SUBMITTED:
                return newState == ScorecardState.FINALIZED && createdBy.equals(userId);
            case FINALIZED:
                return newState == ScorecardState.ARCHIVED && createdBy.equals(userId);
            case ARCHIVED:
                return false; // No transitions from archived state
            default:
                return false;
        }
    }
    
    /**
     * Validate scorecard responses
     */
    public ScorecardValidationResult validateResponses(Map<String, Map<String, Object>> responses) {
        List<String> errors = new ArrayList<>();
        Map<String, Section.SectionValidationResult> sectionResults = new HashMap<>();
        
        for (Section section : sections) {
            Map<String, Object> sectionResponses = responses.getOrDefault(section.getSectionId(), new HashMap<>());
            Section.SectionValidationResult result = section.validateResponses(sectionResponses);
            sectionResults.put(section.getSectionId(), result);
            
            if (!result.isValid()) {
                errors.addAll(result.getErrors());
            }
        }
        
        return new ScorecardValidationResult(errors.isEmpty(), errors, sectionResults);
    }
    
    /**
     * Calculate overall scorecard score
     */
    public ScorecardScore calculateScore(Map<String, Map<String, Object>> responses) {
        double totalWeightedScore = 0.0;
        double totalWeight = 0.0;
        Map<String, Section.SectionScore> sectionScores = new HashMap<>();
        
        for (Section section : sections) {
            Map<String, Object> sectionResponses = responses.getOrDefault(section.getSectionId(), new HashMap<>());
            Section.SectionScore sectionScore = section.calculateScore(sectionResponses);
            sectionScores.put(section.getSectionId(), sectionScore);
            
            double weightedScore = sectionScore.getScore() * section.getWeight();
            totalWeightedScore += weightedScore;
            totalWeight += section.getWeight();
        }
        
        double overallScore = totalWeight > 0 ? totalWeightedScore / totalWeight : 0.0;
        
        return new ScorecardScore(overallScore, sectionScores);
    }
    
    /**
     * Check if user has permission to perform action
     */
    public boolean hasPermission(String userId, String action) {
        switch (action.toLowerCase()) {
            case "edit":
                return state == ScorecardState.DRAFT && createdBy.equals(userId);
            case "evaluate":
                return (state == ScorecardState.DRAFT || state == ScorecardState.SUBMITTED) && 
                       assignedEvaluators.contains(userId);
            case "finalize":
                return state == ScorecardState.SUBMITTED && createdBy.equals(userId);
            case "view":
                return createdBy.equals(userId) || assignedEvaluators.contains(userId) || 
                       (state == ScorecardState.FINALIZED && userId.equals(targetUserId));
            default:
                return false;
        }
    }
    
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    public String getScorecardId() { return scorecardId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public ScorecardState getState() { return state; }
    public List<Section> getSections() { return new ArrayList<>(sections); }
    public Set<String> getAssignedEvaluators() { return new HashSet<>(assignedEvaluators); }
    public String getTargetUserId() { return targetUserId; }
    
    @Override
    public String toString() {
        return String.format("Scorecard{id='%s', title='%s', state=%s, sections=%d}", 
                           scorecardId, title, state, sections.size());
    }
    
    // Inner classes
    public static class ScorecardValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final Map<String, Section.SectionValidationResult> sectionResults;
        
        public ScorecardValidationResult(boolean valid, List<String> errors, 
                                       Map<String, Section.SectionValidationResult> sectionResults) {
            this.valid = valid;
            this.errors = new ArrayList<>(errors);
            this.sectionResults = new HashMap<>(sectionResults);
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return new ArrayList<>(errors); }
        public Map<String, Section.SectionValidationResult> getSectionResults() { 
            return new HashMap<>(sectionResults); 
        }
    }
    
    public static class ScorecardScore {
        private final double overallScore;
        private final Map<String, Section.SectionScore> sectionScores;
        
        public ScorecardScore(double overallScore, Map<String, Section.SectionScore> sectionScores) {
            this.overallScore = overallScore;
            this.sectionScores = new HashMap<>(sectionScores);
        }
        
        public double getOverallScore() { return overallScore; }
        public Map<String, Section.SectionScore> getSectionScores() { 
            return new HashMap<>(sectionScores); 
        }
        
        @Override
        public String toString() {
            return String.format("ScorecardScore{overall=%.2f, sections=%d}", 
                               overallScore, sectionScores.size());
        }
    }
}
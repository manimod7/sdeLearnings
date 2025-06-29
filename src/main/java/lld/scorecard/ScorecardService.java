package lld.scorecard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Main service class for scorecard management system
 * Provides comprehensive API for scorecard operations
 */
public class ScorecardService {
    private final Map<String, Scorecard> scorecards;
    private final Map<String, User> users;
    private final Map<String, Map<String, Map<String, Object>>> evaluations; // scorecardId -> evaluatorId -> responses
    private final NotificationService notificationService;
    
    public ScorecardService() {
        this.scorecards = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.evaluations = new ConcurrentHashMap<>();
        this.notificationService = new NotificationService();
    }
    
    /**
     * Register a user in the system
     */
    public boolean registerUser(String userId, String name, String email, UserRole role) {
        if (users.containsKey(userId)) {
            return false;
        }
        
        User user = new User(userId, name, email, role);
        users.put(userId, user);
        return true;
    }
    
    /**
     * Create a new scorecard
     */
    public String createScorecard(String title, String description, String createdBy) {
        User creator = users.get(createdBy);
        if (creator == null || creator.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admins can create scorecards");
        }
        
        String scorecardId = generateScorecardId();
        Scorecard scorecard = new Scorecard(scorecardId, title, description, createdBy);
        scorecards.put(scorecardId, scorecard);
        
        return scorecardId;
    }
    
    /**
     * Add section to scorecard
     */
    public boolean addSection(String scorecardId, Section section, String userId) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard == null || !scorecard.hasPermission(userId, "edit")) {
            return false;
        }
        
        try {
            scorecard.addSection(section);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Assign evaluator to scorecard
     */
    public boolean assignEvaluator(String scorecardId, String evaluatorId, String assignedBy) {
        Scorecard scorecard = scorecards.get(scorecardId);
        User evaluator = users.get(evaluatorId);
        
        if (scorecard == null || evaluator == null || 
            !scorecard.hasPermission(assignedBy, "edit") ||
            evaluator.getRole() != UserRole.EVALUATOR) {
            return false;
        }
        
        boolean assigned = scorecard.assignEvaluator(evaluatorId);
        if (assigned) {
            notificationService.sendNotification(evaluatorId, 
                "You have been assigned to evaluate: " + scorecard.getTitle());
        }
        
        return assigned;
    }
    
    /**
     * Submit evaluation responses
     */
    public boolean submitEvaluation(String scorecardId, String evaluatorId, 
                                  Map<String, Map<String, Object>> responses) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard == null || !scorecard.hasPermission(evaluatorId, "evaluate")) {
            return false;
        }
        
        // Validate responses
        Scorecard.ScorecardValidationResult validation = scorecard.validateResponses(responses);
        if (!validation.isValid()) {
            return false;
        }
        
        // Store evaluation
        evaluations.computeIfAbsent(scorecardId, k -> new ConcurrentHashMap<>())
                   .put(evaluatorId, responses);
        
        // Change state to submitted if not already
        if (scorecard.getState() == ScorecardState.DRAFT) {
            scorecard.changeState(ScorecardState.SUBMITTED, evaluatorId);
            
            // Notify admin
            notificationService.sendNotification(scorecard.getCreatedBy(),
                "Scorecard evaluation submitted: " + scorecard.getTitle());
        }
        
        return true;
    }
    
    /**
     * Finalize scorecard
     */
    public boolean finalizeScorecard(String scorecardId, String adminId) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard == null || !scorecard.hasPermission(adminId, "finalize")) {
            return false;
        }
        
        boolean finalized = scorecard.changeState(ScorecardState.FINALIZED, adminId);
        if (finalized && scorecard.getTargetUserId() != null) {
            notificationService.sendNotification(scorecard.getTargetUserId(),
                "Your scorecard has been finalized: " + scorecard.getTitle());
        }
        
        return finalized;
    }
    
    /**
     * Get scorecard details
     */
    public Scorecard getScorecard(String scorecardId, String userId) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard == null || !scorecard.hasPermission(userId, "view")) {
            return null;
        }
        
        return scorecard;
    }
    
    /**
     * Get scorecards for user based on role
     */
    public List<ScorecardSummary> getUserScorecards(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        
        List<ScorecardSummary> summaries = new ArrayList<>();
        
        for (Scorecard scorecard : scorecards.values()) {
            if (scorecard.hasPermission(userId, "view")) {
                summaries.add(new ScorecardSummary(scorecard, userId));
            }
        }
        
        return summaries;
    }
    
    /**
     * Calculate scorecard score
     */
    public Scorecard.ScorecardScore calculateScore(String scorecardId, String evaluatorId) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard == null) {
            return null;
        }
        
        Map<String, Map<String, Object>> responses = 
            evaluations.getOrDefault(scorecardId, Collections.emptyMap())
                      .getOrDefault(evaluatorId, Collections.emptyMap());
        
        return scorecard.calculateScore(responses);
    }
    
    /**
     * Generate scorecard report
     */
    public ScorecardReport generateReport(String scorecardId, String userId) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard == null || !scorecard.hasPermission(userId, "view")) {
            return null;
        }
        
        Map<String, Scorecard.ScorecardScore> evaluatorScores = new HashMap<>();
        Map<String, Map<String, Object>> allEvaluations = 
            evaluations.getOrDefault(scorecardId, Collections.emptyMap());
        
        for (Map.Entry<String, Map<String, Object>> entry : allEvaluations.entrySet()) {
            String evaluatorId = entry.getKey();
            Scorecard.ScorecardScore score = scorecard.calculateScore(
                Collections.singletonMap(scorecardId, entry.getValue()));
            evaluatorScores.put(evaluatorId, score);
        }
        
        return new ScorecardReport(scorecard, evaluatorScores);
    }
    
    /**
     * Search scorecards
     */
    public List<ScorecardSummary> searchScorecards(String userId, String query, 
                                                 ScorecardState state, String createdBy) {
        User user = users.get(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        
        return scorecards.values().stream()
                        .filter(scorecard -> scorecard.hasPermission(userId, "view"))
                        .filter(scorecard -> matchesQuery(scorecard, query))
                        .filter(scorecard -> state == null || scorecard.getState() == state)
                        .filter(scorecard -> createdBy == null || scorecard.getCreatedBy().equals(createdBy))
                        .map(scorecard -> new ScorecardSummary(scorecard, userId))
                        .collect(Collectors.toList());
    }
    
    private boolean matchesQuery(Scorecard scorecard, String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        return scorecard.getTitle().toLowerCase().contains(lowerQuery) ||
               scorecard.getDescription().toLowerCase().contains(lowerQuery);
    }
    
    private String generateScorecardId() {
        return "scorecard_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    // Inner classes
    public static class User {
        private final String userId;
        private final String name;
        private final String email;
        private final UserRole role;
        
        public User(String userId, String name, String email, UserRole role) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role;
        }
        
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public UserRole getRole() { return role; }
    }
    
    public static class ScorecardSummary {
        public final String scorecardId;
        public final String title;
        public final String createdBy;
        public final ScorecardState state;
        public final int sectionCount;
        public final String userRole;
        
        public ScorecardSummary(Scorecard scorecard, String userId) {
            this.scorecardId = scorecard.getScorecardId();
            this.title = scorecard.getTitle();
            this.createdBy = scorecard.getCreatedBy();
            this.state = scorecard.getState();
            this.sectionCount = scorecard.getSections().size();
            
            if (scorecard.getCreatedBy().equals(userId)) {
                this.userRole = "CREATOR";
            } else if (scorecard.getAssignedEvaluators().contains(userId)) {
                this.userRole = "EVALUATOR";
            } else {
                this.userRole = "TARGET";
            }
        }
    }
    
    public static class ScorecardReport {
        private final Scorecard scorecard;
        private final Map<String, Scorecard.ScorecardScore> evaluatorScores;
        
        public ScorecardReport(Scorecard scorecard, Map<String, Scorecard.ScorecardScore> evaluatorScores) {
            this.scorecard = scorecard;
            this.evaluatorScores = new HashMap<>(evaluatorScores);
        }
        
        public Scorecard getScorecard() { return scorecard; }
        public Map<String, Scorecard.ScorecardScore> getEvaluatorScores() { 
            return new HashMap<>(evaluatorScores); 
        }
        
        public double getAverageScore() {
            return evaluatorScores.values().stream()
                                 .mapToDouble(Scorecard.ScorecardScore::getOverallScore)
                                 .average()
                                 .orElse(0.0);
        }
    }
    
    // Simple notification service
    private static class NotificationService {
        public void sendNotification(String userId, String message) {
            System.out.println("📧 Notification to " + userId + ": " + message);
        }
    }
}
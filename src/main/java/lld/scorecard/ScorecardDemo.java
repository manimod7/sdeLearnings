package lld.scorecard;

import java.util.*;

/**
 * Demo class showcasing the Scorecard Management System
 * Demonstrates comprehensive scorecard lifecycle and evaluation process
 */
public class ScorecardDemo {
    private static final ScorecardService service = new ScorecardService();
    
    public static void main(String[] args) {
        System.out.println("📋 Scorecard Management System Demo");
        System.out.println("===================================");
        
        // Run comprehensive demo scenarios
        runDemoScenarios();
        
        System.out.println("\n🎉 Demo completed successfully!");
    }
    
    /**
     * Run comprehensive demo scenarios
     */
    private static void runDemoScenarios() {
        System.out.println("\n🚀 Running Demo Scenarios...\n");
        
        // Scenario 1: User setup and scorecard creation
        System.out.println("Scenario 1: User Setup and Scorecard Creation");
        System.out.println("----------------------------------------------");
        
        // Register users with different roles
        service.registerUser("admin1", "Alice Admin", "alice@company.com", UserRole.ADMIN);
        service.registerUser("eval1", "Bob Evaluator", "bob@company.com", UserRole.EVALUATOR);
        service.registerUser("eval2", "Carol Evaluator", "carol@company.com", UserRole.EVALUATOR);
        service.registerUser("user1", "David Employee", "david@company.com", UserRole.USER);
        System.out.println("✅ Registered 4 users (1 admin, 2 evaluators, 1 user)");
        
        // Create performance review scorecard
        String scorecardId = service.createScorecard(
            "Employee Performance Review Q4", 
            "Quarterly performance evaluation for employees", 
            "admin1");
        System.out.println("✅ Created scorecard: " + scorecardId);
        
        // Add sections to scorecard
        addPerformanceSections(scorecardId);
        System.out.println("✅ Added performance review sections");
        
        // Scenario 2: Evaluator assignment and evaluation process
        System.out.println("\nScenario 2: Evaluator Assignment and Evaluation");
        System.out.println("-----------------------------------------------");
        
        // Assign evaluators
        service.assignEvaluator(scorecardId, "eval1", "admin1");
        service.assignEvaluator(scorecardId, "eval2", "admin1");
        System.out.println("✅ Assigned 2 evaluators to scorecard");
        
        // Set target user
        Scorecard scorecard = service.getScorecard(scorecardId, "admin1");
        if (scorecard != null) {
            scorecard.setTargetUser("user1");
            System.out.println("✅ Set target user for evaluation");
        }
        
        // Submit evaluations
        submitSampleEvaluations(scorecardId);
        System.out.println("✅ Submitted sample evaluations");
        
        // Scenario 3: Score calculation and reporting
        System.out.println("\nScenario 3: Score Calculation and Reporting");
        System.out.println("-------------------------------------------");
        
        // Calculate scores for each evaluator
        Scorecard.ScorecardScore eval1Score = service.calculateScore(scorecardId, "eval1");
        Scorecard.ScorecardScore eval2Score = service.calculateScore(scorecardId, "eval2");
        
        if (eval1Score != null && eval2Score != null) {
            System.out.printf("✅ Evaluator 1 Score: %.2f\n", eval1Score.getOverallScore());
            System.out.printf("✅ Evaluator 2 Score: %.2f\n", eval2Score.getOverallScore());
        }
        
        // Generate comprehensive report
        ScorecardService.ScorecardReport report = service.generateReport(scorecardId, "admin1");
        if (report != null) {
            System.out.printf("✅ Generated report - Average Score: %.2f\n", report.getAverageScore());
        }
        
        // Scenario 4: Scorecard finalization
        System.out.println("\nScenario 4: Scorecard Finalization");
        System.out.println("----------------------------------");
        
        // Finalize scorecard
        boolean finalized = service.finalizeScorecard(scorecardId, "admin1");
        System.out.println("✅ Scorecard finalized: " + finalized);
        
        // User can now view their finalized scorecard
        Scorecard finalScorecard = service.getScorecard(scorecardId, "user1");
        if (finalScorecard != null) {
            System.out.println("✅ Target user can view finalized scorecard");
        }
        
        // Scenario 5: Search and discovery
        System.out.println("\nScenario 5: Search and Discovery");
        System.out.println("--------------------------------");
        
        // Get user scorecards
        List<ScorecardService.ScorecardSummary> adminScorecards = service.getUserScorecards("admin1");
        List<ScorecardService.ScorecardSummary> evaluatorScorecards = service.getUserScorecards("eval1");
        List<ScorecardService.ScorecardSummary> userScorecards = service.getUserScorecards("user1");
        
        System.out.println("✅ Admin can see " + adminScorecards.size() + " scorecards");
        System.out.println("✅ Evaluator can see " + evaluatorScorecards.size() + " scorecards");
        System.out.println("✅ User can see " + userScorecards.size() + " scorecards");
        
        // Search scorecards
        List<ScorecardService.ScorecardSummary> searchResults = 
            service.searchScorecards("admin1", "Performance", null, null);
        System.out.println("✅ Search found " + searchResults.size() + " matching scorecards");
        
        // Demonstrate detailed scorecard information
        demonstrateDetailedInfo(scorecardId);
    }
    
    /**
     * Add performance review sections to scorecard
     */
    private static void addPerformanceSections(String scorecardId) {
        // Technical Skills Section
        Section technicalSection = new Section("tech_skills", "Technical Skills", 
            "Evaluation of technical competencies", 0.4);
        
        technicalSection.addQuestion(
            new Question.Builder("coding_quality", "Rate coding quality and best practices", ResponseType.RATING)
                .weight(2.0)
                .mandatory()
                .helpText("Consider code readability, efficiency, and adherence to standards")
                .build()
        );
        
        technicalSection.addQuestion(
            new Question.Builder("problem_solving", "Problem solving ability", ResponseType.RATING)
                .weight(2.0)
                .mandatory()
                .build()
        );
        
        technicalSection.addQuestion(
            new Question.Builder("learning_agility", "Learning new technologies", ResponseType.RATING)
                .weight(1.5)
                .build()
        );
        
        service.addSection(scorecardId, technicalSection, "admin1");
        
        // Communication Section
        Section commSection = new Section("communication", "Communication Skills", 
            "Evaluation of communication and collaboration", 0.3);
        
        commSection.addQuestion(
            new Question.Builder("team_collaboration", "Team collaboration effectiveness", ResponseType.RATING)
                .weight(2.0)
                .mandatory()
                .build()
        );
        
        commSection.addQuestion(
            new Question.Builder("written_comm", "Written communication clarity", ResponseType.RATING)
                .weight(1.5)
                .build()
        );
        
        commSection.addQuestion(
            new Question.Builder("presentation_skills", "Presentation and verbal skills", ResponseType.RATING)
                .weight(1.0)
                .build()
        );
        
        service.addSection(scorecardId, commSection, "admin1");
        
        // Leadership Section
        Section leadershipSection = new Section("leadership", "Leadership & Initiative", 
            "Evaluation of leadership potential and initiative", 0.3);
        
        leadershipSection.addQuestion(
            new Question.Builder("mentoring", "Mentoring and helping others", ResponseType.RATING)
                .weight(1.5)
                .build()
        );
        
        leadershipSection.addQuestion(
            new Question.Builder("initiative", "Taking initiative on projects", ResponseType.RATING)
                .weight(2.0)
                .mandatory()
                .build()
        );
        
        leadershipSection.addQuestion(
            new Question.Builder("feedback_comments", "Additional feedback and comments", ResponseType.TEXT)
                .weight(0.5)
                .minLength(10)
                .maxLength(500)
                .helpText("Provide specific examples and suggestions for improvement")
                .build()
        );
        
        service.addSection(scorecardId, leadershipSection, "admin1");
    }
    
    /**
     * Submit sample evaluations from different evaluators
     */
    private static void submitSampleEvaluations(String scorecardId) {
        // Evaluator 1 responses (more positive)
        Map<String, Map<String, Object>> eval1Responses = new HashMap<>();
        
        Map<String, Object> techResponses1 = new HashMap<>();
        techResponses1.put("coding_quality", 4);
        techResponses1.put("problem_solving", 5);
        techResponses1.put("learning_agility", 4);
        eval1Responses.put("tech_skills", techResponses1);
        
        Map<String, Object> commResponses1 = new HashMap<>();
        commResponses1.put("team_collaboration", 4);
        commResponses1.put("written_comm", 4);
        commResponses1.put("presentation_skills", 3);
        eval1Responses.put("communication", commResponses1);
        
        Map<String, Object> leadershipResponses1 = new HashMap<>();
        leadershipResponses1.put("mentoring", 3);
        leadershipResponses1.put("initiative", 4);
        leadershipResponses1.put("feedback_comments", "Strong technical skills and great team player. Could improve presentation skills.");
        eval1Responses.put("leadership", leadershipResponses1);
        
        service.submitEvaluation(scorecardId, "eval1", eval1Responses);
        
        // Evaluator 2 responses (slightly different perspective)
        Map<String, Map<String, Object>> eval2Responses = new HashMap<>();
        
        Map<String, Object> techResponses2 = new HashMap<>();
        techResponses2.put("coding_quality", 4);
        techResponses2.put("problem_solving", 4);
        techResponses2.put("learning_agility", 5);
        eval2Responses.put("tech_skills", techResponses2);
        
        Map<String, Object> commResponses2 = new HashMap<>();
        commResponses2.put("team_collaboration", 5);
        commResponses2.put("written_comm", 3);
        commResponses2.put("presentation_skills", 4);
        eval2Responses.put("communication", commResponses2);
        
        Map<String, Object> leadershipResponses2 = new HashMap<>();
        leadershipResponses2.put("mentoring", 4);
        leadershipResponses2.put("initiative", 3);
        leadershipResponses2.put("feedback_comments", "Excellent collaborator with strong learning ability. Shows good mentoring potential.");
        eval2Responses.put("leadership", leadershipResponses2);
        
        service.submitEvaluation(scorecardId, "eval2", eval2Responses);
    }
    
    /**
     * Demonstrate detailed scorecard information
     */
    private static void demonstrateDetailedInfo(String scorecardId) {
        System.out.println("\n📊 Detailed Scorecard Analysis");
        System.out.println("==============================");
        
        Scorecard scorecard = service.getScorecard(scorecardId, "admin1");
        if (scorecard == null) return;
        
        System.out.println("Scorecard: " + scorecard.getTitle());
        System.out.println("State: " + scorecard.getState());
        System.out.println("Sections: " + scorecard.getSections().size());
        System.out.println("Evaluators: " + scorecard.getAssignedEvaluators().size());
        
        // Show section details
        for (Section section : scorecard.getSections()) {
            System.out.println("\n📝 Section: " + section.getTitle());
            System.out.println("  Weight: " + section.getWeight());
            System.out.println("  Questions: " + section.getQuestions().size());
            
            for (Question question : section.getQuestions()) {
                System.out.println("    ❓ " + question.getText() + 
                                 " (" + question.getResponseType() + ", weight: " + question.getWeight() + ")");
            }
        }
        
        // Show evaluation summary
        ScorecardService.ScorecardReport report = service.generateReport(scorecardId, "admin1");
        if (report != null) {
            System.out.println("\n📈 Evaluation Summary");
            System.out.println("Average Score: " + String.format("%.2f", report.getAverageScore()));
            
            for (Map.Entry<String, Scorecard.ScorecardScore> entry : report.getEvaluatorScores().entrySet()) {
                System.out.println("Evaluator " + entry.getKey() + ": " + 
                                 String.format("%.2f", entry.getValue().getOverallScore()));
            }
        }
    }
}
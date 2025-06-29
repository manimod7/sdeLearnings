package lld.taskmanagement;

import java.time.LocalDateTime;

public class TaskManagementDemo {
    public static void main(String[] args) {
        System.out.println("=== Task Management System Demo ===\n");

        // Initialize the task management system
        TaskManagementSystem tms = new TaskManagementSystem();

        // Add observers
        tms.addObserver(new EmailNotificationObserver("EmailService"));
        tms.addObserver(new SlackNotificationObserver("dev-team"));

        System.out.println("1. Creating tasks...\n");

        // Create tasks
        Task task1 = tms.createTask(
            "Implement user authentication",
            "Add JWT-based authentication to the API",
            TaskPriority.HIGH,
            "john.doe"
        );

        Task task2 = tms.createTask(
            "Fix login bug",
            "Users are unable to login with special characters in password",
            TaskPriority.CRITICAL,
            "jane.smith"
        );

        Task task3 = tms.createTask(
            "Update documentation",
            "Update API documentation for new endpoints",
            TaskPriority.LOW,
            "bob.wilson"
        );

        System.out.println("\n2. Updating task statuses...\n");

        // Update task statuses
        tms.updateTaskStatus(task1.getId(), TaskStatus.IN_PROGRESS);
        tms.updateTaskStatus(task2.getId(), TaskStatus.IN_REVIEW);
        tms.updateTaskStatus(task3.getId(), TaskStatus.DONE);

        System.out.println("\n3. Reassigning tasks...\n");

        // Reassign tasks
        tms.assignTask(task1.getId(), "alice.brown");

        System.out.println("\n4. Updating priorities...\n");

        // Update priorities
        tms.updateTaskPriority(task3.getId(), TaskPriority.MEDIUM);

        System.out.println("\n5. Setting due dates...\n");

        // Set due dates
        task1.setDueDate(LocalDateTime.now().plusDays(3));
        task2.setDueDate(LocalDateTime.now().minusDays(1)); // Overdue task

        System.out.println("\n6. Generating reports...\n");

        // Generate reports
        System.out.println("=== Task Status Report ===");
        tms.getTaskStatusReport().forEach((status, count) -> 
            System.out.println(status + ": " + count + " tasks"));

        System.out.println("\n=== Task Priority Report ===");
        tms.getTaskPriorityReport().forEach((priority, count) -> 
            System.out.println(priority + ": " + count + " tasks"));

        System.out.println("\n=== Tasks by Assignee ===");
        System.out.println("Alice's tasks: " + tms.getTasksByAssignee("alice.brown").size());
        System.out.println("Jane's tasks: " + tms.getTasksByAssignee("jane.smith").size());
        System.out.println("Bob's tasks: " + tms.getTasksByAssignee("bob.wilson").size());

        System.out.println("\n=== Overdue Tasks ===");
        tms.getOverdueTasks().forEach(task -> 
            System.out.println("OVERDUE: " + task.getTitle() + " (Due: " + task.getDueDate() + ")"));

        System.out.println("\n=== Demo completed successfully! ===");
    }
}
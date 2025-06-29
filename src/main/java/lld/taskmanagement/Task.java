package lld.taskmanagement;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public Task(String title, String description, TaskPriority priority, String assigneeId) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.priority = priority;
        this.assigneeId = assigneeId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(TaskStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignTo(String newAssigneeId) {
        this.assigneeId = newAssigneeId;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePriority(TaskPriority newPriority) {
        this.priority = newPriority;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }
    public String getAssigneeId() { return assigneeId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getDueDate() { return dueDate; }

    @Override
    public String toString() {
        return String.format("Task{id='%s', title='%s', status=%s, priority=%s, assignee='%s'}",
                id, title, status, priority, assigneeId);
    }
}
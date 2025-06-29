package lld.taskmanagement;

public class EmailNotificationObserver implements TaskObserver {
    private String observerName;

    public EmailNotificationObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void onTaskCreated(Task task) {
        System.out.println(String.format("[EMAIL] %s: New task created - %s assigned to %s",
                observerName, task.getTitle(), task.getAssigneeId()));
    }

    @Override
    public void onTaskStatusChanged(Task task, TaskStatus oldStatus, TaskStatus newStatus) {
        System.out.println(String.format("[EMAIL] %s: Task '%s' status changed from %s to %s",
                observerName, task.getTitle(), oldStatus, newStatus));
    }

    @Override
    public void onTaskAssigned(Task task, String oldAssigneeId, String newAssigneeId) {
        System.out.println(String.format("[EMAIL] %s: Task '%s' reassigned from %s to %s",
                observerName, task.getTitle(), oldAssigneeId, newAssigneeId));
    }

    @Override
    public void onTaskPriorityChanged(Task task, TaskPriority oldPriority, TaskPriority newPriority) {
        System.out.println(String.format("[EMAIL] %s: Task '%s' priority changed from %s to %s",
                observerName, task.getTitle(), oldPriority, newPriority));
    }
}
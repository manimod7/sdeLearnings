package lld.taskmanagement;

public interface TaskObserver {
    void onTaskCreated(Task task);
    void onTaskStatusChanged(Task task, TaskStatus oldStatus, TaskStatus newStatus);
    void onTaskAssigned(Task task, String oldAssigneeId, String newAssigneeId);
    void onTaskPriorityChanged(Task task, TaskPriority oldPriority, TaskPriority newPriority);
}
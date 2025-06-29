package lld.taskmanagement;

public class SlackNotificationObserver implements TaskObserver {
    private String channelName;

    public SlackNotificationObserver(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void onTaskCreated(Task task) {
        System.out.println(String.format("[SLACK] #%s: 🆕 New task: %s (Priority: %s) assigned to @%s",
                channelName, task.getTitle(), task.getPriority(), task.getAssigneeId()));
    }

    @Override
    public void onTaskStatusChanged(Task task, TaskStatus oldStatus, TaskStatus newStatus) {
        String emoji = getStatusEmoji(newStatus);
        System.out.println(String.format("[SLACK] #%s: %s Task '%s' moved to %s",
                channelName, emoji, task.getTitle(), newStatus));
    }

    @Override
    public void onTaskAssigned(Task task, String oldAssigneeId, String newAssigneeId) {
        System.out.println(String.format("[SLACK] #%s: 👤 Task '%s' reassigned: @%s → @%s",
                channelName, task.getTitle(), oldAssigneeId, newAssigneeId));
    }

    @Override
    public void onTaskPriorityChanged(Task task, TaskPriority oldPriority, TaskPriority newPriority) {
        String emoji = getPriorityEmoji(newPriority);
        System.out.println(String.format("[SLACK] #%s: %s Task '%s' priority: %s → %s",
                channelName, emoji, task.getTitle(), oldPriority, newPriority));
    }

    private String getStatusEmoji(TaskStatus status) {
        switch (status) {
            case TODO: return "📝";
            case IN_PROGRESS: return "⚡";
            case IN_REVIEW: return "👀";
            case DONE: return "✅";
            case BLOCKED: return "🚫";
            default: return "📋";
        }
    }

    private String getPriorityEmoji(TaskPriority priority) {
        switch (priority) {
            case CRITICAL: return "🔴";
            case HIGH: return "🟠";
            case MEDIUM: return "🟡";
            case LOW: return "🟢";
            default: return "⚪";
        }
    }
}
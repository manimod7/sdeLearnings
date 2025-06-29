package lld.taskmanagement;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskManagementSystem {
    private Map<String, Task> tasks;
    private List<TaskObserver> observers;

    public TaskManagementSystem() {
        this.tasks = new ConcurrentHashMap<>();
        this.observers = new ArrayList<>();
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    public Task createTask(String title, String description, TaskPriority priority, String assigneeId) {
        Task task = new Task(title, description, priority, assigneeId);
        tasks.put(task.getId(), task);
        
        // Notify observers
        for (TaskObserver observer : observers) {
            observer.onTaskCreated(task);
        }
        
        return task;
    }

    public boolean updateTaskStatus(String taskId, TaskStatus newStatus) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return false;
        }

        TaskStatus oldStatus = task.getStatus();
        task.updateStatus(newStatus);
        
        // Notify observers
        for (TaskObserver observer : observers) {
            observer.onTaskStatusChanged(task, oldStatus, newStatus);
        }
        
        return true;
    }

    public boolean assignTask(String taskId, String newAssigneeId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return false;
        }

        String oldAssigneeId = task.getAssigneeId();
        task.assignTo(newAssigneeId);
        
        // Notify observers
        for (TaskObserver observer : observers) {
            observer.onTaskAssigned(task, oldAssigneeId, newAssigneeId);
        }
        
        return true;
    }

    public boolean updateTaskPriority(String taskId, TaskPriority newPriority) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return false;
        }

        TaskPriority oldPriority = task.getPriority();
        task.updatePriority(newPriority);
        
        // Notify observers
        for (TaskObserver observer : observers) {
            observer.onTaskPriorityChanged(task, oldPriority, newPriority);
        }
        
        return true;
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }

    public List<Task> getTasksByAssignee(String assigneeId) {
        return tasks.values().stream()
                .filter(task -> assigneeId.equals(task.getAssigneeId()))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return tasks.values().stream()
                .filter(task -> status.equals(task.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(TaskPriority priority) {
        return tasks.values().stream()
                .filter(task -> priority.equals(task.getPriority()))
                .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        return tasks.values().stream()
                .filter(task -> task.getDueDate() != null && 
                              task.getDueDate().isBefore(now) && 
                              task.getStatus() != TaskStatus.DONE)
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Map<TaskStatus, Long> getTaskStatusReport() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

    public Map<TaskPriority, Long> getTaskPriorityReport() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
    }
}
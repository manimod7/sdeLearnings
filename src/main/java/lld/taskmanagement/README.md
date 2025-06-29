# Task Management System (Jira-like) - Low Level Design

## 🎯 Problem Statement
Design a task management system similar to Jira/Atlassian where users can create, assign, and track tasks through different states. The system should support real-time notifications and provide comprehensive task management capabilities.

## 📋 Requirements

### Functional Requirements
- ✅ Create tasks with title, description, priority, and assignee
- 📝 Update task status (TODO, IN_PROGRESS, IN_REVIEW, DONE, BLOCKED)
- 👤 Assign/reassign tasks to different users
- 🔄 Change task priorities dynamically
- 📊 Generate reports on task status and priority distribution
- 🔍 Filter tasks by assignee, status, priority
- ⏰ Set and track due dates
- 📈 Track task creation and modification timestamps

### Non-Functional Requirements
- 🔔 Real-time notifications for task changes
- ⚡ Low-latency task operations
- 🧵 Thread-safe for concurrent access
- 📊 Support for thousands of tasks
- 🔧 Extensible notification system
- 💾 Memory-efficient data structures

## 🏗️ Architecture & Design Patterns

### Primary Design Patterns

#### 1. Observer Pattern
- **Purpose**: Enable real-time notifications when tasks change
- **Implementation**: `TaskObserver` interface with concrete observers
- **Benefits**: 
  - Loose coupling between task management and notifications
  - Easy to add new notification channels
  - Real-time updates to multiple subscribers

#### 2. Factory Pattern
- **Purpose**: Create different types of observers based on configuration
- **Benefits**: Centralized object creation, easy to extend

#### 3. Builder Pattern
- **Purpose**: Flexible task creation with optional parameters
- **Benefits**: Readable task creation, immutable objects

## 🎨 UML Class Diagram

```
                    ┌─────────────────────────┐
                    │   TaskManagementSystem  │
                    ├─────────────────────────┤
                    │ - tasks: Map<String,Task>│
                    │ - observers: List       │
                    ├─────────────────────────┤
                    │ + createTask()          │
                    │ + updateTaskStatus()    │
                    │ + assignTask()          │
                    │ + addObserver()         │
                    │ + getTasksByStatus()    │
                    │ + getTasksByAssignee()  │
                    └─────────────────────────┘
                              │
                              │ manages
                              ▼
                    ┌─────────────────────────┐
                    │         Task            │
                    ├─────────────────────────┤
                    │ - id: String            │
                    │ - title: String         │
                    │ - description: String   │
                    │ - status: TaskStatus    │
                    │ - priority: TaskPriority│
                    │ - assigneeId: String    │
                    │ - createdAt: DateTime   │
                    │ - updatedAt: DateTime   │
                    │ - dueDate: DateTime     │
                    ├─────────────────────────┤
                    │ + updateStatus()        │
                    │ + assignTo()            │
                    │ + updatePriority()      │
                    │ + setDueDate()          │
                    └─────────────────────────┘
                              │
                              │ uses
                              ▼
                    ┌─────────────────────────┐
                    │      TaskObserver       │
                    ├─────────────────────────┤
                    │ + onTaskCreated()       │
                    │ + onTaskStatusChanged() │
                    │ + onTaskAssigned()      │
                    │ + onTaskPriorityChanged()│
                    └─────────────────────────┘
                              △
                              │
                    ┌─────────┴─────────┐
                    │                   │
        ┌─────────────────────┐ ┌─────────────────────┐
        │EmailNotification    │ │SlackNotification    │
        │Observer             │ │Observer             │
        ├─────────────────────┤ ├─────────────────────┤
        │ - observerName      │ │ - channelName       │
        ├─────────────────────┤ ├─────────────────────┤
        │ + onTaskCreated()   │ │ + onTaskCreated()   │
        │ + onTaskStatus...() │ │ + onTaskStatus...() │
        └─────────────────────┘ └─────────────────────┘
```

## 📊 Time & Space Complexity Analysis

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|--------|
| Create Task | O(1) | O(1) | HashMap insertion |
| Update Task Status | O(1) + O(n) | O(1) | O(n) for notifying observers |
| Assign Task | O(1) + O(n) | O(1) | O(n) for observer notification |
| Get Tasks by Status | O(t) | O(k) | t=total tasks, k=matching tasks |
| Get Tasks by Assignee | O(t) | O(k) | Linear scan through all tasks |
| Get Overdue Tasks | O(t) | O(k) | Check due date for all tasks |
| Generate Reports | O(t) | O(1) | Single pass aggregation |

**Overall Space Complexity**: O(t + o) where t = number of tasks, o = number of observers

## 🔄 System Flow

```
User Action
    │
    ▼
┌─────────────────┐
│ Task Management │
│ System          │
└─────────────────┘
    │
    ▼
┌─────────────────┐     ┌─────────────────┐
│ Update Task     │────▶│ Notify All      │
│ State           │     │ Observers       │
└─────────────────┘     └─────────────────┘
    │                           │
    ▼                           ▼
┌─────────────────┐     ┌─────────────────┐
│ Update Database │     │ Send            │
│ / Memory        │     │ Notifications   │
└─────────────────┘     └─────────────────┘
                               │
                               ▼
                    ┌─────────────────┐
                    │ Email/Slack/    │
                    │ Other Channels  │
                    └─────────────────┘
```

## 🏁 Workflow States

```
    TODO
      │
      ▼
┌─────────────┐     ┌─────────────┐
│ IN_PROGRESS │────▶│ IN_REVIEW   │
└─────────────┘     └─────────────┘
      │                     │
      ▼                     ▼
┌─────────────┐     ┌─────────────┐
│ BLOCKED     │     │ DONE        │
└─────────────┘     └─────────────┘
      │                     
      ▼                     
┌─────────────┐             
│ IN_PROGRESS │             
└─────────────┘             
```

## ⚖️ Pros and Cons

### Pros
✅ **Loose Coupling**: Observer pattern enables independent notification systems  
✅ **Extensibility**: Easy to add new task fields and notification channels  
✅ **Real-time Updates**: Immediate notifications for task changes  
✅ **Type Safety**: Enums for status and priority prevent invalid states  
✅ **Thread Safety**: Concurrent collections for multi-user environments  
✅ **Memory Efficient**: HashMap-based storage with O(1) lookups  
✅ **Comprehensive Filtering**: Multiple filter options for task queries  

### Cons
❌ **Memory Usage**: Stores all tasks in memory (could use database)  
❌ **Observer Overhead**: Notification latency increases with observer count  
❌ **Single Point of Failure**: In-memory storage loses data on restart  
❌ **Limited Query Capabilities**: No complex queries like SQL  
❌ **No Persistence**: No built-in data persistence mechanism  

## 🚀 Scalability & Extensibility

### Horizontal Scaling
- **Database Integration**: Replace in-memory storage with database
- **Message Queues**: Use async messaging for notifications (RabbitMQ, Kafka)
- **Microservices**: Split into task-service and notification-service
- **Load Balancing**: Distribute requests across multiple instances

### Vertical Scaling
- **Memory Optimization**: LRU cache for frequently accessed tasks
- **Database Indexing**: Index on assigneeId, status, priority, dueDate
- **Connection Pooling**: Efficient database connection management
- **Caching Layer**: Redis for frequently accessed data

### Extensibility Features
- **Custom Task Fields**: Easy to add new task attributes
- **Workflow Engine**: Implement custom state transition rules
- **Plugin Architecture**: Support for custom observers and validators
- **API Gateway**: RESTful API for external integrations
- **Event Sourcing**: Track complete task history
- **Batch Operations**: Support for bulk task operations

## 🧪 Edge Cases Handled

1. **Null Safety**: All inputs validated for null values
2. **Concurrent Modifications**: Thread-safe operations using concurrent collections
3. **Invalid State Transitions**: Validation for allowed status changes
4. **Duplicate Task IDs**: UUID generation prevents collisions
5. **Observer Failures**: Graceful handling of notification failures
6. **Memory Pressure**: Could implement LRU eviction for task cache
7. **Date Validation**: Ensure due dates are in the future
8. **Empty Collections**: Safe handling of empty filter results

## 🏭 Production Considerations

### Monitoring & Observability
- **Metrics**: Task creation rate, status change frequency, notification latency
- **Logging**: Comprehensive logging for debugging and audit trails
- **Health Checks**: System health endpoints for load balancers
- **Performance Monitoring**: Track response times and throughput

### Security
- **Authentication**: User authentication before task operations
- **Authorization**: Role-based access control for task operations
- **Input Validation**: Sanitize all user inputs
- **Rate Limiting**: Prevent abuse of task creation APIs

### Data Management
- **Backup Strategy**: Regular backups of task data
- **Data Retention**: Policies for archiving old completed tasks
- **Migration Support**: Schema migration for system updates
- **Disaster Recovery**: Failover and recovery procedures

## 🧰 How to Run

### Prerequisites
```bash
Java 11+
Maven 3.6+
```

### Run Demo
```bash
# Compile the project
mvn compile

# Run the demo
java -cp target/classes lld.taskmanagement.TaskManagementDemo
```

### Sample Output
```
=== Task Management System Demo ===

1. Creating tasks...

[EMAIL] EmailService: New task created - Implement user authentication assigned to john.doe
[SLACK] #dev-team: 🆕 New task: Implement user authentication (Priority: High) assigned to @john.doe
[EMAIL] EmailService: New task created - Fix login bug assigned to jane.smith
[SLACK] #dev-team: 🆕 New task: Fix login bug (Priority: Critical) assigned to @jane.smith

2. Updating task statuses...

[EMAIL] EmailService: Task 'Implement user authentication' status changed from To Do to In Progress
[SLACK] #dev-team: ⚡ Task 'Implement user authentication' moved to In Progress
```

## 🔮 Future Enhancements

1. **Advanced Workflows**: Custom workflow definitions with approval steps
2. **Time Tracking**: Track time spent on tasks
3. **Dependencies**: Task dependency management
4. **Comments & Attachments**: Rich task documentation
5. **Advanced Analytics**: Burndown charts, velocity tracking
6. **Mobile API**: RESTful API for mobile applications
7. **Integration Hub**: Webhooks for third-party integrations
8. **AI-Powered Features**: Smart task assignment, deadline prediction

## 📚 Related Design Patterns

- **Chain of Responsibility**: For task approval workflows
- **Command Pattern**: For undo/redo task operations
- **Memento Pattern**: For task history and versioning
- **Proxy Pattern**: For lazy loading of task details
- **Decorator Pattern**: For adding features to existing tasks
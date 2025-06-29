package lld.taskmanagement;

public enum TaskPriority {
    CRITICAL(4, "Critical"),
    HIGH(3, "High"),
    MEDIUM(2, "Medium"),
    LOW(1, "Low");

    private final int level;
    private final String displayName;

    TaskPriority(int level, String displayName) {
        this.level = level;
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
package project;

public class RecurringTask extends Task {
    private String recurrencePattern;

    public RecurringTask(String title, String description, String dueDate, int priority, String recurrencePattern) {
        super(title, description, dueDate, priority);
        this.recurrencePattern = recurrencePattern;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
}

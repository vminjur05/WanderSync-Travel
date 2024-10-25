package project;

public class HighPriorityTask extends Task {
    public HighPriorityTask(String title, String description, String dueDate) {
        super(title, description, dueDate, 1); // Setting priority as 1 for high priority.
    }
}

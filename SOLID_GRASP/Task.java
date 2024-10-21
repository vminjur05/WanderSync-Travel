public class Task implements ITask {
    private String title;
    private String description;
    private String dueDate;
    private String status;
    private int priority;

    public Task(String title, String description, String dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = "Pending";
    }

    @Override
    public void updateStatus(String status) {
        this.status = status;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isCompleted() {
        return "Completed".equalsIgnoreCase(status);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDueDate() {
        return dueDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}

public interface ITask {
    void updateStatus(String status);
    void setPriority(int priority);
    boolean isCompleted();
    String getTitle();
    String getDescription();
    String getDueDate();
    String getStatus();
    int getPriority();
}

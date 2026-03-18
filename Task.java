public class Task {
    private String title;
    private String deadline;
    private int priority;
    private boolean isCompleted;

    public Task(String title, String deadline, int priority) {
        this.title = title;
        this.deadline = deadline;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getTitle() { return title; }
    
    @Override
    public String toString() {
        String status = isCompleted ? "[DONE]" : "[PENDING]";
        return status + " " + title + " (Due: " + deadline + ") Priority: " + priority;
    }
}

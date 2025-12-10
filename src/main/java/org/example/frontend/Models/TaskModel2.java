package org.example.frontend.Models;

public class TaskModel2 {
    private Integer taskId;
    private String title;
    private String description;
    private String deadline;   // вместо LocalDate
    private String status;
    private String createdAt;  // вместо LocalDateTime

    public TaskModel2() {}

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return title + " | " + status + " | " + deadline;
    }
}




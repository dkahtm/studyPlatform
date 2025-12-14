package com.study.studyplatform.JavaFX.Config;

import java.time.LocalDateTime;

public class NotificationMessage {
    private String title;
    private String body;
    private LocalDateTime timestamp;
    private Integer taskId; // опционально
    private Integer groupId; // опционально

    public NotificationMessage() {}

    public NotificationMessage(String title, String body, LocalDateTime timestamp) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    // геттеры/сеттеры
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }
    public Integer getGroupId() { return groupId; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }
}

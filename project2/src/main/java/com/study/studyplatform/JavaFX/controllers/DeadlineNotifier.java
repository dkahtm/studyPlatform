package com.study.studyplatform.JavaFX.controllers;
import com.study.studyplatform.JavaFX.Config.NotificationMessage;
import com.study.studyplatform.JavaFX.Config.NotificationWebSocketHandler;
import com.study.studyplatform.JavaFX.entity.Tasks;
import com.study.studyplatform.JavaFX.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeadlineNotifier {

    private final TaskRepository taskRepository;
    private final NotificationWebSocketHandler wsHandler;

    public DeadlineNotifier(TaskRepository taskRepository, NotificationWebSocketHandler wsHandler) {
        this.taskRepository = taskRepository;
        this.wsHandler = wsHandler;
    }

    // каждые 15 минут проверяем — настрой по вкусу
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void checkAndNotify() {
        List<Tasks> tasks = taskRepository.findTasksWithDeadlineInTwoDays();
        for (Tasks t : tasks) {
            NotificationMessage msg = new NotificationMessage();
            msg.setTitle("Deadline is soon");
            msg.setBody("Task \"" + t.getTitle() + "\" — in 2 days (" + t.getDeadline() + ")");
            msg.setTimestamp(LocalDateTime.now());
            msg.setTaskId(t.getTaskId());
            msg.setGroupId(t.getGroupId());

            wsHandler.sendNotification(msg);
        }
    }
}

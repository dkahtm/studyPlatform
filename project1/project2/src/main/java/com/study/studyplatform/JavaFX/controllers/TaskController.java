package com.study.studyplatform.JavaFX.controllers;

import com.study.studyplatform.JavaFX.entity.Group;
import com.study.studyplatform.JavaFX.entity.Tasks;
import com.study.studyplatform.JavaFX.entity.User;
import com.study.studyplatform.JavaFX.repository.GroupRepository;
import com.study.studyplatform.JavaFX.repository.TaskRepository;
import com.study.studyplatform.JavaFX.repository.UserRepository;

import lombok.Data;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final GroupRepository groupRepository;


    public TaskController(TaskRepository taskRepository,
                          GroupRepository groupRepository) {
        this.taskRepository = taskRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping("/deadline2days")
    public List<Tasks> getTasksDeadlineInTwoDays() {
        return taskRepository.findTasksWithDeadlineInTwoDays();
    }

    @GetMapping("/group/{groupId}")
    public List<TaskModel> getTasksByGroup(@PathVariable Integer groupId) {
        List<Tasks> tasks = taskRepository.findByGroup_Id(groupId);

        return tasks.stream().map(task -> {
            TaskModel model = new TaskModel();
            model.setTaskId(task.getTaskId());
            model.setTitle(task.getTitle());
            model.setDescription(task.getDescription());
            model.setDeadline(task.getDeadline());
            model.setStatus(task.getStatus());
            model.setCreatedAt(task.getCreatedAt());
            return model;
        }).toList();
    }




    // ---------------------------------------------------------
    // 2️⃣ Создать таск в группе
    // ---------------------------------------------------------
    @Data
    public static class TaskModel {
        private Integer taskId;
        private String title;
        private String description;
        private LocalDate deadline;
        private Tasks.Status status;
        private LocalDateTime createdAt;
    }

    @PostMapping("/group/{groupId}")
    public boolean createTask(
            @PathVariable Integer groupId,
            @RequestBody TaskModel request) {


        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));


        Tasks task = new Tasks();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setStatus(request.getStatus());
        task.setGroup(group);
        task.setCreatedAt(LocalDateTime.now()); // Добавляем дату создания

        taskRepository.save(task);

        return true;
    }




    // ---------------------------------------------------------
    // 3️⃣ Обновить таск
    // ---------------------------------------------------------
    @PostMapping("/update/{taskId}")
    public Tasks updateTask(@PathVariable Integer taskId,
                           @RequestBody Tasks updatedTask) {

        Tasks existing = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        existing.setDeadline(updatedTask.getDeadline());

        return taskRepository.save(existing);
    }

    // ---------------------------------------------------------
    // 4️⃣ Удалить таск
    // ---------------------------------------------------------
    @PostMapping("/delete/{taskId}")
    public boolean deleteTask(@PathVariable Integer taskId) {
        taskRepository.deleteById(taskId);
        return true;
    }
}


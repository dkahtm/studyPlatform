package com.study.studyplatform.JavaFX.controllers;

import com.study.studyplatform.JavaFX.entity.Tasks;
import com.study.studyplatform.JavaFX.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final TaskRepository taskRepository;

    public StatsController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/statuses")
    public Map<String, Long> getStatusStats(@RequestParam Integer groupId) {

        Map<String, Long> stats = new HashMap<>();
        stats.put("OPEN", taskRepository.countByStatusAndGroup_Id(Tasks.Status.OPEN, groupId));
        stats.put("IN_PROGRESS", taskRepository.countByStatusAndGroup_Id(Tasks.Status.IN_PROGRESS, groupId));
        stats.put("DONE", taskRepository.countByStatusAndGroup_Id(Tasks.Status.DONE, groupId));

        return stats;
    }
}


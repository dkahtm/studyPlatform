package com.study.studyplatform.JavaFX.controllers;

import com.study.studyplatform.JavaFX.entity.Group;
import com.study.studyplatform.JavaFX.entity.User;
import com.study.studyplatform.JavaFX.repository.GroupRepository;
import com.study.studyplatform.JavaFX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/home")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public boolean createGroup(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam String createdByName) {


        // Перевірка на існуючу групу
        if (groupRepository.findByName(name) != null) {
            return false;
        }



        // Створення групи
        Group group = new Group();
        group.setName(name);
        group.setDescription(description != null ? description : "");
        group.setCreatedBy(createdByName);

        groupRepository.save(group);



        return true;
    }

    @PostMapping("/delete/{groupId}")
    public boolean deleteGroup(@PathVariable("groupId") Integer groupId) {
        if (!groupRepository.existsById(groupId)) {
            return false;
        }
        groupRepository.deleteById(groupId);
        return true;
    }




    @GetMapping(path="/all")
    public @ResponseBody Iterable<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // 2. Получить всех пользователей в конкретной группе
    @GetMapping("/group/{groupId}/students")
    public ResponseEntity<List<User>> getStudentsByGroup(@PathVariable Integer groupId) {
        List<User> students = userRepository.findByGroupId(groupId);
        return ResponseEntity.ok(students);
    }

    // 3. Добавить существующего студента в группу
    @PostMapping("/group/{groupId}/add-student")
    public ResponseEntity<Map<String, String>> addStudentToGroup(
            @PathVariable Integer groupId,
            @RequestParam Integer studentId) {

        Map<String, String> response = new HashMap<>();

        try {
            // Находим группу
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("group not found"));

            // Находим студента
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("student not found"));

            // Проверяем, не состоит ли уже в другой группе
            if (student.getGroup() != null) {
                response.put("status", "error");
                response.put("message", "student already in group " + student.getGroup().getName());
                return ResponseEntity.badRequest().body(response);
            }

            // Добавляем студента в группу
            student.setGroup(group);
            userRepository.save(student);

            response.put("status", "success");
            response.put("message", "student " + student.getName() + " have added " + group.getName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/group/{groupId}/remove-student")
    public ResponseEntity<Map<String, String>> removeStudentFromGroup(
            @PathVariable Integer groupId,
            @RequestParam Integer studentId) {

        Map<String, String> response = new HashMap<>();

        try {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("student not found"));

            // Проверяем, что студент действительно в этой группе
            if (student.getGroup() == null || !student.getGroup().getId().equals(groupId)) {
                response.put("status", "error");
                response.put("message", "student not in group ");
                return ResponseEntity.badRequest().body(response);
            }

            // Удаляем из группы (устанавливаем null)
            student.setGroup(null);
            userRepository.save(student);

            response.put("status", "success");
            response.put("message", "student deleted ");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }




}

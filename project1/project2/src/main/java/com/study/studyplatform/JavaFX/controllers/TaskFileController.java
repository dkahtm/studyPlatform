package com.study.studyplatform.JavaFX.controllers;

import com.study.studyplatform.JavaFX.entity.TaskFile;
import com.study.studyplatform.JavaFX.repository.TaskFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/files")
public class TaskFileController {

    private final String UPLOAD_DIR = "uploads"; // Папка для хранения файлов

    @Autowired
    private TaskFileRepository repository;

    // Загрузка файла
    @PostMapping("/upload")
    public TaskFile uploadFile(@RequestParam("file") MultipartFile file,
                               @RequestParam("taskId") Long taskId) throws IOException {
        // Создаем папку если не существует
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Сохраняем файл на диск
        String filePath = UPLOAD_DIR + "/" + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        // Сохраняем информацию о файле в БД
        TaskFile taskFile = new TaskFile(
                file.getOriginalFilename(),
                filePath,
                file.getContentType(),
                file.getSize(),
                taskId
        );
        return repository.save(taskFile);
    }

    // Получение списка файлов по taskId
    @GetMapping("/{taskId}")
    public List<TaskFile> getFiles(@PathVariable Long taskId) {
        return repository.findByTaskId(taskId);
    }

    // Скачивание/открытие файла
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) throws IOException {
        TaskFile taskFile = repository.findById(fileId).orElseThrow();
        Path path = Paths.get(taskFile.getFilePath());
        byte[] fileBytes = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + taskFile.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(taskFile.getFileType()))
                .body(fileBytes);
    }
}


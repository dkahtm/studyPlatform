package org.example.frontend.Services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.frontend.Models.TaskFile;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.List;

public class FileService {

    private final String BASE_URL = "http://localhost:8080/files"; // адрес Spring Boot
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public FileService() {
    }


    // Получение списка файлов
    public List<TaskFile> getFiles(Long taskId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + taskId))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<TaskFile>>() {});
    }

    // Загрузка файла
    public TaskFile uploadFile(File file, Long taskId) throws IOException, InterruptedException {
        // boundary для multipart/form-data
        String boundary = "Boundary-" + System.currentTimeMillis();
        String LINE_FEED = "\r\n";

        // Создаем multipart тело запроса
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream), true);

        // taskId поле
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"taskId\"").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(String.valueOf(taskId)).append(LINE_FEED);

        // файл
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(Files.probeContentType(file.toPath())).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
        Files.copy(file.toPath(), outputStream);
        outputStream.flush();
        writer.append(LINE_FEED);
        writer.append("--").append(boundary).append("--").append(LINE_FEED);
        writer.close();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/upload"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(outputStream.toByteArray()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), TaskFile.class);
    }

    // Скачивание файла
    public void downloadFile(TaskFile taskFile, File targetFile) throws IOException, InterruptedException {
        if (taskFile.getId() == null) {
            throw new IllegalArgumentException("TaskFile ID is null");
        }

        // Формируем URL на бекенд
        String url = BASE_URL + "/download/" + taskFile.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Отправляем запрос и получаем байты
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // Проверка ответа сервера
        if (response.statusCode() != 200) {
            throw new IOException("Failed to download file: " + response.statusCode());
        }

        String userHome = System.getProperty("user.home");
        String downloadDir = userHome + "/Downloads/";
        File tempFile = new File(downloadDir + taskFile.getFileName());

        // Записываем байты в файл
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(response.body());
        }

        System.out.println("File downloaded successfully: " + tempFile.getAbsolutePath());
    }

}

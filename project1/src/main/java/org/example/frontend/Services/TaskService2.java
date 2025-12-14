package org.example.frontend.Services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.frontend.Models.TaskModel2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskService2 {

    // Правильный URL для получения задач по группе
    private static final String BASE_URL = "http://localhost:8080/tasks";

    public String getTasksByGroupJson(Integer groupId) {
        try {
            // Формируем URL к endpoint
            URL url = new URL(BASE_URL + "/group/" + groupId);
            System.out.println("Requesting tasks from: " + url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Читаем JSON как строку
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            System.out.println("Response code: " + conn.getResponseCode());
            System.out.println("JSON tasks for group " + groupId + ": " + json);

            return json.toString();  // возвращаем JSON строкой

        } catch (Exception e) {
            e.printStackTrace();
            return "[]"; // пустой массив в JSON формате, если ошибка
        }
    }

}



package org.example.frontend.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.frontend.Models.TaskModel;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private static final String SERVER_URL = "http://localhost:8080";
    private static final Gson gson = new Gson();

    // ---------------------------------------------------------
    // 1️⃣ Получить все таски группы
    // ---------------------------------------------------------
    public static List<TaskModel> getTasksByGroup(int groupId) {
        System.out.println("\n=== GET TASKS FOR GROUP " + groupId + " ===");

        try {
            URL url = new URL(SERVER_URL + "/tasks/group/" + groupId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");


            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != 200) {
                System.err.println("HTTP Error: " + responseCode);
                return new ArrayList<>(); // ВОЗВРАЩАЕМ ПУСТОЙ СПИСОК
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            String json = response.toString();
            System.out.println("JSON response: " + json);

            // ВСЕГДА возвращаем список, даже если пустой
            if (json == null || json.trim().isEmpty() || json.equals("[]")) {
                System.out.println("Empty response - returning empty list");
                return new ArrayList<>();
            }

            Type listType = new TypeToken<List<TaskModel>>(){}.getType();
            List<TaskModel> tasks = gson.fromJson(json, listType);

            System.out.println("Parsed " + (tasks != null ? tasks.size() : 0) + " tasks");

            // ВОЗВРАЩАЕМ ПУСТОЙ СПИСОК если null
            return tasks != null ? tasks : new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Exception in getTasksByGroup: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // ВОЗВРАЩАЕМ ПУСТОЙ СПИСОК
        }
    }

    // ---------------------------------------------------------
    // 2️⃣ Создать таск (POST) - ПРОСТОЙ ВАРИАНТ
    // ---------------------------------------------------------
    public static boolean createTask(int groupId, TaskModel task) {
        HttpURLConnection con = null;

        try {
            System.out.println("=== SENDING CREATE TASK REQUEST ===");
            System.out.println("Group ID: " + groupId);
            System.out.println("Task Title: " + task.getTitle());
            System.out.println("Task Status: " + task.getStatus());

            // ПРОСТОЙ ВАРИАНТ: создаем JSON вручную
            String json = createTaskJson(task);
            System.out.println("JSON being sent: " + json);

            URL url = new URL(SERVER_URL + "/tasks/group/" + groupId);
            System.out.println("URL: " + url);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {

                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    String serverResponse = response.toString();
                    System.out.println("Server response: " + serverResponse);
                    System.out.println("=== REQUEST SUCCESSFUL ===");

                    return Boolean.parseBoolean(serverResponse);
                }
            } else {
                System.err.println("HTTP ERROR: " + responseCode);
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.err.println("Server error: " + errorResponse.toString());
                }
                System.out.println("=== REQUEST FAILED ===");
                return false;
            }

        } catch (Exception e) {
            System.err.println("EXCEPTION in createTask:");
            e.printStackTrace();
            System.out.println("=== REQUEST FAILED ===");
            return false;
        } finally {
            if (con != null) con.disconnect();
        }
    }

    private static String createTaskJson(TaskModel task) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"title\":\"").append(escapeJson(task.getTitle())).append("\"");

        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            json.append(",\"description\":\"").append(escapeJson(task.getDescription())).append("\"");
        } else {
            json.append(",\"description\":null");
        }

        if (task.getDeadline() != null) {
            json.append(",\"deadline\":\"").append(task.getDeadline()).append("\"");
        } else {
            json.append(",\"deadline\":null");
        }

        if (task.getStatus() != null) {
            json.append(",\"status\":\"").append(task.getStatus().name()).append("\"");
        } else {
            json.append(",\"status\":\"OPEN\"");
        }

        json.append("}");
        return json.toString();
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // ---------------------------------------------------------
    // 3️⃣ Обновить таск
    // ---------------------------------------------------------
    public static TaskModel updateTask(int taskId, TaskModel task) {
        try {
            URL url = new URL(SERVER_URL + "/tasks/update/" + taskId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            // Используем тот же метод для создания JSON
            String json = createTaskJson(task);

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
                os.flush();
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                return gson.fromJson(response.toString(), TaskModel.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------
    // 4️⃣ Удалить таск
    // ---------------------------------------------------------
    public static boolean deleteTask(Integer taskId) {
        try {
            URL url = new URL(SERVER_URL + "/tasks/delete/" + taskId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                os.write("".getBytes());
                os.flush();
            }

            int code = con.getResponseCode();
            return code == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
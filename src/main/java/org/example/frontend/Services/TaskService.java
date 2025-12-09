package org.example.frontend.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.frontend.Models.TaskModel;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TaskService {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final Gson gson = new Gson();

    // ---------------------------------------------------------
    // 1️⃣ Получить все таски группы
    // ---------------------------------------------------------
    public static List<TaskModel> getTasksByGroup(int groupId) {
        try {
            URL url = new URL(SERVER_URL + "/tasks/group/" + groupId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            Type listType = new TypeToken<List<TaskModel>>() {}.getType();
            return gson.fromJson(response.toString(), listType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------
    // 2️⃣ Создать таск (POST)
    // ---------------------------------------------------------
    public static boolean createTask(int groupId, TaskModel task) {
        HttpURLConnection con = null;

        try {
            URL url = new URL(SERVER_URL + "/tasks/group/" + groupId);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            String json = gson.toJson(task);

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            // Просто проверяем успешный HTTP статус
            int responseCode = con.getResponseCode();
            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    // ---------------------------------------------------------
    // 3️⃣ Обновить таск (POST вместо PUT)
    // ---------------------------------------------------------
    public static TaskModel updateTask(int taskId, TaskModel task) {
        try {
            URL url = new URL(SERVER_URL + "/tasks/update/" + taskId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            String json = gson.toJson(task);

            OutputStream os = con.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            return gson.fromJson(response.toString(), TaskModel.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------
    // 4️⃣ Удалить таск (POST вместо DELETE)
    // ---------------------------------------------------------
    public static boolean deleteTask(int taskId) {
        try {
            URL url = new URL(SERVER_URL + "/tasks/delete/" + taskId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            // тело не нужно
            OutputStream os = con.getOutputStream();
            os.write("".getBytes());
            os.flush();
            os.close();

            int code = con.getResponseCode();
            return code == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

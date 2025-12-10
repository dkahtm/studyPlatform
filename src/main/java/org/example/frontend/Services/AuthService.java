package org.example.frontend.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.Group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class AuthService {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final Gson gson = new Gson();

    public static boolean  login(String email, String password) {
        try {
            String params = "email=" + URLEncoder.encode(email, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");
            URL url = new URL(SERVER_URL + "/auth/login");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            try (OutputStream os = con.getOutputStream()) {
                os.write(params.getBytes("UTF-8"));
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String response = in.readLine();
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean register(String name, String email, String password) {
        try {
            String params = "name=" + URLEncoder.encode(name, "UTF-8") +
                    "&email=" + URLEncoder.encode(email, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");
            URL url = new URL(SERVER_URL + "/auth/register?");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            try (OutputStream os = con.getOutputStream()) {
                os.write(params.getBytes());
            }

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.readLine();
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createGroup(String name, String description, String createdByName) {
        try {
            String params = "name=" + URLEncoder.encode(name, "UTF-8") +
                    "&description=" + URLEncoder.encode(description, "UTF-8") +
                    "&createdByName=" + URLEncoder.encode(createdByName, "UTF-8");
            URL url = new URL(SERVER_URL + "/home/create?");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            try (OutputStream os = con.getOutputStream()) {
                os.write(params.getBytes());
            }

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.readLine();
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String showGroup() {
        try {
            URL url = new URL("http://localhost:8080/home/all"); // твій URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() != 200)
                return "[]"; // повертаємо пустий масив у разі помилки

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]"; // пустий масив при помилці
        }
    }

    public static String getStudents() {
        try {
            URL url = new URL(SERVER_URL + "/api/users/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() != 200) return "[]";

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    // 2. Добавить студента в группу
    public static Map<String, String> addStudentToGroup(Integer groupId, Integer studentId) {
        try {
            String params = "studentId=" + studentId;
            URL url = new URL(SERVER_URL + "/home/group/" + groupId + "/add-student");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            return gson.fromJson(response.toString(), type);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return error;
        }
    }

    // 3. Удалить студента из группы
    public static Map<String, String> removeStudentFromGroup(Integer groupId, Integer studentId) {
        try {
            String params = "studentId=" + studentId;
            URL url = new URL(SERVER_URL + "/home/group/" + groupId + "/remove-student");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            return gson.fromJson(response.toString(), type);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return error;
        }
    }

    public static String getGroupStudents(Integer groupId) {
        try {
            URL url = new URL(SERVER_URL + "/home/group/" + groupId + "/students");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() != 200)
                return "[]";

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public static boolean deletefromGroup(Integer groupId){
        try {
            URL url = new URL(SERVER_URL + "/home/delete/" + groupId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            BufferedReader in =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.readLine();
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }



    }


}

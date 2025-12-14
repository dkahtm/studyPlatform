package org.example.frontend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.frontend.Models.TaskModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class checkDeadlineService {
    private static final String BASE_URL = "http://localhost:8080/tasks";

    public List<TaskModel> getTasksWithDeadlineInTwoDays() throws IOException {
        URL url = new URL(BASE_URL + "/deadline2days");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.toString(), new TypeReference<List<TaskModel>>() {});
    }

}

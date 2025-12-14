package org.example.frontend.Services;


import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StatService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public JSONObject getStatsByGroup(int groupId) throws Exception {
        String url = "http://localhost:8080/stats/statuses?groupId=" + groupId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Stats response: " + response.body());

        return new JSONObject(response.body());
    }
}



package org.example.frontend;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class NotificationWsClient {

    private final OkHttpClient client;
    private WebSocket webSocket;
    private final String wsUrl; // e.g. "ws://localhost:8080/ws/notifications"

    public NotificationWsClient(String wsUrl) {
        this.wsUrl = wsUrl;
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS) // keep alive
                .build();
    }

    public void connect() {
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket opened");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // получаем JSON, парсим и показываем Alert в JavaFX-потоке
                // чтобы не добавлять jackson здесь, разберём вручную или используй jackson
                Platform.runLater(() -> {
                    // простой парсинг: ожидаем поля title и body в JSON
                    String title = extractJsonField(text, "title");
                    String body = extractJsonField(text, "body");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(title != null ? title : "Уведомление");
                    alert.setHeaderText(null);
                    alert.setContentText(body != null ? body : text);
                    alert.showAndWait();
                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                System.out.println("WebSocket closing: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
            }
        });

        // не блокируем — OkHttp управляет потоками
    }

    public void disconnect() {
        if (webSocket != null) webSocket.close(1000, "Client closing");
        client.dispatcher().executorService().shutdown();
    }

    private String extractJsonField(String json, String field) {
        // очень простой извлекатель (рекомендуется заменить на Jackson)
        String pattern = "\"" + field + "\"\\s*:\\s*\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;
        int start = idx + pattern.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
}

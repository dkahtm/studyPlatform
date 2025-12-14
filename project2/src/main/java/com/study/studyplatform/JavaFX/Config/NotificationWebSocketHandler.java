package com.study.studyplatform.JavaFX.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // опционально: обработка сообщений от клиента
    }

    public void sendNotification(NotificationMessage notification) {
        try {
            String payload = objectMapper.writeValueAsString(notification);
            TextMessage msg = new TextMessage(payload);

            sessions.forEach(s -> {
                try {
                    if (s.isOpen()) s.sendMessage(msg);
                } catch (IOException e) {
                    // логируем и удаляем сессию при ошибке
                    try { s.close(); } catch (IOException ignore) {}
                    sessions.remove(s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


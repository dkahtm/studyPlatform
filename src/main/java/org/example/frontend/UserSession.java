package org.example.frontend;

public class UserSession {
    private static Integer currentUserId;

    public static void setCurrentUserId(Integer userId) {
        currentUserId = userId;
    }

    public static Integer getCurrentUserId() {
        return currentUserId;
    }
}

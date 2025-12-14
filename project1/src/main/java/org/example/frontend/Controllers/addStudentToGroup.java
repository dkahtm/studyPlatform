package org.example.frontend.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.frontend.Models.UserModel;
import org.example.frontend.Services.AuthService;

import java.util.Map;

public class addStudentToGroup {
    @FXML
    private Button studentToGroup;
    @FXML
    private Button homePage;
    @FXML
    private TextField groupId;
    @FXML
    private TextField userId;
    @FXML
    private Label massage;

    @FXML
    private void addStudentToGroup() {
        try {
            // Получаем значения из полей
            Integer groupIdValue = Integer.parseInt(groupId.getText());
            Integer userIdValue = Integer.parseInt(userId.getText());

            // Вызываем сервис для добавления студента в группу
            Map<String, String> response = AuthService.addStudentToGroup(groupIdValue, userIdValue);

            // Проверяем результат
            if ("success".equals(response.get("status"))) {
                massage.setText("succes");
                // Очищаем поля после успешного добавления
                groupId.clear();
                userId.clear();
            } else {
                massage.setText("error");
            }

        } catch (NumberFormatException e) {
            massage.setText("enter id");
        } catch (Exception e) {
            e.printStackTrace();
            massage.setText("enter id");
        }

    }

    @FXML
    private void returnHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) homePage.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

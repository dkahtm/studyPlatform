package org.example.frontend.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.frontend.Services.AuthService;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField userEmail;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button home;

    @FXML
    private Label messageLabel;

    @FXML
    private void registerUser() {
        String name = userName.getText();
        String email = userEmail.getText();
        String password = userPassword.getText();
        boolean result = AuthService.register(name, email, password);
        if (result) {
            messageLabel.setText("Registration successful!");
        } else {
            messageLabel.setText("You already have account!");
        }
    }

    @FXML
    private void retutnhome(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/login_new.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) home.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void EnterEmail() {
        // например, просто перенос фокуса на PasswordField или ничего
        System.out.println("Email введён");
    }

    @FXML
    private void EnterPassword() {
        System.out.println("password");
    }
    @FXML
    private void EnterName() {
        System.out.println("Name");
    }
}

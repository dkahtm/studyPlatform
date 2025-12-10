package org.example.frontend.Controllers;



import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.frontend.Models.UserModel;
import org.example.frontend.Services.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.frontend.UserSession;

public class LoginController {

    @FXML
    private TextField userEmail;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button singUpButton;

    @FXML
    private Button singInButton;

    @FXML
    private Label messageLabel;
    private Integer userId;
    private  Integer groupId;

    @FXML
    private void openRegisterPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) singUpButton.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadUserAndOpenHomePage() {
        String email = userEmail.getText();
        String password = userPassword.getText();

        boolean userName = AuthService.login(email, password);

        if (userName) {

            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/home.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) singInButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }catch (Exception e) {
                e.printStackTrace();
            }

        } else if(userName == false){
            messageLabel.setText("You don't have account!");
        } else {
            messageLabel.setText("You are already logged in!");
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

}

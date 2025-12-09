package org.example.frontend.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.frontend.Services.AuthService;
import org.example.frontend.UserSession;

public class GroupController {
    @FXML
    private Button group;
    @FXML
    private TextField groupName;
    @FXML
    private TextField userName;

    @FXML
    private Button back;
    @FXML
    private TextField groupDescription;
    @FXML
    private Label createdBy;


    @FXML
    public void createGroup(){
        String name = groupName.getText();
        String description = groupDescription.getText();
        String createdByName = userName.getText();

        Boolean result = AuthService.createGroup(name, description, createdByName);

        if (result.equals(true)) {
            createdBy.setText("Group created by: " + createdByName);
        } else if (result.equals(false)) {
            createdBy.setText("Group already exists!");
        }  else {
            createdBy.setText("another");
        }
    }
    @FXML
    public void returnHome(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) back.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

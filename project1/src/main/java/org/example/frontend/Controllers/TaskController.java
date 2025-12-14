package org.example.frontend.Controllers;



import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.frontend.Models.TaskModel;
import org.example.frontend.Services.TaskService;

public class TaskController {
    @FXML
    private Button returnButton;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private Button createButton;

    private final Gson gson = new Gson();

    private Integer groupId;
    private Integer userId;

    @FXML
    public void initialize() {
        statusCombo.getItems().addAll("OPEN", "IN_PROGRESS", "DONE");
        statusCombo.getSelectionModel().select("OPEN");
    }

    // Устанавливаем данные при открытии окна
    public void setData(Integer groupId, Integer userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    @FXML
    private void createTask() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String status = statusCombo.getValue();
        String deadline = (deadlinePicker.getValue() != null)
                ? deadlinePicker.getValue().toString()
                : null;

        if (title.isEmpty()) {
            showAlert("Ошибка", "Название таска обязательно!");
            return;
        }
        TaskModel task = new TaskModel();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskModel.Status.valueOf(status)); // "OPEN" → ENUM
        task.setDeadline(deadlinePicker.getValue());

        System.out.println("groupId = " + groupId);

        boolean createdTask = TaskService.createTask(groupId, task);



        if (createdTask) {
            showAlert("success", "Task created successfully!");
            clearFields();
        } else {
            showAlert("error", "Task creation failed!");
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        statusCombo.getSelectionModel().select("OPEN");
        deadlinePicker.setValue(null);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void returnHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnButton.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }



    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}

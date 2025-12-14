package org.example.frontend.Controllers;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.frontend.Models.TaskFile;
import org.example.frontend.Services.FileService;

import java.io.File;
import java.util.List;

public class FileController {

    @FXML
    private Button uploadFile;

    @FXML
    private ListView<TaskFile> fileList;

    private Integer taskId;
    private FileService fileService = new FileService();

    private HostServices hostServices; // нужен для открытия файлов на системе

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
        loadFilesForTask();
    }

    @FXML
    public void initialize() {
        // Двойной клик по файлу для открытия
        fileList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TaskFile selected = fileList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openFile(selected);
                }
            }
        });
    }

    @FXML
    public void addFile() {
        if (taskId == null) {
            showErrorMessage("Ошибка: задача не выбрана");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt", "*.doc", "*.docx", "*.pdf"),
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Архивы", "*.zip", "*.rar", "*.7z")
        );

        Stage stage = (Stage) uploadFile.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                TaskFile uploaded = fileService.uploadFile(selectedFile, Long.valueOf(taskId));
                if (uploaded != null) {
                    showSuccessMessage("Файл '" + uploaded.getFileName() + "' успешно загружен!");
                    loadFilesForTask();
                } else {
                    showErrorMessage("Не удалось загрузить файл");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Ошибка: " + e.getMessage());
            }
        }
    }

    public void loadFilesForTask() {
        if (taskId == null) return;

        try {
            List<TaskFile> files = fileService.getFiles(Long.valueOf(taskId));
            fileList.getItems().clear();
            if (files.isEmpty()) {
                fileList.getItems().add(null); // можно добавить placeholder
            } else {
                fileList.getItems().addAll(files);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileList.getItems().clear();
            showErrorMessage("Ошибка загрузки файлов");
        }
    }

    private void openFile(TaskFile taskFile) {
        try {
            // Скачиваем файл во временную папку
            File tempFile = File.createTempFile("tmp_", "_" + taskFile.getFileName());

            fileService.downloadFile(taskFile, tempFile);
            showSuccessMessage("File downloaded successfully: " + tempFile.getAbsolutePath());

            // Открываем файл через систему
            if (hostServices != null) {
                hostServices.showDocument(tempFile.toURI().toString());
            } else {
                tempFile.deleteOnExit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Ошибка открытия файла: " + e.getMessage());
        }
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}





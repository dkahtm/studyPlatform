package org.example.frontend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.frontend.Models.TaskModel;
import org.example.frontend.Models.TaskModel2;
import org.example.frontend.Models.UserModel;
import org.example.frontend.Services.AuthService;
import org.example.frontend.Services.TaskService;
import org.example.frontend.Services.TaskService2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.example.frontend.Services.AuthService.showGroup;

public class HomeController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button Student;
    @FXML
    private Button showStudentButton;
    @FXML
    private Button task;
    @FXML
    private Button deleteButton;
    @FXML
    private Button edit;
    @FXML
    private Button group;
    @FXML
    private ListView<String> groupList;
    @FXML
    private ListView<String> studentsList;

    @FXML
    private ListView<String> taskList;


    private TaskService2 taskService2 = new TaskService2();

    @FXML
    private Button deleteStudentButton;

    private Integer selectedStudentId;
    private Integer selectedGroupId;
    private Integer currentGroupId;
    private Integer groupId;
    private Integer userId;


    public void setData(Integer groupId, Integer userId) {
        this.groupId = groupId;
        this.userId = userId;
    }




    @FXML
    public void addGroup(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/create_group.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) group.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void addStudent(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/addStudentToGroup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) group.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addTask(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/CreateTask.fxml"));
            Parent root = loader.load();

            TaskController controller = loader.getController();
            controller.setGroupId(selectedGroupId);

            Stage stage = (Stage) task.getScene().getWindow(); // получить текущее окно
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void editTask(){
        System.out.println("e");
    }
    @FXML
    public void deleteTask(){
        System.out.println("d");
    }


    /*private void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(
                showGroup()
        );
        groupList.setItems(items);
    }*/
    @FXML
    public void initialize() {
       loadGroups();

        groupList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Извлекаем ID группы из выбранной строки
                        // Например: "ID: 1 | Name: Test Group | Description: Test"
                        selectedGroupId = extractGroupId(newValue);

                        // Активируем кнопки, которые требуют выбора группы
                        deleteButton.setDisable(false);
                        //deleteGroupButton.setDisable(false); // если есть такая кнопка

                        // Загружаем студентов этой группы
                        //loadStudentsForGroup(selectedGroupId);


                    } else {
                        selectedGroupId = null;

                        // Деактивируем кнопки
                        deleteButton.setDisable(true);
                        //deleteGroupButton.setDisable(true);

                        // Очищаем список студентов
                        //studentsList.getItems().clear();
                    }
                }
        );


        groupList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Извлекаем ID группы из выбранной строки
                        // Например: "ID: 1 | Name: Test Group | Description: Test"
                        selectedGroupId = extractGroupId(newValue);

                        // Активируем кнопки, которые требуют выбора группы
                        task.setDisable(false);
                        //deleteGroupButton.setDisable(false); // если есть такая кнопка

                        // Загружаем студентов этой группы
                        //loadStudentsForGroup(selectedGroupId);


                    } else {
                        selectedGroupId = null;

                        // Деактивируем кнопки
                        task.setDisable(true);
                        //deleteGroupButton.setDisable(true);

                        // Очищаем список студентов
                        //studentsList.getItems().clear();
                    }
                }
        );

        // Добавляем слушатель выбора студента
        studentsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Извлекаем ID студента из выбранной строки
                        // Например: "ID: 302 | Name: Dima,Dima | Email: dima@gmail.com"
                        selectedStudentId = extractStudentId(newValue);
                        deleteStudentButton.setDisable(false);
                    } else {
                        selectedStudentId = null;
                        deleteStudentButton.setDisable(true);
                    }
                }
        );
        groupList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                try {
                    // Вытаскиваем ID из строки группы
                    int groupId = Integer.parseInt(newV.split("\\|")[0].replace("ID:", "").trim());
                    loadTasks(groupId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Изначально кнопка неактивна
        deleteStudentButton.setDisable(true);
        deleteButton.setDisable(true);
    }


    private void loadTasks(Integer groupId) {
        try {
            // Получаем JSON задач с сервера
            String json = taskService2.getTasksByGroupJson(groupId); // допустим, метод возвращает JSON строку
            System.out.println("Tasks JSON for group " + groupId + ": " + json);

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // обязательно для LocalDate / LocalDateTime

            // Парсим JSON в список Map
            List<Map<String, Object>> tasksData = mapper.readValue(json, List.class);

            List<String> taskItems = new ArrayList<>();
            for (Map<String, Object> task : tasksData) {
                taskItems.add(
                        "ID: " + task.get("taskId") +
                                " | Title: " + task.get("title") +
                                " | Status: " + task.get("status") +
                                " | Deadline: " + task.get("deadline")
                );
            }

            // Если задач нет, показываем сообщение
            if (taskItems.isEmpty()) {
                taskItems.add("No tasks for this group");
            }

            // Устанавливаем в ListView
            taskList.setItems(FXCollections.observableArrayList(taskItems));

        } catch (Exception e) {
            e.printStackTrace();
            taskList.setItems(FXCollections.observableArrayList("Error loading tasks"));
        }
    }



    private void loadGroups() {
        try {
            String json = showGroup();

            // Парсимо JSON як масив Map
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            // Конвертуємо JSON у масив Map
            List<Map<String, Object>> groups = mapper.readValue(json, List.class);

            // Формуємо список рядків для ListView
            List<String> items = new ArrayList<>();
            for (Map<String, Object> g : groups) {
                items.add(
                        "ID: " + g.get("id") +
                                " | Name: " + g.get("name") +
                                " | Description: " + g.get("description") +
                                " | CreatedBy: " + g.get("createdBy") +
                                " | CreatedAt: " + g.get("createdAt")
                );
            }

            // Додаємо у ListView
            groupList.setItems(FXCollections.observableArrayList(items));

            groupList.getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            // Извлекаем ID группы из строки (первое число после "ID: ")
                            String idStr = newValue.split("\\|")[0].replace("ID:", "").trim();
                            try {
                                Integer groupId = Integer.parseInt(idStr);
                                currentGroupId = groupId;
                                loadGroupStudents(groupId);


                                // Также показываем название группы
                                String groupName = newValue.split("\\|")[1].replace("Name:", "").trim();
                                nameLabel.setText("Group: " + groupName);
                                descriptionLabel.setText("ID: " + idStr);

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadGroupStudents(Integer groupId) {
        try {
            String json = AuthService.getGroupStudents(groupId);
            System.out.println("Students JSON for group " + groupId + ": " + json);

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> studentsData = mapper.readValue(json, List.class);

            List<String> studentItems = new ArrayList<>();
            for (Map<String, Object> student : studentsData) {
                studentItems.add(
                        "ID: " + student.get("id") +
                                " | Name: " + student.get("name") +
                                " | Email: " + student.get("email")
                );
            }

            // Если студентов нет, показываем сообщение
            if (studentItems.isEmpty()) {
                studentItems.add("No students in this group");
            }

            studentsList.setItems(FXCollections.observableArrayList(studentItems));

        } catch (Exception e) {
            e.printStackTrace();
            studentsList.setItems(FXCollections.observableArrayList("Error loading students"));
        }
    }
    @FXML
    public void deleteGroup() {
        boolean response = AuthService.deletefromGroup(currentGroupId);
        if (response) {
            System.out.println("group removed successfully");
            // Обновляем список студентов
            loadGroupStudents(currentGroupId);
        } else {
            System.err.println("Error removing student: ");
        }

    }

    @FXML
    public void handleDeleteStudent() {
        if (selectedStudentId == null || currentGroupId == null) {
            System.err.println("Error: Student or Group not selected");
            return;
        }

        // Вызываем API для удаления
        Map<String, String> response = AuthService.removeStudentFromGroup(currentGroupId, selectedStudentId);

        if ("success".equals(response.get("status"))) {
            System.out.println("Student removed successfully");
            // Обновляем список студентов
            loadGroupStudents(currentGroupId);
        } else {
            System.err.println("Error removing student: " + response.get("message"));
        }
    }

    // Метод для извлечения ID студента из строки
    private Integer extractStudentId(String studentInfo) {
        try {
            // Пример входной строки:
            // "ID: 302 | Name: Dima | Email: dima@gmail.com"

            String[] parts = studentInfo.split("\\|");  // разбиваем по "|"
            String idPart = parts[0].trim();            // "ID: 302"

            String idValue = idPart.split(":")[1].trim();  // "302"

            return Integer.parseInt(idValue);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Integer extractGroupId(String groupInfo) {
        try {
            // Пример входной строки:
            // "ID: 302 | Name: Dima | Email: dima@gmail.com"

            String[] parts = groupInfo.split("\\|");  // разбиваем по "|"
            String idPart = parts[0].trim();            // "ID: 302"

            String idValue = idPart.split(":")[1].trim();  // "302"

            return Integer.parseInt(idValue);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void openList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/frontend/allStudents.fxml"));
            Parent root = loader.load();

            // создаём новое окно
            Stage newStage = new Stage();
            newStage.setTitle("All Students");
            newStage.setScene(new Scene(root));

            newStage.show(); // показываем новое доп. окно

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}

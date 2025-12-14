package org.example.frontend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.frontend.Services.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentsController {

    @FXML
    private ListView<String> allStudentsList; // <-- это должен быть ListView!

    @FXML
    public void initialize() {
        loadAllStudentsList();
    }

    public void loadAllStudentsList() {
        try {
            String json = AuthService.getStudents();


            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            List<Map<String, Object>> students = mapper.readValue(json, List.class);

            List<String> items = new ArrayList<>();
            for (Map<String, Object> st : students) {

                Object groupValue = st.get("group");
                String groupText;

                if (groupValue == null) {
                    groupText = "none";
                } else if (groupValue instanceof Map) {
                    // Если group — объект, выводим его имя или id
                    Map<String, Object> g = (Map<String, Object>) groupValue;
                    groupText = "GroupID: " + g.get("id");
                } else {
                    groupText = groupValue.toString();
                }

                items.add(
                        "ID: " + st.get("id") +
                                " | Name: " + st.get("name") +
                                " | Email: " + st.get("email") +
                                " | Group: " + groupText
                );
            }

            allStudentsList.getItems().setAll(items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

